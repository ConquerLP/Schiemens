package ch.schiemens.logger.frontend.compilationEngine;

import ch.schiemens.logger.frontend.BaseLogger;

import java.nio.file.Path;

public class LexerLogger extends BaseLogger {

    public LexerLogger(Path sourcePath) {
        super(sourcePath, CompilationUnitLogger.Phase.LEXER.toString());
    }

    @Override
    public void logInfo(String message) {

    }

    @Override
    public void logWarning(String message) {

    }

    @Override
    public void logError(String message) {

    }

    @Override
    public void logDebug(String message) {

    }

    @Override
    public void logFatal(String message) {

    }

}
