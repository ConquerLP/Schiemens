package ch.schiemens.frontend.lexer.dfa;

public class SchiemensMatcher {

    private final static SchiemensMatcher INSTANCE = new SchiemensMatcher();
    private final static DFAFactory schiemensFactory = new DFAFactory();

    private SchiemensMatcher() {
        subINT();
    }

    public SchiemensMatcher getInstance() {
        return INSTANCE;
    }

    public static DFAMatcher getMatcher() {
        return schiemensFactory.build();
    }

    private static void subINT() {

    }

}
