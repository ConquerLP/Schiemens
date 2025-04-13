package ch.schiemens.logger.frontend;

import ch.schiemens.logger.BaseLogger;

import java.nio.file.Path;

public class CLILogger extends BaseLogger {

    private static final CLILogger INSTANCE = new CLILogger();

    private CLILogger() {
        super(Path.of("cli"), "cli");
    }

    public static CLILogger getInstance() {
        return INSTANCE;
    }

    @Override
    public void logInfo(String message) {
        writeLine("[INFO] " + message);
    }

    @Override
    public void logWarning(String message) {
        writeLine("[WARN] " + message);
    }

    @Override
    public void logError(String message) {
        writeLine("[ERROR] " + message);
    }

    @Override
    public void logDebug(String message) {
        writeLine("[DEBUG] " + message);
    }

    @Override
    public void logFatal(String message) {
        writeLine("[FATAL] " + message);
    }

}
