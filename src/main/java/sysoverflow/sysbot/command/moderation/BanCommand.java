package sysoverflow.sysbot.command.moderation;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;
import sysoverflow.sysbot.command.Command;
import sysoverflow.sysbot.util.NumberUtils;

public class BanCommand implements Command {

    private final SysBot bot;

    public BanCommand(SysBot bot) { this.bot = bot; }

    @Override
    public @NotNull CommandData getData() {
        return new CommandData("ban", "Have bongo cat swing the ban hammer on someone")
                .addOption(OptionType.USER, "member", "The member you wish to let bongo cat smack with ban hammer", true);
    }

    @Override
    public void execute(@NotNull CommandInteraction interaction) {
        if(!interaction.getMember().hasPermission(Permission.BAN_MEMBERS)){
            var embed = new EmbedBuilder()
                    .setColor(0xde2e21)
                    .setDescription("You do not have permission to ban members from this guild! If you believe this is in error please contact the guild owner.")
                    .build();
            interaction.replyEmbeds(embed).setEphemeral(true).queue();
        } else {
            interaction.reply("hi").setEphemeral(true).queue();
        }

    }
}
