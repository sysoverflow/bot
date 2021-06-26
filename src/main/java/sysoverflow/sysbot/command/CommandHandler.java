package sysoverflow.sysbot.command;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler extends ListenerAdapter {

    private final SysBot bot;
    private final Map<String, Command> registeredCommands;

    public CommandHandler(@NotNull SysBot bot) {
        this.bot = bot;
        this.registeredCommands = new HashMap<>();

        bot.getJda().addEventListener(this);
    }

    public void register(@NotNull Command... commands) {
        Arrays.stream(commands).forEach(this::register);
    }

    private void register(@NotNull Command command) {
        registeredCommands.put(command.getData().getName().toLowerCase(), command);
        bot.getPrimaryGuild().upsertCommand(command.getData()).queue();
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        var command = registeredCommands.get(event.getName().toLowerCase());

        if (command == null) {
            event.reply("Unrecognized command.").queue();
            return;
        }

        command.execute((CommandInteraction) event.getInteraction());
    }
}
