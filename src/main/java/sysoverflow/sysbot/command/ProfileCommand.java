package sysoverflow.sysbot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

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
    public void execute(@NotNull CommandInteraction interaction) {
        var member = Optional.ofNullable(interaction.getOption("member"))
                .map(OptionMapping::getAsMember)
                .orElse(interaction.getMember());

        if (member == null) {
            interaction.reply("The requested member is not in this server.").queue();
            return;
        }

        ForkJoinPool.commonPool().submit(() -> execute0(interaction, member));
    }

    private void execute0(@NotNull CommandInteraction interaction, @NotNull Member member) {
        bot.getUserStore().getUser(member.getIdLong()).ifPresentOrElse(document -> {
            var embed = new EmbedBuilder()
                    .setColor(0xFFFFFF)
                    .setTitle(member.getEffectiveName() + "'s profile")
                    .setThumbnail(member.getUser().getEffectiveAvatarUrl())
                    .addField("XP", document.getDouble("xp") + "", true)
                    .addField("Coins", document.getInteger("coins") + "", true)
                    .build();

            interaction.replyEmbeds(embed).queue();
        }, () -> interaction.reply("The requested member does not have a profile.").queue());
    }
}
