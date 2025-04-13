package ch.schiemens.frontend.cli;

import ch.schiemens.frontend.core.CompilationEngine;
import ch.schiemens.frontend.core.compilationOptions.CompilationOptions;
import ch.schiemens.logger.BaseLogger;
import ch.schiemens.logger.frontend.CLILogger;

public class CompilationApp {

    public static void main(String[] args) {
        CompilationOptions compilationOptions = CompilationOptions.getInstance();
        CLILogger logger = CLILogger.getInstance();
        ArgumentParser.parseArguments(args, logger, compilationOptions);
        if(logger.hasErrors()) {
            System.err.println(logger.readLoggedLines(BaseLogger.LogLevel.ERROR));
            compilationOptions.printHelp();
        } else {
            CompilationEngine compilationEngine = CompilationEngine.getInstance();
            compilationEngine.setCompilationOptions(compilationOptions);
            compilationEngine.compile();
        }
    }

}
