package sysoverflow.sysbot;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Bootstrap {

    public static void main(String[] args) throws Exception {
        var file = new File("sysbot.properties");

        if (!file.exists()) {
            System.out.println("Failed to locate sysbot.properties.");
            return;
        }

        var properties = new Properties();
        properties.load(new FileInputStream(file));
        new SysBot(properties);
    }
}
