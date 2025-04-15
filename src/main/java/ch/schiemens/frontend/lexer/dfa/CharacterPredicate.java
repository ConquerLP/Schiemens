package ch.schiemens.frontend.lexer.dfa;

public interface CharacterPredicate {

    boolean test(char c);

    static CharacterPredicate isChar(char expected) {
        return c -> c == expected;
    }

    static CharacterPredicate inRange(char start, char end) {
        return c -> c >= start && c <= end;
    }

    static CharacterPredicate inSet(String chars) {
        return c -> chars.indexOf(c) >= 0;
    }

}
