package sysoverflow.sysbot.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;

public class GuildMemberListener extends ListenerAdapter {

    private final TextChannel notificationChannel;

    public GuildMemberListener(@NotNull SysBot bot) {
        this.notificationChannel = bot.getPrimaryGuild().getTextChannelById("528733373200334878");
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        var name = event.getUser().getName();
        var embed = new EmbedBuilder()
                .setColor(0xffe605)
                .setThumbnail(event.getUser().getEffectiveAvatarUrl())
                .addField(
                        "Welcome to sysoverflow, " + name + "!",
                        "Feel free to look around. Use `/about` for more information about the server!",
                        false
                )
                .build();

        notificationChannel.sendMessage(event.getUser().getAsMention())
                .setEmbeds(embed)
                .queue();
    }
}
