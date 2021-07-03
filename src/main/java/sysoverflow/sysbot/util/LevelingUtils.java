package sysoverflow.sysbot.util;

public class LevelingUtils {

    public static int getLevelFromXp(double xp) {
        return (int) Math.sqrt(xp - 35);
    }
}
