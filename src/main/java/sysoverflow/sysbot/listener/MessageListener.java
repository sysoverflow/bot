package sysoverflow.sysbot.listener;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;
import sysoverflow.sysbot.util.CooldownMap;

import java.util.concurrent.ForkJoinPool;

public class MessageListener extends ListenerAdapter {

    private final SysBot bot;
    private final CooldownMap cooldowns;

    public MessageListener(@NotNull SysBot bot) {
        this.bot = bot;
        this.cooldowns = new CooldownMap();
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem()) {
            return;
        }

        var snowflake = event.getAuthor().getIdLong();

        if (cooldowns.isUnderCooldown(snowflake)) {
            return;
        }

        cooldowns.logCooldown(snowflake);
        ForkJoinPool.commonPool().submit(() -> bot.getUserStore().incrementXp(snowflake, 5.0D));
    }
}
