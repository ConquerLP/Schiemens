package ch.schiemens.frontend.lexer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LexerAtomics {

    private final static Set<Character> decimals = new HashSet<>(List.of(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    ));

    private final static Set<Character> octal = new HashSet<>(List.of(
            '0', '1', '2', '3', '4', '5', '6', '7'
    ));

    private final static Set<Character> hexadecimals = new HashSet<>(List.of(
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f',
            'A', 'B', 'C', 'D', 'E', 'F'
    ));

    private final static Set<Character> identifierStart = new HashSet<>(List.of(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', '_'
    ));

    private final static Set<Character> alphaNum = new HashSet<>(List.of(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z', '_',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    ));

    private final static Set<Character> whitespace = new HashSet<>(List.of(
            ' ', '\t', '\n', '\r', '\f', '\b'
    ));

    private final static Set<Character> carriageReturns = new HashSet<>(List.of(
            '\r', '\n'
    ));

    public static boolean isCarriageReturn(char character) {
        return carriageReturns.contains(character);
    }

    public static boolean isDecimal(char character) {
        return decimals.contains(character);
    }

    public static boolean isOctal(char character) {
        return octal.contains(character);
    }

    public static boolean isHexadecimal(char character) {
        return hexadecimals.contains(character);
    }

    public static boolean isIdentifierStart(char character) {
        return identifierStart.contains(character);
    }

    public static boolean isWhitespace(char character) {
        return whitespace.contains(character);
    }

    public static boolean isBinary(char character) {
        return character == '0' || character == '1';
    }

    public static boolean isAlphaNum(char character) {
        return alphaNum.contains(character);
    }

}
