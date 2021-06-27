package sysoverflow.sysbot.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import sysoverflow.sysbot.SysBot;

import java.util.Optional;
import java.util.concurrent.ForkJoinPool;

import static com.mongodb.client.model.Filters.eq;

public class UserStore extends ListenerAdapter {

    private final SysBot bot;
    private final MongoCollection<Document> collection;

    public UserStore(@NotNull SysBot bot, @NotNull ConnectionString connectionString) {
        this.bot = bot;
        var settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .retryWrites(true)
                .build();

        this.collection = MongoClients.create(settings)
                .getDatabase("sysbot")
                .getCollection("members");

        bot.getJda().addEventListener(this);
    }

    @NotNull
    public Optional<Document> getUser(long snowflake) {
        return Optional.ofNullable(collection.find(eq("_id", snowflake)).first());
    }

    public void createUser(long snowflake) {
        if (getUser(snowflake).isPresent()) {
            return;
        }

        var document = new Document("_id", snowflake)
                .append("coins", 0)
                .append("xp", 0D);

        collection.insertOne(document);
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        ForkJoinPool.commonPool().submit(() -> createUser(event.getUser().getIdLong()));
    }
}
