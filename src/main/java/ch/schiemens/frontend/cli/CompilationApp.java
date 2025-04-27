package ch.schiemens.frontend.cli;

import ch.schiemens.frontend.core.CompilationEngine;
import ch.schiemens.frontend.core.compilationOptions.CompilationOptions;
import ch.schiemens.frontend.lexer.Lexer;
import ch.schiemens.frontend.lexer.token.Token;
import ch.schiemens.frontend.lexer.token.TokenType;
import ch.schiemens.logger.BaseLogger;
import ch.schiemens.logger.frontend.CLILogger;

import java.io.IOException;
import java.nio.file.Path;

public class CompilationApp {

    public static void main(String[] args) {
        /*
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
        */

        Path sourcePath = Path.of("D:\\Schiemens\\src\\main\\java\\ch\\schiemens\\frontend\\cli\\test.txt");
        //LexerLogger logger = new LexerLogger(sourcePath);
        try {
            Lexer lexer = new Lexer(sourcePath, null);
            Token token = lexer.nextToken();
            while (token.getType() != TokenType.EOF) {
                System.out.println(token);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
