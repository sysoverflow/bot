package sysoverflow.sysbot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;
import sysoverflow.sysbot.util.LevelingUtils;
import sysoverflow.sysbot.util.NumberUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicInteger;

public class LeaderboardCommand implements Command {

    private final SysBot bot;

    public LeaderboardCommand(@NotNull SysBot bot) {
        this.bot = bot;
    }

    @Override
    @NotNull
    public CommandData getData() {
        return new CommandData("leaderboard", "Displays the profile leaderboard.");
    }

    @Override
    public void execute(@NotNull CommandInteraction interaction) {
        ForkJoinPool.commonPool().submit(() -> execute0(interaction));
    }

    public void execute0(@NotNull CommandInteraction interaction) {
        var place = new AtomicInteger();
        var embed = new EmbedBuilder()
                .setColor(0xb0878f)
                .setTitle("📚 The rankings")
                .setDescription("The most active members of our community!")
                .setImage("https://media1.tenor.com/images/44ec016502c96929a17cb251c81e0100/tenor.gif");

        getLeaderboard().stream()
                .map(profile -> createField(profile, place.incrementAndGet()))
                .forEach(embed::addField);

        interaction.replyEmbeds(embed.build()).queue();
    }

    @NotNull
    private List<Document> getLeaderboard() {
        var list = new ArrayList<Document>();
        var profiles = bot.getProfiles().getCollection().find().sort(new Document("xp", -1)).limit(6);

        for (Document profile : profiles) {
            list.add(profile);
        }

        return list;
    }

    @NotNull
    private MessageEmbed.Field createField(@NotNull Document document, int place) {
        var symbol = getPlaceSymbol(place);
        var user = bot.getJda().retrieveUserById(document.getLong("_id")).complete();
        var name = user.getName() + "#" + user.getDiscriminator();
        var xp = document.getDouble("xp");
        var coins = document.getInteger("coins");

        var title = String.format("%s %s (Level %s)", symbol, name, LevelingUtils.getLevelFromXp(xp));
        var description = String.format("%s XP - %s coins", NumberUtils.format(xp.intValue()), NumberUtils.format(coins));
        return new MessageEmbed.Field(title, description, true);
    }

    @NotNull
    private String getPlaceSymbol(int place) {
        return switch (place) {
            case 1 -> "🥇";
            case 2 -> "🥈";
            case 3 -> "🥉";
            default -> "👑";
        };
    }
}
