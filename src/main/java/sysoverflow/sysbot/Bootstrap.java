package sysoverflow.sysbot;

import javax.security.auth.login.LoginException;

public class Bootstrap {

    public static void main(String[] args) throws LoginException {
        if (args.length < 1) {
            System.out.println("Provide a bot token to bootstrap sysbot.");
            return;
        }

        new SysBot(args[0]);
    }
}
