package ch.schiemens.frontend.lexer;

import ch.schiemens.exception.LexicalException;
import ch.schiemens.frontend.lexer.token.Token;
import ch.schiemens.frontend.lexer.token.TokenFactory;
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

    public List<Token> lex() throws IOException, LexicalException {
        StringBuilder value = new StringBuilder();
        int lineNumber = 1;
        int columnNumber = 1;
        int currentChar = -1;
        char inputChar = '\0';
        TokenState tokenState = TokenState.START;
        TokenState oldTokenState = TokenState.START;
        boolean hasDot = false;
        String tokenValue = "";
        do {
            currentChar = reader.read();
            inputChar = (char) currentChar;
            switch (tokenState) {
                case START: {
                    hasDot = false;
                    if (LexerAtomics.isZero(inputChar)) {
                        tokenState = TokenState.NUMBER_SYSTEM;
                        value.append(inputChar);
                    } else if (LexerAtomics.isNaturalNumber(inputChar)) {
                        tokenState = TokenState.INT;
                        value.append(inputChar);
                    } else if (LexerAtomics.isSymbol(inputChar)) {

                    } else if (LexerAtomics.isIdentifierStart(inputChar)) {

                    } else if (LexerAtomics.isWhitespace(inputChar)) {

                    } else {
                        //logger.logError("Unexpected character: " + inputChar + new PositionInFile(lineNumber, columnNumber));
                    }
                }
                break;
                case NUMBER_SYSTEM: {
                    if (LexerAtomics.isDecimal(inputChar)) {
                        tokenState = TokenState.INT;
                        value.append(inputChar);
                    } else if (LexerAtomics.isHexStart(inputChar)) {
                        tokenState = TokenState.HEX;
                        value.append(inputChar);
                    } else if (LexerAtomics.isBinaryStart(inputChar)) {
                        tokenState = TokenState.BINARY;
                        value.append(inputChar);
                    } else {
                        tokenState = TokenState.CREATE;
                    }
                }
                break;
                case KEYWORD: {
                }
                break;
                case IDENTIFIER: {
                }
                break;
                case SYMBOL: {
                }
                break;
                case WHITESPACE: {
                }
                break;
                case COMMENT: {
                }
                break;
                case BIG_COMMENT: {
                }
                break;
                case EOF: {
                }
                break;
                case STRING: {
                }
                break;
                case CHAR: {
                }
                break;
                case DOUBLE: {
                    if (LexerAtomics.isDot(inputChar) && hasDot) {
                        tokenState = TokenState.ERROR;
                        value.append(inputChar);
                    }
                }
                break;
                case INT: {
                    if (LexerAtomics.isDot(inputChar)) {
                        hasDot = true;
                        tokenState = TokenState.DOUBLE;
                        value.append(inputChar);
                    } else if (LexerAtomics.isDecimal(inputChar)) {
                        tokenState = TokenState.INT;
                        value.append(inputChar);
                    } else {
                        tokenState = TokenState.CREATE;
                    }
                }
                break;
                case BINARY: {
                }
                break;
                case HEX: {
                }
                break;
                case OCTAL: {
                }
                break;
                case ERROR: {
                    //logger.logError("Lexical error: " + inputChar + new PositionInFile(lineNumber, columnNumber, value.toString()));
                }
                break;
            }
            if(tokenState == TokenState.CREATE) {
                tokenValue = value.toString();
                tokens.add(TokenFactory.createToken(tokenValue, oldTokenState, logger, new PositionInFile(lineNumber, columnNumber, tokenValue)));
                tokenState = TokenState.START;
            }
            oldTokenState = tokenState;
        } while (currentChar != -1);
        tokens.add(new Token(TokenType.EOF, new PositionInFile(lineNumber, columnNumber)));
        return tokens;
    }

}
