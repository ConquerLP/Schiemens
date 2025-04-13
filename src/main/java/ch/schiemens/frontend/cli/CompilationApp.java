package ch.schiemens.frontend.cli;

import ch.schiemens.frontend.core.CompilationEngine;
import ch.schiemens.frontend.core.compilationOptions.CompilationOptions;
import ch.schiemens.logger.frontend.CLILogger;

public class CompilationApp {

    public static void main(String[] args) {
        if (args.length == 0) {
            CompilationOptions.printHelp();
            return;
        }

        CLILogger cliLogger = CLILogger.getInstance();



        ArgumentParser.parseArguments(args, CompilationOptions.getInstance());
        CompilationEngine engine = new CompilationEngine();
        engine.setUp(CompilationOptions.getInstance());
        engine.compile();
    }

}
