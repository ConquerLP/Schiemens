package ch.schiemens.logger.frontend.util;

import ch.schiemens.util.PositionInFile;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogFormatUtil {

    private static final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String formatMessage(String level, String message) {
        return "[" + timeFormat.format(LocalDateTime.now()) + "] " + level.toUpperCase() + ": " + message;
    }

    public static String formatMessage(String level, Path file, PositionInFile pos, String message) {
        return String.format("[%s] %s: (%s:%s) %s",
                timeFormat.format(LocalDateTime.now()),
                level.toUpperCase(),
                file.getFileName(),
                pos,
                message
        );
    }

    public static String info(String msg) {
        return formatMessage("INFO", msg);
    }

    public static String warn(String msg) {
        return formatMessage("WARN", msg);
    }

    public static String error(String msg) {
        return formatMessage("ERROR", msg);
    }

    public static String debug(String msg) {
        return formatMessage("DEBUG", msg);
    }

    public static String fatal(String msg) {
        return formatMessage("FATAL", msg);
    }

}
