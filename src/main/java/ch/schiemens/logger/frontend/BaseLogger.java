package ch.schiemens.logger.frontend;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public abstract class BaseLogger {

    private final Path sourcePath;
    private final Path logFilePath;
    private final BufferedWriter writer;

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

    private Path computeLogFilePath(String phase) {
        String base = sourcePath.getFileName().toString().replaceAll("\\.[^.]+$", "");
        return sourcePath.resolveSibling(base + "." + phase + ".log");
    }

    protected void writeLine(String line) {
        try {
            writer.write(line);
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

    public abstract void logInfo(String message);
    public abstract void logWarning(String message);
    public abstract void logError(String message);
    public abstract void logDebug(String message);
    public abstract void logFatal(String message);

}