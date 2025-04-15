package ch.schiemens.frontend.lexer;

import ch.schiemens.exception.LexicalException;
import ch.schiemens.frontend.lexer.dfa.DFAMatchResult;
import ch.schiemens.frontend.lexer.dfa.DFAMatcher;
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
    private final BufferedReader reader;
    private final LexerLogger logger;

    public Lexer(Path path, LexerLogger logger) throws IOException {
        this.reader = Files.newBufferedReader(path, StandardCharsets.US_ASCII);
        this.logger = logger;
    }

    public List<Token> lex(DFAMatcher matcher) throws IOException, LexicalException {
        LexerBuffer buffer = new LexerBuffer(reader);
        while (!buffer.isEOF()) {
            if (Character.isWhitespace((char) buffer.peek())) {
                buffer.advance();
                continue;
            }
            DFAMatchResult result = matcher.matchStream(buffer);
            if (result == null) {
                //logger.logError("DFA matcher failed.");
                System.out.println("DFA matcher failed.");
            } else {
                Token token = new Token(result.getTokenType(), result.getValue(),
                        new PositionInFile(buffer.getLine(), buffer.getColumn() - result.getValue().length(), buffer.getColumn()));
                tokens.add(token);
            }

            //logger.logInfo("New Token: " + token);
        }
        tokens.add(new Token(TokenType.EOF, new PositionInFile(buffer.getLine(), buffer.getColumn(), buffer.getColumn())));
        return tokens;
    }

}
