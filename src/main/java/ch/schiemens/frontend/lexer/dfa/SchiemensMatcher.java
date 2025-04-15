package ch.schiemens.frontend.lexer.dfa;

import ch.schiemens.frontend.lexer.token.TokenType;

public class SchiemensMatcher {

    private final static SchiemensMatcher INSTANCE = new SchiemensMatcher();
    private final static DFAFactory schiemensFactory = new DFAFactory();

    private final static String HEX_NUMBER = "0123456789abcdefABCDEF";
    private final static String OCTAL_NUMBER = "01234567";
    private final static String BINARY_NUMBER = "01";
    private final static String DECIMAL_NUMBER = "0123456789";
    private final static String NATURAL_NUMBER = "123456789";

    private SchiemensMatcher() {
    }

    public static void build() {
        subNumbers();
    }

    public SchiemensMatcher getInstance() {
        return INSTANCE;
    }

    public static DFAMatcher getMatcher() {
        return schiemensFactory.build();
    }

    private static void subNumbers() {
        schiemensFactory.create("numberStart", false, TokenType.DUMMY);      // Entry
        schiemensFactory.create("zero", true, TokenType.L_INT);              // '0' only
        schiemensFactory.create("intOK", true, TokenType.L_INT);             // 1-9 digits
        schiemensFactory.create("hexStart", false, TokenType.DUMMY);
        schiemensFactory.create("hexOK", true, TokenType.L_HEX);
        schiemensFactory.create("binaryStart", false, TokenType.DUMMY);
        schiemensFactory.create("binaryOK", true, TokenType.L_BINARY);
        schiemensFactory.create("octalStart", false, TokenType.DUMMY);
        schiemensFactory.create("octalOK", true, TokenType.L_OCTAL);
        schiemensFactory.create("doubleStart", false, TokenType.DUMMY);
        schiemensFactory.create("doubleOK", true, TokenType.L_DOUBLE);

        schiemensFactory.connect("start", CharacterPredicate.always(), "numberStart");

        // Transitions
        // Start -> 0
        schiemensFactory.connect("numberStart", CharacterPredicate.is('0'), "zero");
        // zero → 0b
        schiemensFactory.connect("zero", CharacterPredicate.is('b'), "binaryStart");
        schiemensFactory.connect("binaryStart", CharacterPredicate.inSet(BINARY_NUMBER), "binaryOK");
        schiemensFactory.connect("binaryOK", CharacterPredicate.inSet(BINARY_NUMBER), "binaryOK");

        // zero → 0x
        schiemensFactory.connect("zero", CharacterPredicate.is('x'), "hexStart");
        schiemensFactory.connect("hexStart", CharacterPredicate.inSet(HEX_NUMBER), "hexOK");
        schiemensFactory.connect("hexOK", CharacterPredicate.inSet(HEX_NUMBER), "hexOK");

        // numberStart → o
        schiemensFactory.connect("numberStart", CharacterPredicate.is('o'), "octalStart");
        schiemensFactory.connect("octalStart", CharacterPredicate.inSet(OCTAL_NUMBER), "octalOK");
        schiemensFactory.connect("octalOK", CharacterPredicate.inSet(OCTAL_NUMBER), "octalOK");

        // zero or intOK -> . → doubleStart
        schiemensFactory.connect("zero", CharacterPredicate.is('.'), "doubleStart");
        schiemensFactory.connect("intOK", CharacterPredicate.is('.'), "doubleStart");
        schiemensFactory.connect("doubleStart", CharacterPredicate.inSet(DECIMAL_NUMBER), "doubleOK");
        schiemensFactory.connect("doubleOK", CharacterPredicate.inSet(DECIMAL_NUMBER), "doubleOK");

        // numberStart → 1-9
        schiemensFactory.connect("numberStart", CharacterPredicate.inSet(NATURAL_NUMBER), "intOK");
        schiemensFactory.connect("intOK", CharacterPredicate.inSet(DECIMAL_NUMBER), "intOK");
    }


}
