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

import java.util.Objects;
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

    // TODO convoluted logic

    @NotNull
    public Optional<Document> getUser(long snowflake) {
        var document = collection.find(eq("_id", snowflake)).first();
        return Optional.of(Objects.requireNonNullElseGet(document, () -> createUser(snowflake)));
    }

    @NotNull
    public Document createUser(long snowflake) {
        var existing = getUser(snowflake);

        if (existing.isPresent()) {
            return existing.get();
        }

        var document = new Document("_id", snowflake)
                .append("coins", 0)
                .append("xp", 0D);

        collection.insertOne(document);
        return document;
    }

    public void incrementXp(long snowflake, double amount) {
        var update = new Document("$inc", new Document("xp", amount));
        collection.updateOne(eq("_id", snowflake), update);
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        ForkJoinPool.commonPool().submit(() -> createUser(event.getUser().getIdLong()));
    }
}
