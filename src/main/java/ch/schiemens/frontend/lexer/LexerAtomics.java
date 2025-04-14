package ch.schiemens.frontend.lexer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LexerAtomics {

    private final static Set<Character> symbols = new HashSet<>(List.of(
            '(', ')', '{', '}', '[', ']', ';', ',', '.',
            '+', '-', '*', '/', '%', '^', '!', '=', '<', '>', '&', '|', '"'
    ));

    private final static Set<Character> digits = new HashSet<>(List.of(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    ));

    private final static Set<Character> abc = new HashSet<>(List.of(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    ));

    private final static Set<Character> whitespace = new HashSet<>(List.of(
            ' ', '\t', '\n', '\r', '\f', '\b'
    ));

    public static boolean isSymbol(int character) {
        return symbols.contains((char) character);
    }

    public static boolean isDigit(int character) {
        return digits.contains((char) character);
    }

    public static boolean isAbc(int character) {
        return abc.contains((char) character);
    }

    public static boolean isWhitespace(int character) {
        return whitespace.contains((char) character);
    }

}
