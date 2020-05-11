package com.fenyx.core.console;

import com.fenyx.utils.StringUtils;

import java.util.Arrays;
import java.util.LinkedHashSet;

/**
 *
 * @author DarkPartizaN
 */
public class EngineConsole {

    private final LinkedHashSet<ConsoleCommand> registered_commands = new LinkedHashSet<>();

    public void init() {
        //Console commands
        addCommand(new CmdlistCommand());
        addCommand(new ClearCommand());
        addCommand(new ExitCommand());
        addCommand(new QuitCommand());
    }

    public void exec(String cmd) {
        String[] cmd_out = StringUtils.splitString(cmd, " ");
        exec(cmd_out);
    }

    public void exec(String... cmd) {
        if (registered_commands.isEmpty()) {
            warn_print(StringUtils.concat("Command list is empty!"));
            return;
        }

        String name = cmd[0];

        String[] args = null;
        if (cmd.length > 1) {
            args = new String[cmd.length - 1];
            System.arraycopy(cmd, 1, args, 0, cmd.length - 1);
        }

        for (ConsoleCommand c : registered_commands) {
            if (name.equals(c.name)) {
                c.exec(args);
                return;
            }
        }

        warn_print(StringUtils.concat("Command ", name, " was not found!")); // Command not registered
    }

    public void con_print(String s) {
        System.out.println(s);
    }

    public void info_print(String s) {
        System.out.println(StringUtils.concat("[I] ", s));
    }

    public void debug_print(String s) {
        System.out.println(StringUtils.concat("[D] ", s));
    }

    public void warn_print(String s) {
        System.out.println(StringUtils.concat("[W] ", s));
    }

    public void err_print(String s) {
        System.out.println(StringUtils.concat("[E] ", s));
    }

    public void addCommand(ConsoleCommand cmd) {
        cmd.init();
        registered_commands.add(cmd);
        sortCommands();
    }

    private void sortCommands() {
        ConsoleCommand[] tmp;
        ConsoleCommand cc1, cc2, ctmp;
        char c1, c2;

        tmp = registered_commands.toArray(new ConsoleCommand[registered_commands.size()]);

        for (int i = 0; i < registered_commands.size(); i++) {
            for (int j = registered_commands.size() - 2; j >= i; j--) {
                cc1 = tmp[j];
                cc2 = tmp[j + 1];

                c1 = cc1.name.charAt(0);
                c2 = cc2.name.charAt(0);

                if (c1 > c2) {
                    ctmp = tmp[j];
                    tmp[j] = cc2;
                    tmp[j + 1] = ctmp;
                }
            }
        }

        registered_commands.clear();
        registered_commands.addAll(Arrays.asList(tmp));
    }

    //===============
    //CONSOLE COMMANDS
    //===============
    public class ExitCommand extends ConsoleCommand {

        public void init() {
            name = "exit";
            desc = "Exit application";
        }

        public void exec(String... args) {
            System.exit(0);
        }

        public void print() {
            info_print("Exiting app");
        }
    };

    public class QuitCommand extends ExitCommand{

        public void init() {
            name = "quit";
            desc = "Exit application";
        }
    };

    public class CmdlistCommand extends ConsoleCommand {

        public void init() {
            name = "cmdlist";
            desc = "List registered commands";
        }

        public void exec(String... args) {
            print();
        }

        public void print() {
            registered_commands.forEach((c) -> {
                info_print(StringUtils.concat(c.name, " - ", c.desc));
            });
        }
    };

    public class ClearCommand extends ConsoleCommand {

        public void init() {
            name = "clear";
            desc = "Clear console";
        }

        public void exec(String... args) {
            //lines.clear();
        }

        public void print() {
        }
    };
}
