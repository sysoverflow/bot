package sysoverflow.sysbot;

import com.mongodb.ConnectionString;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.command.CommandHandler;
import sysoverflow.sysbot.command.DebugCommand;
import sysoverflow.sysbot.command.InfoCommand;
import sysoverflow.sysbot.command.PingCommand;
import sysoverflow.sysbot.command.ProfileCommand;
import sysoverflow.sysbot.data.UserStore;
import sysoverflow.sysbot.listener.MessageListener;

import javax.security.auth.login.LoginException;
import java.util.Properties;

public class SysBot extends ListenerAdapter {

    private final JDA jda;
    private final CommandHandler commandHandler;
    private final UserStore userStore;
    private Guild primaryGuild;

    public SysBot(@NotNull Properties properties) throws LoginException {
        this.jda = JDABuilder.createDefault(properties.getProperty("botToken"))
                .setActivity(Activity.competing("banana"))
                .addEventListeners(this, new MessageListener(this))
                .build();

        this.commandHandler = new CommandHandler(this);
        this.userStore = new UserStore(this, new ConnectionString(properties.getProperty("mongoUri")));
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
    public UserStore getUserStore() {
        return userStore;
    }

    @NotNull
    public Guild getPrimaryGuild() {
        return primaryGuild;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        primaryGuild = event.getJDA().getGuilds().get(0); // TODO a bit hacky

        // Register commands
        commandHandler.register(
                new DebugCommand(this),
                new InfoCommand(this),
                new PingCommand(this),
                new ProfileCommand(this)
        );
    }
}
