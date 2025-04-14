package ch.schiemens.frontend.lexer;

import ch.schiemens.exception.LexicalException;
import ch.schiemens.frontend.lexer.token.Token;
import ch.schiemens.frontend.lexer.token.TokenType;
import ch.schiemens.logger.frontend.compilationEngine.LexerLogger;
import ch.schiemens.util.PositionInFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private final List<Token> tokens = new ArrayList<>();
    private BufferedReader reader;
    private final LexerLogger logger;

    public Lexer(Path path, LexerLogger logger) throws IOException {
        this.reader = Files.newBufferedReader(path, StandardCharsets.US_ASCII);
        this.logger = logger;
    }
    
    public List<Token> lex() throws IOException, LexicalException {
        String valueToken = "";
        StringBuilder value = new StringBuilder();
        int lineNumber = 1;
        int columnNumber = 1;
        int currentChar = -1;
        do {
            currentChar = reader.read();


        } while (currentChar != -1);


        tokens.add(new Token(TokenType.EOF, "", new PositionInFile(lineNumber, columnNumber, "")));
        return tokens;
    }

}
