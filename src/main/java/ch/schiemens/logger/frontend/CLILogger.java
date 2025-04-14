package ch.schiemens.logger.frontend;

import ch.schiemens.logger.BaseLogger;

import java.nio.file.Path;

public class CLILogger extends BaseLogger {

    private static final CLILogger INSTANCE = new CLILogger();
    private static final String CLI = "CLI";

    private CLILogger() {
        super(Path.of("cli"), "cli");
    }

    public static CLILogger getInstance() {
        return INSTANCE;
    }

    @Override
    public void logInfo(String message) {
        super.logInfo(CLI + message);
    }

    @Override
    public void logWarning(String message) {
        super.logWarning(CLI + message);
    }

    @Override
    public void logError(String message) {
        super.logError(CLI + message);
    }

}
