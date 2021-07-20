package sysoverflow.sysbot;

import com.mongodb.ConnectionString;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.command.CommandHandler;
import sysoverflow.sysbot.listener.GuildMemberListener;
import sysoverflow.sysbot.profile.ProfileStorage;
import sysoverflow.sysbot.listener.MessageListener;

import java.util.Properties;

public class SysBot {

    private final JDA jda;
    private final CommandHandler commandHandler;
    private final ProfileStorage profileStorage;
    private final Guild primaryGuild;

    public SysBot(@NotNull Properties properties) throws Exception {
        this.jda = JDABuilder.createDefault(properties.getProperty("botToken"))
                .setActivity(Activity.competing("banana"))
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build()
                .awaitReady();
        this.primaryGuild = jda.getGuildById(properties.getProperty("primaryGuild"));
        this.commandHandler = new CommandHandler(this);
        this.profileStorage = new ProfileStorage(this, new ConnectionString(properties.getProperty("mongoUri")));

        jda.addEventListener(new MessageListener(this));
        jda.addEventListener(new GuildMemberListener(this));
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
    public ProfileStorage getProfiles() {
        return profileStorage;
    }

    @NotNull
    public Guild getPrimaryGuild() {
        return primaryGuild;
    }
}
