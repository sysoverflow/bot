package sysoverflow.sysbot;

import com.mongodb.ConnectionString;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public class Bootstrap {

    public static void main(String[] args) throws LoginException {
        Arrays.stream(args).forEach(System.out::println);

        if (args.length < 2) {
            System.out.println("Provide a bot token to bootstrap sysbot.");
            return;
        }

        new SysBot(args[0], new ConnectionString(args[1]));
    }
}
