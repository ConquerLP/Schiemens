package ch.schiemens.frontend.lexer;

public enum TokenState {

    KEYWORD, IDENTIFIER, SYMBOL, WHITESPACE, COMMENT, BIG_COMMENT, EOF,
    STRING, CHAR,
    NUMBER_SYSTEM, INT, DOUBLE, HEX, OCTAL, BINARY,
    START, ERROR, CREATE;

}
