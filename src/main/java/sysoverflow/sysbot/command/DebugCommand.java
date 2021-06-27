package sysoverflow.sysbot.command;

import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;

public class DebugCommand implements Command {

    private final SysBot bot;

    public DebugCommand(@NotNull SysBot bot) {
        this.bot = bot;
    }

    @Override
    @NotNull
    public CommandData getData() {
        return new CommandData("debug", "Runs bot debugging code. Intended for development purposes.");
               // .setDefaultEnabled(false);
    }

    @Override
    public void execute(@NotNull CommandInteraction interaction) {
        bot.getUserStore().createUser(interaction.getUser().getIdLong());
        interaction.reply("Created user profile!").queue();
    }
}
