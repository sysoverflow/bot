package sysoverflow.sysbot;

import com.mongodb.ConnectionString;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.command.CommandHandler;
import sysoverflow.sysbot.data.UserStore;
import sysoverflow.sysbot.listener.MessageListener;

import java.util.Properties;

public class SysBot {

    private final JDA jda;
    private final CommandHandler commandHandler;
    private final UserStore userStore;
    private final Guild primaryGuild;

    public SysBot(@NotNull Properties properties) throws Exception {
        this.jda = JDABuilder.createDefault(properties.getProperty("botToken"))
                .setActivity(Activity.competing("banana"))
                .addEventListeners(new MessageListener(this))
                .build()
                .awaitReady();
        this.commandHandler = new CommandHandler(this);
        this.userStore = new UserStore(this, new ConnectionString(properties.getProperty("mongoUri")));
        this.primaryGuild = jda.getGuildById(properties.getProperty("primaryGuild"));
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
}
