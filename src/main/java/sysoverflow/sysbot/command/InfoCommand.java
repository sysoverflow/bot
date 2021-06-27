package sysoverflow.sysbot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;

public class InfoCommand implements Command {

    private final SysBot bot;

    public InfoCommand(@NotNull SysBot bot) {
        this.bot = bot;
    }

    @Override
    @NotNull
    public CommandData getData() {
        return new CommandData("info", "View information about the bot instance.");
    }

    @Override
    public void execute(@NotNull CommandInteraction interaction) {
        var version = getClass().getPackage().getImplementationVersion();
        var embed = new EmbedBuilder()
                .setDescription("🖇 sysbot information")
                .setThumbnail(bot.getJda().getSelfUser().getEffectiveAvatarUrl())
                .addField("Version " + (version == null ? "DEV" : version), "Running on Java " + Runtime.version(), false)
                .addField(bot.getJda().getGatewayPing() + " ms ping", "Connected to Discord API", false)
                .build();

        interaction.replyEmbeds(embed).queue();
    }
}
