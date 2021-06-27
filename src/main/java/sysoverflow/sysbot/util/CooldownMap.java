package sysoverflow.sysbot.util;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CooldownMap extends HashMap<Long, Long> {

    public boolean isUnderCooldown(long snowflake) {
        return containsKey(snowflake)
                && getSecondsPassed(get(snowflake)) < 5;
    }

    public void logCooldown(long snowflake) {
        put(snowflake, System.currentTimeMillis());
    }

    public void purgeEntries() {
        entrySet().removeIf(entry -> getSecondsPassed(entry.getValue()) >= 5);
    }

    private long getSecondsPassed(long timestamp) {
        return TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - timestamp);
    }
}
