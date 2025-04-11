package ch.codegen.cli;

import java.util.*;

public class CompilationApp {

    private static final String FORMAT = "\t%-15s %-25s %s%n";
    private static final String VERSION = "1.0.69";

    public static void main(String[] args) {
        if (args.length == 0) {
            printHelp();
            return;
        }

        Map<String, String> options = parseArgs(args);
        if (options.containsKey("-help")) {
            printHelp();
            return;
        }
        if (options.containsKey("-version")) {
            System.out.println("Version: " + VERSION);
            return;
        }
        CompileOptions.parseArguments(options);




    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            String current = args[i];
            if (current.startsWith("-")) {
                String value = (i + 1 < args.length && !args[i + 1].startsWith("-")) ? args[i + 1] : "";
                map.put(current, value);
                if (!value.isEmpty()) i++;
            }
        }
        return map;
    }

    private static void printHelp() {
        System.out.println("Schiemens options:");
        printOption("-help", "", "Shows helping screen");
        printOption("-i", "<source-file(s)>", "Specify the input source file(s)");
        printOption("-o", "<output-file>", "Specify the output file");
        printOption("-version", "", "Shows the current version");
        printOption("-time", "", "Shows compilation time");
        printOption("-ast", "", "Dumps the AST into the specified outputfile");
        printOption("-log", "", "Dumps all loggings into the specified outputfile");
        printOption("-verify", "", "Verifies the program, just syntax checking");
        printOption("-target", "<target-name>", "Specify the target to compile to (default: sm)");
    }

    private static void printOption(String flag, String param, String description) {
        System.out.printf(FORMAT, flag, param, description);
    }

}
