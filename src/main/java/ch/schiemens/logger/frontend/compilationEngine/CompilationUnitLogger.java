package ch.schiemens.logger.frontend.compilationEngine;

import ch.schiemens.frontend.core.compilationOptions.CompilationOptions;
import ch.schiemens.logger.BaseLogger;

import java.nio.file.Path;

public class CompilationUnitLogger extends BaseLogger {

    private final ASTLogger astLogger;
    private final Preprocessorlogger preprocessorlogger;
    private final LexerLogger lexerLogger;
    private final ParseLogger parseLogger;
    private Phase currentPhase = Phase.PREPROCESSOR;
    private final CompilationOptions compilationOptions;

    public enum Phase {
        PREPROCESSOR("Preprocessing"),
        LEXER("Lexing"),
        PARSER("Parsing"),
        AST("AST Generation");

        private final String name;

        Phase(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public CompilationUnitLogger(Path sourcePath, CompilationOptions compilationOptions) {
        super(sourcePath, "CompilationUnit");
        this.compilationOptions = compilationOptions;
        astLogger = new ASTLogger(sourcePath);
        preprocessorlogger = new Preprocessorlogger(sourcePath);
        lexerLogger = new LexerLogger(sourcePath);
        parseLogger = new ParseLogger(sourcePath);
    }

    public void nextPhase() {
        switch (currentPhase) {
            case PREPROCESSOR:
                currentPhase = Phase.LEXER;
                break;
            case LEXER:
                currentPhase = Phase.PARSER;
                break;
            case PARSER:
                currentPhase = Phase.AST;
                break;
            default:
                break;
        }
    }

    @Override
    public void logInfo(String message) {
        switch (currentPhase) {
            case PREPROCESSOR:
                preprocessorlogger.logInfo(message);
                break;
            case LEXER:
                lexerLogger.logInfo(message);
                break;
            case PARSER:
                parseLogger.logInfo(message);
                break;
            case AST:
                astLogger.logInfo(message);
                break;
        }
    }

    @Override
    public void logWarning(String message) {
        switch (currentPhase) {
            case PREPROCESSOR:
                preprocessorlogger.logWarning(message);
                break;
            case LEXER:
                lexerLogger.logWarning(message);
                break;
            case PARSER:
                parseLogger.logWarning(message);
                break;
            case AST:
                astLogger.logWarning(message);
                break;
        }
    }

    @Override
    public void logError(String message) {
        switch (currentPhase) {
            case PREPROCESSOR:
                preprocessorlogger.logError(message);
                break;
            case LEXER:
                lexerLogger.logError(message);
                break;
            case PARSER:
                parseLogger.logError(message);
                break;
            case AST:
                astLogger.logError(message);
                break;
        }
    }

    @Override
    public void logDebug(String message) {
        switch (currentPhase) {
            case PREPROCESSOR:
                preprocessorlogger.logDebug(message);
                break;
            case LEXER:
                lexerLogger.logDebug(message);
                break;
            case PARSER:
                parseLogger.logDebug(message);
                break;
            case AST:
                astLogger.logDebug(message);
                break;
        }
    }

    @Override
    public void logFatal(String message) {
        switch (currentPhase) {
            case PREPROCESSOR:
                preprocessorlogger.logFatal(message);
                break;
            case LEXER:
                lexerLogger.logFatal(message);
                break;
            case PARSER:
                parseLogger.logFatal(message);
                break;
            case AST:
                astLogger.logFatal(message);
                break;
        }
    }

    @Override
    public void flush() {

    }

}
