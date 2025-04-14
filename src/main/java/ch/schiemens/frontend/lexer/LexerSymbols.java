package ch.schiemens.frontend.lexer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LexerSymbols {

    private static Set<Character> symbols = new HashSet<>(List.of(
            '(', ')', '{', '}', '[', ']', ';', ',', '.', '+', '-', '*', '/', '%', '^'
    ));

    public static boolean isSymbol(int character) {
        char possibleSymbol = (char) character;


        return false;
    }

}
