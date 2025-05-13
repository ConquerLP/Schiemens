package ch.schiemens.frontend.lexer.token;

import ch.schiemens.util.PositionInFile;

public class Token {

    private final TokenType type;
    private final String value;
    private final PositionInFile position;

    public Token(TokenType type, String value, PositionInFile position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }

    public Token(TokenType type, PositionInFile position) {
        this.type = type;
        this.value = "";
        this.position = position;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public PositionInFile getPosition() {
        return position;
    }

    public boolean is(TokenType expected) {
        return this.type == expected;
    }

    public boolean isAny(TokenType... types) {
        for (TokenType t : types) {
            if (this.type == t) return true;
        }
        return false;
    }

    public boolean isKeyword() {
        return type.name().startsWith("K_");
    }

    public boolean isLiteral() {
        return type.name().startsWith("L_");
    }

    public boolean isSymbol() {
        return type.name().startsWith("S_");
    }

    @Override
    public String toString() {
        return String.format("Token(%s, '%s', %s)", type, value, position);
    }

}
