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

import static ch.schiemens.frontend.lexer.LexerAtomics.*;

public class Lexer {

    private final List<Token> tokens = new ArrayList<>();
    private final BufferedReader reader;
    private final LexerLogger logger;

    public Lexer(Path path, LexerLogger logger) throws IOException {
        this.reader = Files.newBufferedReader(path, StandardCharsets.US_ASCII);
        this.logger = logger;
    }

    public List<Token> lex() throws IOException, LexicalException {
        StringBuilder value = new StringBuilder();
        int lineNumber = 1;
        int columnNumber = 1;
        int currentChar = -1;
        TokenState tokenState = TokenState.START;
        TokenState oldState = TokenState.START;
        do {
            currentChar = reader.read();
            switch (tokenState) {
                case START: {
                    if(isDigit(currentChar)) {
                        if(currentChar == '0') {
                            tokenState = TokenState.OTHER_NUMBER_SYSTEM;
                        } else {
                            tokenState = TokenState.INT;
                        }
                    }
                }
                case OTHER_NUMBER_SYSTEM: {

                };
                case INT: {
                    if(isDigit(currentChar)) {
                        value.append(currentChar);
                    } else if(currentChar == '.') {
                        tokenState = TokenState.DOUBLE;
                        value.append(currentChar);
                    } else {
                        switch (tokenState) {
                            case DOUBLE: {
                                tokens.add(new Token(TokenType.L_DOUBLE, value.toString(), new PositionInFile(lineNumber, columnNumber)));
                            } break;
                            case INT: {
                                tokens.add(new Token(TokenType.L_INT, value.toString(), new PositionInFile(lineNumber, columnNumber)));
                            } break;
                            default: {
                                if(currentChar == '.') {

                                }
                            } break;
                        }
                    }
                }

            }


            oldState = tokenState;
        } while (currentChar != -1);
        tokens.add(new Token(TokenType.EOF, new PositionInFile(lineNumber, columnNumber)));
        return tokens;
    }

}
