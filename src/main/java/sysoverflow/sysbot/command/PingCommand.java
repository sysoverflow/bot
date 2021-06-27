package sysoverflow.sysbot.command;

import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;

public class PingCommand implements Command {

    private final SysBot bot;

    public PingCommand(@NotNull SysBot bot) {
        this.bot = bot;
    }

    @Override
    @NotNull
    public CommandData getData() {
        return new CommandData("ping", "Sends a ping request to the bot.");
    }

    @Override
    public void execute(@NotNull CommandInteraction interaction) {
        interaction.reply("Pong! My ping to Discord is " + bot.getJda().getGatewayPing() + " ms.").queue();
    }
}
