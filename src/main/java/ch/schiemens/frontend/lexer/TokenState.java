package ch.schiemens.frontend.lexer;

public enum TokenState {

    KEYWORD, IDENTIFIER, SYMBOL, WHITESPACE, COMMENT, EOF,
    STRING, CHAR,
    OTHER_NUMBER_SYSTEM, INT, DOUBLE, BOOLEAN, HEX, OCTAL, BINARY,
    START;

}
