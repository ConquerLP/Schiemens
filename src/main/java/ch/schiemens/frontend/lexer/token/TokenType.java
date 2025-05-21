package ch.schiemens.frontend.lexer.token;

public enum TokenType {

    IDENTIFIER("identifier"), EOF("EOF"), INVALID("invalid token"),
    L_STRING("literal string"), L_CHAR("literal char"), L_INT("literal int"), L_DOUBLE("literal double"),
    L_HEX("literal hex"), L_OCTAL("literal octal"), L_BINARY("literal binary"),

    K_CLASS("class"), K_EXTENDS("extends"), K_METH("meth"), K_CONSTR("constr"), K_SUPER("super"),
    K_PUBLIC("public"), K_PROTECTED("protected"), K_PRIVATE("private"), K_FINAL("final"), K_STATIC("static"),
    K_NEW("new"), K_THIS("this"),
    K_VOID("void"), K_DOUBLE("double"), K_INT("int"), K_STRING("string"), K_BOOLEAN("boolean"), K_CHAR("char"),
    K_FUNCTION("func"), K_GLOBAL("global"),  K_LABEL("label"),
    K_VAR("var"), K_FCALL("fcall"), K_MCALL("mcall"),

    K_IF("if"), K_ELSE("else"), K_WHILE("while"), K_DO("do"), K_FOR("for"),
    K_CONTINUE("continue"), K_BREAK("break"), K_HOME("home"), K_RETURN("return"), K_GOTO("goto"),
    K_SWITCH("switch"), K_CASE("case"), K_DEFAULT("default"),
    K_TRUE("true"), K_FALSE("false"), K_NULL("null"),
    K_NOT("not"), K_AND("and"), K_OR("or"),
    S_NOT_ALT("!"), S_AND_ALT("&&"), S_OR_ALT("||"),

    S_EQ("=="), S_NEQ("!="), S_LEQ("<="), S_GEQ(">="), S_LT("<"), S_GT(">"),
    S_PLUS("+"), S_MINUS("-"), S_MULT("*"), S_DIV("/"), S_MOD("%"), S_EXP("^"), S_EXP_ALT("**"),
    S_ASSIGN("="), S_ADD_ASSIGN("+="), S_SUB_ASSIGN("-="), S_MULT_ASSIGN("*="), S_DIV_ASSIGN("/="), S_MOD_ASSIGN("%="),
    S_EXP_ASSIGN("^="), S_EXP_ALT_ASSIGN("**="),
    S_INC("++"), S_DEC("--"),
    S_LPAREN("("), S_RPAREN(")"), S_LBRACE("{"), S_RBRACE("}"), S_LBRACKET("["), S_RBRACKET("]"),
    S_SEMICOLON(";"), S_COMMA(","), S_DOT("."), S_COLON(":"),
    INLINE_COMMENT("inline comment"), MULTI_LINE_COMMENT("multiline comment");

    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
