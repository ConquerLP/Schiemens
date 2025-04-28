package ch.schiemens.frontend.lexer.token;

import ch.schiemens.frontend.lexer.LexerBuffer;
import ch.schiemens.frontend.lexer.TokenState;
import ch.schiemens.logger.frontend.compilationEngine.LexerLogger;

import java.util.HashMap;
import java.util.Map;

public class TokenFactory {

    private static final Map<String, TokenType> keywords = new HashMap<>() {
        {
            put(TokenType.K_CLASS.toString(), TokenType.K_CLASS);
            put(TokenType.K_VOID.toString(), TokenType.K_VOID);
            put(TokenType.K_FUNCTION.toString(), TokenType.K_FUNCTION);
            put(TokenType.K_GLOBAL.toString(), TokenType.K_GLOBAL);
            put(TokenType.K_FINAL.toString(), TokenType.K_FINAL);
            put(TokenType.K_STATIC.toString(), TokenType.K_STATIC);
            put(TokenType.K_IF.toString(), TokenType.K_IF);
            put(TokenType.K_ELSE.toString(), TokenType.K_ELSE);
            put(TokenType.K_WHILE.toString(), TokenType.K_WHILE);
            put(TokenType.K_DO.toString(), TokenType.K_DO);
            put(TokenType.K_FOR.toString(), TokenType.K_FOR);
            put(TokenType.K_CONTINUE.toString(), TokenType.K_CONTINUE);
            put(TokenType.K_BREAK.toString(), TokenType.K_BREAK);
            put(TokenType.K_RETURN.toString(), TokenType.K_RETURN);
            put(TokenType.K_GOTO.toString(), TokenType.K_GOTO);
            put(TokenType.K_SWITCH.toString(), TokenType.K_SWITCH);
            put(TokenType.K_CASE.toString(), TokenType.K_CASE);
            put(TokenType.K_DEFAULT.toString(), TokenType.K_DEFAULT);
            put(TokenType.K_LABEL.toString(), TokenType.K_LABEL);
            put(TokenType.K_TRUE.toString(), TokenType.K_TRUE);
            put(TokenType.K_FALSE.toString(), TokenType.K_FALSE);
            put(TokenType.K_NULL.toString(), TokenType.K_NULL);
            put(TokenType.K_NEW.toString(), TokenType.K_NEW);
            put(TokenType.K_THIS.toString(), TokenType.K_THIS);
            put(TokenType.K_NOT.toString(), TokenType.K_NOT);
            put(TokenType.K_AND.toString(), TokenType.K_AND);
            put(TokenType.K_OR.toString(), TokenType.K_OR);
            put(TokenType.K_DOUBLE.toString(), TokenType.K_DOUBLE);
            put(TokenType.K_INT.toString(), TokenType.K_INT);
            put(TokenType.K_STRING.toString(), TokenType.K_STRING);
            put(TokenType.K_BOOLEAN.toString(), TokenType.K_BOOLEAN);
            put(TokenType.K_CHAR.toString(), TokenType.K_CHAR);
        }
    };

    private final LexerLogger logger;
    private final LexerBuffer lexerBuffer;

    public TokenFactory(LexerLogger logger, LexerBuffer lexerBuffer) {
        this.logger = logger;
        this.lexerBuffer = lexerBuffer;
    }

    public Token createToken(String value, TokenState state) {
        return switch (state) {
            case INT -> makeToken(TokenType.L_INT, value);
            case DOUBLE_E -> makeToken(TokenType.L_DOUBLE, value);
            case OCTAL_E -> makeToken(TokenType.L_OCTAL, value);
            case HEX_E -> makeToken(TokenType.L_HEX, value);
            case BINARY_E -> makeToken(TokenType.L_BINARY, value);
            case S_PAREN_START -> makeToken(TokenType.S_LPAREN, value);
            case S_PAREN_END -> makeToken(TokenType.S_RPAREN, value);
            case S_ARRAY_START -> makeToken(TokenType.S_LBRACKET, value);
            case S_ARRAY_END -> makeToken(TokenType.S_RBRACKET, value);
            case S_CURLY_BRACKET_START -> makeToken(TokenType.S_LBRACE, value);
            case S_CURLY_BRACKET_END -> makeToken(TokenType.S_RBRACE, value);
            case S_DOT -> makeToken(TokenType.S_DOT, value);
            case S_SEMI -> makeToken(TokenType.S_SEMICOLON, value);
            case S_COMMA -> makeToken(TokenType.S_COMMA, value);
            case S_PLUS -> makeToken(TokenType.S_PLUS, value);
            case S_PLUS_PLUS -> makeToken(TokenType.S_INC, value);
            case S_PLUS_EQ -> makeToken(TokenType.S_ADD_ASSIGN, value);
            case S_MINUS -> makeToken(TokenType.S_MINUS, value);
            case S_MINUS_MINUS -> makeToken(TokenType.S_DEC, value);
            case S_MINUS_EQ -> makeToken(TokenType.S_SUB_ASSIGN, value);
            case S_MUL -> makeToken(TokenType.S_MULT, value);
            case S_MUL_EQ -> makeToken(TokenType.S_MULT_ASSIGN, value);
            case S_EXPO_MUL -> makeToken(TokenType.S_EXP_ALT, value);
            case S_EXPO_MUL_EQ -> makeToken(TokenType.S_EXP_ALT_ASSIGN, value);
            case S_DIV -> makeToken(TokenType.S_DIV, value);
            case S_DIV_EQ -> makeToken(TokenType.S_DIV_ASSIGN, value);
            case S_MOD -> makeToken(TokenType.S_MOD, value);
            case S_MOD_EQ -> makeToken(TokenType.S_MOD_ASSIGN, value);
            case S_EXPO -> makeToken(TokenType.S_EXP, value);
            case S_EXPO_EQ -> makeToken(TokenType.S_EXP_ASSIGN, value);
            case S_GT -> makeToken(TokenType.S_GT, value);
            case S_GT_EQ -> makeToken(TokenType.S_GEQ, value);
            case S_LT -> makeToken(TokenType.S_LT, value);
            case S_LT_EQ -> makeToken(TokenType.S_LEQ, value);
            case S_NOT -> makeToken(TokenType.K_NOT_ALT, value);
            case S_NOT_EQ -> makeToken(TokenType.S_NEQ, value);
            case S_OR_E -> makeToken(TokenType.K_OR_ALT, value);
            case S_AND_E -> makeToken(TokenType.K_AND_ALT, value);
            case S_EQ -> makeToken(TokenType.S_ASSIGN, value);
            case S_EQ_EQ -> makeToken(TokenType.S_EQ, value);
            case CHAR_E -> makeToken(TokenType.L_CHAR, value);
            case STRING_E -> makeToken(TokenType.L_STRING, value);
            case S_INLINE_COMMENT -> makeToken(TokenType.INLINE_COMMENT, value);
            case MULTI_LINE_COMMENT_E -> makeToken(TokenType.MULTI_LINE_COMMENT, value);
            case IDENTIFIER -> makeToken(keywords.getOrDefault(value, TokenType.IDENTIFIER), value);
            case EOF -> makeToken(TokenType.EOF, value);
            default -> makeToken(TokenType.INVALID, value);
        };
    }

    private Token makeToken(TokenType type, String value) {
        Token token = new Token(type, value, lexerBuffer.makePositionInFile(value));
        if(type == TokenType.INVALID) {
            logger.logError("Created an invalid token: " + token);
        } else {
            logger.logInfo("Created Token: " + token);
        }
        return token;
    }

}
