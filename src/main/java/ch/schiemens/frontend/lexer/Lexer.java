package ch.schiemens.frontend.lexer;

import ch.schiemens.exception.LexicalException;
import ch.schiemens.frontend.lexer.token.Token;
import ch.schiemens.frontend.lexer.token.TokenFactory;
import ch.schiemens.frontend.lexer.token.TokenType;
import ch.schiemens.logger.frontend.compilationEngine.LexerLogger;

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

    private final LexerBuffer lexerBuffer;
    private final StringBuilder tokenValue = new StringBuilder();
    private final TokenFactory tokenFactory;
    private char currentChar;
    private char peekedChar;
    private TokenState currentTokenState;
    private TokenState oldTokenState;
    private boolean hasDot = false;
    private String tokenValueString;

    public Lexer(Path path, LexerLogger logger) throws IOException {
        this.reader = Files.newBufferedReader(path, StandardCharsets.US_ASCII);
        this.logger = logger;
        lexerBuffer = new LexerBuffer(reader);
        tokenFactory = new TokenFactory(logger, lexerBuffer);
    }

    private void initLexer() {
        tokenValue.delete(0, tokenValue.length());
        currentChar = '\0';
        peekedChar = '\0';
        currentTokenState = TokenState.START;
        oldTokenState = TokenState.START;
        tokenValueString = "";
        hasDot = false;
    }

    private void createToken() {
        tokenValueString = tokenValue.toString();
        tokens.add(tokenFactory.createToken(tokenValueString, oldTokenState));
        initLexer();
    }

    public List<Token> lex() throws IOException, LexicalException {
        initLexer();
        tokens.clear();
        while (!lexerBuffer.isEOF()) {
            switch (currentTokenState) {
                case START: {
                    if (LexerAtomics.isZero(currentChar)) {
                        currentTokenState = TokenState.NUMBER_SYSTEM;
                        tokenValue.append(currentChar);
                    } else if (LexerAtomics.isNaturalNumber(currentChar)) {
                        currentTokenState = TokenState.INT;
                        tokenValue.append(currentChar);
                    } else if (LexerAtomics.isSymbol(currentChar)) {

                    } else if (LexerAtomics.isIdentifierStart(currentChar)) {

                    } else if (LexerAtomics.isWhitespace(currentChar)) {

                    } else {
                        //logger.logError("Unexpected character: " + inputChar + new PositionInFile(lineNumber, columnNumber));
                    }
                }
                break;
                case NUMBER_SYSTEM: {
                    if (LexerAtomics.isDecimal(currentChar)) {
                        currentTokenState = TokenState.INT;
                        tokenValue.append(currentChar);
                    } else if (LexerAtomics.isHexStart(currentChar)) {
                        currentTokenState = TokenState.HEX;
                        tokenValue.append(currentChar);
                    } else if (LexerAtomics.isBinaryStart(currentChar)) {
                        currentTokenState = TokenState.BINARY;
                        tokenValue.append(currentChar);
                    } else {
                        currentTokenState = TokenState.CREATE;
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
                    if (LexerAtomics.isDot(currentChar) && hasDot) {
                        currentTokenState = TokenState.ERROR;
                        tokenValue.append(currentChar);
                    }
                }
                break;
                case INT: {
                    if (LexerAtomics.isDot(currentChar)) {
                        hasDot = true;
                        currentTokenState = TokenState.DOUBLE;
                        tokenValue.append(currentChar);
                    } else if (LexerAtomics.isDecimal(currentChar)) {
                        currentTokenState = TokenState.INT;
                        tokenValue.append(currentChar);
                    } else {
                        currentTokenState = TokenState.CREATE;
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
                case CREATE: {
                    createToken();
                }
                break;
            }
            currentChar = (char) lexerBuffer.consume();
        }
        tokens.add(new Token(TokenType.EOF, lexerBuffer.makePositionInFile()));
        return tokens;
    }


}
