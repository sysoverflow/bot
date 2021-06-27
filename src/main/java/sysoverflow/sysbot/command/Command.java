package sysoverflow.sysbot.command;

import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a basic command interaction.
 */
public interface Command {

    @NotNull
    CommandData getData();

    void execute(@NotNull CommandInteraction interaction);
}
