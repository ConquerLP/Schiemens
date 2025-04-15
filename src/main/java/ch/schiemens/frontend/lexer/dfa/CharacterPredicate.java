package ch.schiemens.frontend.lexer.dfa;

public interface CharacterPredicate {

    boolean test(char c);

    static CharacterPredicate is(char expected) {
        return c -> c == expected;
    }

    static CharacterPredicate inSet(String chars) {
        return c -> chars.indexOf(c) >= 0;
    }

    static CharacterPredicate notInSet(String chars) {
        return c -> !(chars.indexOf(c) >= 0);
    }

    static CharacterPredicate always() {
        return c -> true;
    }

}
