package sysoverflow.sysbot.profile;

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

public class ProfileStorage extends ListenerAdapter {

    private final SysBot bot;
    private final MongoCollection<Document> collection;

    public ProfileStorage(@NotNull SysBot bot, @NotNull ConnectionString connectionString) {
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
    public Optional<Document> getProfile(long snowflake) {
        return Optional.ofNullable(collection.find(eq("_id", snowflake)).first());
    }

    @NotNull
    public Document getOrCreateProfile(long snowflake) {
        return getProfile(snowflake).orElse(createProfile(snowflake));
    }

    @NotNull
    private Document createProfile(long snowflake) {
        var document = new Document("_id", snowflake)
                .append("coins", 0)
                .append("xp", 0D);

        collection.insertOne(document);
        return document;
    }

    public void incrementField(long snowflake, String field, double amount) {
        var update = new Document("$inc", new Document(field, amount));
        collection.updateOne(eq("_id", snowflake), update);
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        ForkJoinPool.commonPool().submit(() -> getOrCreateProfile(event.getUser().getIdLong()));
    }
}
