package ch.codegen.util;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerCli {

    private static final PrintStream out = System.err;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        out.printf("[%s] [%s] %s%n", timestamp, level, message);
    }

    public static void info(String message) {
        log("INFO", message);
    }

    public static void warn(String message) {
        log("WARN", message);
    }

    public static void error(String message) {
        log("ERROR", message);
    }

    public static void debug(String message) {
        if (System.getenv("DEBUG") != null) {
            log("DEBUG", message);
        }
    }

}
