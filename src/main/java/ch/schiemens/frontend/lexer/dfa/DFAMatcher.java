package ch.schiemens.frontend.lexer.dfa;

import ch.schiemens.frontend.lexer.LexerBuffer;
import ch.schiemens.frontend.lexer.token.TokenType;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DFAMatcher {

    public static class State {

        private final String name;
        private final boolean accepting;
        private final TokenType type;

        public State(String name, boolean accepting, TokenType type) {
            this.name = name;
            this.accepting = accepting;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public boolean isAccepting() {
            return accepting;
        }

        public TokenType getTokenType() {
            return type;
        }

    }

    private final State startState;
    private final Map<State, Map<CharacterPredicate, State>> transitions = new HashMap<>();

    public DFAMatcher(State startState) {
        this.startState = startState;
    }

    public void addTransition(State from, CharacterPredicate predicate, State to) {
        transitions.computeIfAbsent(from, k -> new LinkedHashMap<>()).put(predicate, to);
    }

    public DFAMatchResult matchStream(LexerBuffer input) throws IOException {
        State current = startState;
        StringBuilder consumed = new StringBuilder();
        DFAMatchResult lastAccepted = null;
        while (!input.isEOF()) {
            char c = (char) input.peek();
            State next = null;
            for (Map.Entry<CharacterPredicate, State> entry : transitions.getOrDefault(current, Map.of()).entrySet()) {
                if (entry.getKey().test(c)) {
                    next = entry.getValue();
                    break;
                }
            }
            if (next == null) break;
            consumed.append((char) input.advance());
            current = next;
            if (current.isAccepting()) {
                lastAccepted = new DFAMatchResult(current.getTokenType(), consumed.toString());
            }
        }
        return lastAccepted;
    }

}
