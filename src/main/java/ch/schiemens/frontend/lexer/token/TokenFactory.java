package ch.schiemens.frontend.lexer.token;

import ch.schiemens.frontend.lexer.LexerBuffer;
import ch.schiemens.frontend.lexer.TokenState;
import ch.schiemens.logger.frontend.compilationEngine.LexerLogger;

public class TokenFactory {

    private final LexerLogger logger;
    private final LexerBuffer lexerBuffer;

    public TokenFactory(LexerLogger logger, LexerBuffer lexerBuffer) {
        this.logger = logger;
        this.lexerBuffer = lexerBuffer;
    }

    public Token createToken(String value, TokenState state) {
        switch (state) {
            case INT: {
                return new Token(TokenType.L_INT, value, lexerBuffer.makePositionInFile(value));
            }

            default: //logger.logError("Unknown token state: " + state);
        }

        return null;
    }


}
