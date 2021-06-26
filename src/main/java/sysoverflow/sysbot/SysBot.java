package sysoverflow.sysbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.command.CommandHandler;
import sysoverflow.sysbot.command.PingCommand;

import javax.security.auth.login.LoginException;

public class SysBot extends ListenerAdapter {

    private final JDA jda;
    private final CommandHandler commandHandler;
    private Guild primaryGuild;

    public SysBot(@NotNull String token) throws LoginException {
        this.jda = JDABuilder.createDefault(token)
                .setActivity(Activity.competing("banana"))
                .addEventListeners(this)
                .build();

        this.commandHandler = new CommandHandler(this);
    }

    @NotNull
    public JDA getJda() {
        return jda;
    }

    @NotNull
    public CommandHandler getCommandHandler() {
        return commandHandler;
    }

    @NotNull
    public Guild getPrimaryGuild() {
        return primaryGuild;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        primaryGuild = event.getJDA().getGuilds().get(0); // TODO a bit hacky

        // Register commands
        commandHandler.register(new PingCommand(this));
    }
}
