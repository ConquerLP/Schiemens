package ch.schiemens.logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseLogger {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private final Path sourcePath;
    private final Path logFilePath;
    private final BufferedWriter writer;
    private int warningCount = 0;
    private int errorCount = 0;

    public enum LogLevel {
        INFO("[INFO]: "), WARNING("[WARNING]: "), ERROR("[ERROR]: ");
        private final String prefix;

        LogLevel(String prefix) {
            this.prefix = prefix;
        }

        @Override
        public String toString() {
            return prefix;
        }

    }

    public BaseLogger(Path sourcePath, String phase) {
        this.sourcePath = sourcePath;
        this.logFilePath = computeLogFilePath(phase);
        try {
            Files.createDirectories(logFilePath.getParent());
            this.writer = Files.newBufferedWriter(logFilePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize log file: " + logFilePath, e);
        }
    }

    public List<String> readLoggedLines(LogLevel filter) {
        List<String> lines = new ArrayList<>();
        if (!Files.exists(logFilePath)) return lines;
        try {
            for (String line : Files.readAllLines(logFilePath)) {
                if (filter == LogLevel.INFO || line.startsWith("[" + filter.name())) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read from log file: " + logFilePath, e);
        }
        return lines;
    }

    private Path computeLogFilePath(String phase) {
        String base = sourcePath.getFileName().toString().replaceAll("\\.[^.]+$", "");
        return sourcePath.resolveSibling(base + "." + phase + ".log");
    }

    private void writeLine(String line) {
        String timestamp = LocalDateTime.now().format(DATE_FORMAT);
        try {
            writer.write("[" + timestamp + "] " + line);
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to log file: " + logFilePath, e);
        }
    }

    protected void flush() {
        try {
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to flush log file: " + logFilePath, e);
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close log file: " + logFilePath, e);
        }
    }

    public void logInfo(String message) {
        writeLine(LogLevel.INFO + message);
    }

    public void logWarning(String message) {
        writeLine(LogLevel.WARNING + message);
        warningCount++;
    }

    public void logError(String message) {
        writeLine(LogLevel.ERROR + message);
        errorCount++;
    }

    public boolean hasErrors() {
        return errorCount > 0;
    }

    public boolean hasWarnings() {
        return warningCount > 0;
    }

}