package sysoverflow.sysbot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;

import java.util.Optional;

public class ProfileCommand implements Command {

    private final SysBot bot;

    public ProfileCommand(@NotNull SysBot bot) {
        this.bot = bot;
    }

    @Override
    @NotNull
    public CommandData getData() {
        return new CommandData("profile", "Displays your member profile information.")
                .addOption(OptionType.USER, "member", "The member who's profile you wish to view.", false);
    }

    @Override
    public void execute(@NotNull CommandInteraction event) {
        var member = Optional.ofNullable(event.getOption("member"))
                .map(OptionMapping::getAsMember)
                .orElse(event.getMember());

        if (member == null) {
            event.reply("The requested member is not in this server.").queue();
            return;
        }

        bot.getUserStore().getUser(member.getIdLong()).ifPresentOrElse(document -> {
            var embed = new EmbedBuilder()
                    .setColor(0xFFFFFF)
                    .setTitle(member.getEffectiveName() + "'s profile")
                    .setThumbnail(member.getUser().getEffectiveAvatarUrl())
                    .addField("XP", document.getDouble("xp") + "", true)
                    .addField("Coins", document.getInteger("coins") + "", true)
                    .build();

            event.replyEmbeds(embed).queue();
        }, () -> event.reply("The requested member does not have a profile.").queue());
    }
}
