package ch.schiemens.frontend.lexer.token;

public enum TokenType {

    IDENTIFIER("identifier"), EOF("EOF"),
    L_STRING("literal string"), L_CHAR("literal char"), L_INT("literal int"), L_DOUBLE("literal double"),
    L_BOOLEAN("literal boolean"), L_HEX("literal hex"), L_OCTAL("literal octal"), L_BINARY("literal binary"),

    K_CLASS("class"), K_VOID("void"), K_FUNCTION("func"), K_GLOBAL("global"),
    K_FINAL("final"), K_STATIC("static"),
    K_IF("if"), K_ELSE("else"), K_WHILE("while"), K_DO("do"), K_FOR("for"),
    K_CONTINUE("continue"), K_BREAK("break"), K_RETURN("return"), K_GOTO("goto"),
    K_SWITCH("switch"), K_CASE("case"), K_DEFAULT("default"), K_LABEL("label"),
    K_TRUE("true"), K_FALSE("false"), K_NULL("null"), K_NEW("new"), K_THIS("this"),
    K_NOT("not"), K_AND("and"), K_OR("or"), K_NOT_ALT("!"), K_AND_ALT("&&"), K_OR_ALT("||"),
    K_DOUBLE("double"), K_INT("int"), K_STRING("string"), K_BOOLEAN("boolean"), K_CHAR("char"),

    S_EQ("=="), S_NEQ("!="), S_LEQ("<="), S_GEQ(">="), S_LT("<"), S_GT(">"),
    S_PLUS("+"), S_MINUS("-"), S_MULT("*"), S_DIV("/"), S_MOD("%"), S_EXP("^"), S_EXP_ALT("**"),
    S_ASSIGN("="), S_ADD_ASSIGN("+="), S_SUB_ASSIGN("-="), S_MULT_ASSIGN("*="),S_DIV_ASSIGN("/="), S_MOD_ASSIGN("%="),
    S_EXP_ASSIGN("^="), S_EXP_ALT_ASSIGN("**="),
    S_INC("++"), S_DEC("--"),
    S_LPAREN("("), S_RPAREN(")"), S_LBRACE("{"), S_RBRACE("}"), S_LBRACKET("["), S_RBRACKET("]"),
    S_SEMICOLON(";"), S_COMMA(","), S_DOT(".");

    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
