package ch.schiemens.logger.frontend.compilationEngine;

import ch.schiemens.logger.BaseLogger;

import java.nio.file.Path;

public class Preprocessorlogger extends BaseLogger {

    public Preprocessorlogger(Path sourcePath) {
        super(sourcePath, CompilationUnitLogger.Phase.PREPROCESSOR.toString());
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

}
