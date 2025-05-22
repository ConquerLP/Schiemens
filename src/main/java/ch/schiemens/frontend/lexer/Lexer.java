package ch.schiemens.frontend.lexer;

import ch.schiemens.exception.LexicalException;
import ch.schiemens.frontend.lexer.token.Token;
import ch.schiemens.frontend.lexer.token.TokenFactory;
import ch.schiemens.logger.frontend.compilationEngine.LexerLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static ch.schiemens.frontend.lexer.LexerAtomics.*;

public class Lexer {

    private final LexerLogger logger;
    private final LexerBuffer lexerBuffer;
    private final StringBuilder tokenValue = new StringBuilder();
    private final TokenFactory tokenFactory;
    private char currentChar;
    private int currentInt;
    private TokenState currentTokenState;
    private TokenState oldTokenState;
    private String tokenValueString;

    public Lexer(Path path, LexerLogger logger) throws IOException {
        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.US_ASCII);
        this.logger = logger;
        lexerBuffer = new LexerBuffer(reader);
        tokenFactory = new TokenFactory(logger, lexerBuffer);
    }

    private void initTokenState() {
        tokenValue.delete(0, tokenValue.length());
        currentTokenState = TokenState.START;
        oldTokenState = TokenState.START;
        currentChar = '\0';
        currentInt = 0;
        tokenValueString = "";
    }

    private Token createTokenBacktrack() {
        lexerBuffer.goBack();
        tokenValue.deleteCharAt(tokenValue.length() - 1);
        return createToken();
    }

    private Token createToken() {
        Token token;
        tokenValueString = tokenValue.toString();
        token = tokenFactory.createToken(tokenValueString, oldTokenState);
        initTokenState();
        return token;
    }

    private void setState(TokenState state) {
        oldTokenState = currentTokenState;
        currentTokenState = state;
        tokenValue.append(currentChar);
    }

    private void setState(TokenState state, TokenState oldTokenState) {
        this.oldTokenState = oldTokenState;
        currentTokenState = state;
        tokenValue.append(currentChar);
    }

    private void handleInvalidEOFInState(TokenState state) {
        logger.logError("Unexpected EOF in state: " + state);
        setState(TokenState.EOF);
    }

    private boolean checkEOF(TokenState state, int currentInt) {
        if (LexerBuffer.isEOF(currentInt)) {
            handleInvalidEOFInState(state);
            return true;
        } else return false;
    }

    public Token nextToken() throws IOException, LexicalException {
        initTokenState();
        Token token = null;
        do {
            if(currentTokenState != TokenState.CREATE && currentTokenState != TokenState.CREATE_B) {
                currentInt = lexerBuffer.consume();
                currentChar = (char) currentInt;
            }
            switch (currentTokenState) {
                case START: {
                    if (LexerBuffer.isEOF(currentInt)) setState(TokenState.EOF);
                    else if (isWhitespace(currentChar)) {
                        setState(TokenState.START);
                        tokenValue.delete(0, tokenValue.length());
                    }
                    else if (currentChar == '0') setState(TokenState.NUM_INT);
                    else if (isDecimal(currentChar)) setState(TokenState.INT);
                    else if (isIdentifierStart(currentChar)) setState(TokenState.IDENTIFIER);
                        //single character symbols
                    else if (currentChar == '(') setState(TokenState.S_PAREN_START);
                    else if (currentChar == ')') setState(TokenState.S_PAREN_END);
                    else if (currentChar == '[') setState(TokenState.S_ARRAY_START);
                    else if (currentChar == ']') setState(TokenState.S_ARRAY_END);
                    else if (currentChar == '{') setState(TokenState.S_CURLY_BRACKET_START);
                    else if (currentChar == '}') setState(TokenState.S_CURLY_BRACKET_END);
                    else if (currentChar == '.') setState(TokenState.S_DOT);
                    else if (currentChar == ';') setState(TokenState.S_SEMI);
                    else if (currentChar == ',') setState(TokenState.S_COMMA);
                        //multiple character symbols
                    else if (currentChar == '+') setState(TokenState.S_PLUS);
                    else if (currentChar == '-') setState(TokenState.S_MINUS);
                    else if (currentChar == '*') setState(TokenState.S_MUL);
                    else if (currentChar == '/') setState(TokenState.S_DIV);
                    else if (currentChar == '%') setState(TokenState.S_MOD);
                    else if (currentChar == '^') setState(TokenState.S_EXPO);
                    else if (currentChar == '<') setState(TokenState.S_LT);
                    else if (currentChar == '>') setState(TokenState.S_GT);
                    else if (currentChar == '!') setState(TokenState.S_NOT);
                    else if (currentChar == '|') setState(TokenState.S_OR_S);
                    else if (currentChar == '&') setState(TokenState.S_AND_S);
                    else if (currentChar == '=') setState(TokenState.S_EQ);
                        //char and string
                    else if (currentChar == '\'') setState(TokenState.CHAR_S);
                    else if (currentChar == '\"') setState(TokenState.STRING_S);
                    else setState(TokenState.ERROR);
                }
                break;
                case ERROR: {
                    logger.logError("Invalid token: " + tokenValue);
                    setState(TokenState.START);
                }
                break;
                case CREATE: {
                    token = createToken();
                }
                break;
                case CREATE_B: {
                    token = createTokenBacktrack();
                }
                break;
                // numbers
                case NUM_INT: {
                    if (currentChar == 'o') setState(TokenState.OCTAL_S);
                    else if (currentChar == 'x') setState(TokenState.HEX_S);
                    else if (currentChar == 'b') setState(TokenState.BINARY_S);
                    else if (isDecimal(currentChar)) setState(TokenState.INT);
                    else if (currentChar == '.') setState(TokenState.DOUBLE_S);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case OCTAL_S: {
                    if (isOctal(currentChar)) setState(TokenState.OCTAL_E);
                    else setState(TokenState.ERROR);
                }
                break;
                case OCTAL_E: {
                    if (isOctal(currentChar)) setState(TokenState.OCTAL_E);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case HEX_S: {
                    if (isHexadecimal(currentChar)) setState(TokenState.HEX_E);
                    else setState(TokenState.ERROR);
                }
                break;
                case HEX_E: {
                    if (isHexadecimal(currentChar)) setState(TokenState.HEX_E);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case BINARY_S: {
                    if (isBinary(currentChar)) setState(TokenState.BINARY_E);
                    else setState(TokenState.ERROR);
                }
                break;
                case BINARY_E: {
                    if (isBinary(currentChar)) setState(TokenState.BINARY_E);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case INT: {
                    if (isDecimal(currentChar)) setState(TokenState.INT);
                    else if (currentChar == '.') setState(TokenState.DOUBLE_S);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case DOUBLE_S: {
                    if (isDecimal(currentChar)) setState(TokenState.DOUBLE_E);
                    else setState(TokenState.ERROR);
                }
                break;
                case DOUBLE_E: {
                    if (isDecimal(currentChar)) setState(TokenState.DOUBLE_E);
                    else setState(TokenState.CREATE_B);
                }
                break;
                //single character symbols
                case S_PAREN_START: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_PAREN_END: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_ARRAY_START: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_ARRAY_END: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_CURLY_BRACKET_START: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_CURLY_BRACKET_END: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_DOT: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_SEMI: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_COMMA: {
                    setState(TokenState.CREATE);
                }
                break;
                //multiple character symbols
                case S_PLUS: {
                    if (currentChar == '+') setState(TokenState.S_PLUS_PLUS);
                    else if (currentChar == '=') setState(TokenState.S_PLUS_EQ);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_MINUS: {
                    if (currentChar == '-') setState(TokenState.S_MINUS_MINUS);
                    else if (currentChar == '=') setState(TokenState.S_MINUS_EQ);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_MUL: {
                    if (currentChar == '*') setState(TokenState.S_EXPO_MUL);
                    else if (currentChar == '=') setState(TokenState.S_MUL_EQ);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_DIV: {
                    if (currentChar == '=') setState(TokenState.S_DIV_EQ);
                    else if (currentChar == '/') setState(TokenState.S_INLINE_COMMENT);
                    else if (currentChar == '*') setState(TokenState.MULTI_LINE_COMMENT_S);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_MOD: {
                    if (currentChar == '=') setState(TokenState.S_MOD_EQ);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_EXPO: {
                    if (currentChar == '=') setState(TokenState.S_EXPO_EQ);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_EXPO_MUL: {
                    if (currentChar == '=') setState(TokenState.S_EXPO_MUL_EQ);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_EXPO_MUL_EQ: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_GT: {
                    if (currentChar == '=') setState(TokenState.S_GT_EQ);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_GT_EQ: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_LT: {
                    if (currentChar == '=') setState(TokenState.S_LT_EQ);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_LT_EQ: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_NOT: {
                    if (currentChar == '=') setState(TokenState.S_NOT_EQ);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_NOT_EQ: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_OR_S: {
                    if (currentChar == '|') setState(TokenState.S_OR_E);
                    else setState(TokenState.ERROR);
                }
                break;
                case S_OR_E: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_AND_S: {
                    if (currentChar == '&') setState(TokenState.S_AND_S);
                    else setState(TokenState.ERROR);
                }
                break;
                case S_AND_E: {
                    setState(TokenState.CREATE);
                }
                break;
                case S_EQ: {
                    if (currentChar == '=') setState(TokenState.S_EQ_EQ);
                    else setState(TokenState.CREATE_B);
                }
                break;
                case S_EQ_EQ: {
                    setState(TokenState.CREATE);
                }
                break;
                //char
                case CHAR_S: {
                    if (checkEOF(TokenState.CHAR_S, currentInt)) break;
                    else if (currentChar == '\\') setState(TokenState.CHAR_MULTI_S);
                    else if (isValidChar(currentChar)) setState(TokenState.CHAR_SINGLE);
                    else setState(TokenState.ERROR);
                }
                break;
                case CHAR_SINGLE: {
                    if (checkEOF(TokenState.CHAR_SINGLE, currentInt)) break;
                    else if (currentChar == '\'') setState(TokenState.CREATE, TokenState.CHAR_E);
                    else setState(TokenState.ERROR);
                }
                break;
                case CHAR_MULTI_S: {
                    if (checkEOF(TokenState.CHAR_MULTI_S, currentInt)) break;
                    else if (isValidChar(currentChar)) setState(TokenState.CREATE, TokenState.CHAR_E);
                    else setState(TokenState.ERROR);
                }
                break;
                case CHAR_MULTI_E: {
                    if (checkEOF(TokenState.CHAR_MULTI_E, currentInt)) break;
                    else if (currentChar == '\'') setState(TokenState.CREATE, TokenState.CHAR_E);
                    else setState(TokenState.ERROR);
                }
                break;
                //string
                case STRING_S: {
                    if (checkEOF(TokenState.STRING_S, currentInt)) break;
                    else if (isCarriageReturn(currentChar)) setState(TokenState.ERROR);
                    else if (currentChar == '"') setState(TokenState.CREATE, TokenState.STRING_E);
                    else if (currentChar == '\\') setState(TokenState.STRING_MULTI_S);
                    else if (isValidChar(currentChar)) setState(TokenState.STRING_SINGLE);
                    else setState(TokenState.ERROR);
                }
                break;
                case STRING_SINGLE: {
                    if (checkEOF(TokenState.STRING_SINGLE, currentInt)) break;
                    else if (isCarriageReturn(currentChar)) setState(TokenState.ERROR);
                    else if (currentChar == '"') setState(TokenState.CREATE, TokenState.STRING_E);
                    else if (currentChar == '\\') setState(TokenState.STRING_MULTI_S);
                    else if (isValidChar(currentChar)) setState(TokenState.STRING_SINGLE);
                    else setState(TokenState.ERROR);
                }
                break;
                case STRING_MULTI_S: {
                    if (checkEOF(TokenState.STRING_MULTI_S, currentInt)) break;
                    else if (isCarriageReturn(currentChar)) setState(TokenState.ERROR);
                    else if (isValidChar(currentChar)) setState(TokenState.STRING_MULTI_E);
                    else setState(TokenState.ERROR);
                }
                break;
                case STRING_MULTI_E: {
                    if (checkEOF(TokenState.STRING_MULTI_E, currentInt)) break;
                    else if (isCarriageReturn(currentChar)) setState(TokenState.ERROR);
                    else if (currentChar == '"') setState(TokenState.CREATE, TokenState.STRING_E);
                    else if (currentChar == '\\') setState(TokenState.STRING_MULTI_S);
                    else if (isValidChar(currentChar)) setState(TokenState.STRING_SINGLE);
                    else setState(TokenState.ERROR);
                }
                break;
                //identifier and keywords
                case IDENTIFIER: {
                    if (isAlphaNum(currentChar)) setState(TokenState.IDENTIFIER);
                    else setState(TokenState.CREATE_B);
                }
                break;
                //inline comment
                case S_INLINE_COMMENT: {
                    if (isCarriageReturn(currentChar) || LexerBuffer.isEOF(currentInt)) setState(TokenState.CREATE_B);
                    else setState(TokenState.S_INLINE_COMMENT);
                }
                break;
                //multiline comment
                case MULTI_LINE_COMMENT_S: {
                    if (checkEOF(TokenState.MULTI_LINE_COMMENT_S, currentInt)) break;
                    else if (currentChar == '*') setState(TokenState.MULTI_LINE_COMMENT_S1);
                    else setState(TokenState.MULTI_LINE_COMMENT_S);
                }
                break;
                case MULTI_LINE_COMMENT_S1: {
                    if (checkEOF(TokenState.MULTI_LINE_COMMENT_S1, currentInt)) break;
                    else if (currentChar == '/') setState(TokenState.MULTI_LINE_COMMENT_E);
                    else setState(TokenState.MULTI_LINE_COMMENT_S);
                }
                break;
                case MULTI_LINE_COMMENT_E: {
                    setState(TokenState.CREATE);
                }
                break;
                case EOF: {
                    setState(TokenState.CREATE);
                }
                break;
            }

        } while(token == null);
        return token;
    }

}
