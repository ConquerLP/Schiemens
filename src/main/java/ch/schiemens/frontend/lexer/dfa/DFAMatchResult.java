package ch.schiemens.frontend.lexer.dfa;

import ch.schiemens.frontend.lexer.token.TokenType;

public class DFAMatchResult {

    private final TokenType tokenType;
    private final String value;

    public DFAMatchResult(TokenType tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getValue() {
        return value;
    }

}
