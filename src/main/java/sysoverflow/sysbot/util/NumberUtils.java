package sysoverflow.sysbot.util;

import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;

public class NumberUtils {

    @NotNull
    public static String format(int number) {
        return NumberFormat.getInstance().format(number);
    }
}
