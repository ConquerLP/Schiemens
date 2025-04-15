package ch.schiemens.logger.frontend.compilationEngine;

import ch.schiemens.logger.BaseLogger;

import java.nio.file.Path;

public class LexerLogger extends BaseLogger {

    private final String LEXER = "Lexer: ";
    
    public LexerLogger(Path sourcePath) {
        super(sourcePath, CompilationUnitLogger.Phase.LEXER.toString());
    }

    @Override
    public void logInfo(String message) {
        super.logInfo(LEXER + message);
    }

    @Override
    public void logWarning(String message) {
        super.logWarning(LEXER + message);
    }

    @Override
    public void logError(String message) {
        super.logError(LEXER + message);
    }

}
