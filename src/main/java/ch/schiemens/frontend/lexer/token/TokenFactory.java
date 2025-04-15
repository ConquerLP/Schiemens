package ch.schiemens.frontend.lexer.token;

import ch.schiemens.frontend.lexer.TokenState;
import ch.schiemens.logger.frontend.compilationEngine.LexerLogger;
import ch.schiemens.util.PositionInFile;

public class TokenFactory {

    public static Token createToken(String value, TokenState state, LexerLogger logger, PositionInFile pos) {
        switch (state) {
            case INT: {
                return new Token(TokenType.L_INT, value, pos);
            }


            default: logger.logError("Unknown token state: " + state);
        }

        return null;
    }


}
