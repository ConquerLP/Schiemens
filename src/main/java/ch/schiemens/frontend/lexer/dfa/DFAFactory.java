package ch.schiemens.frontend.lexer.dfa;

import ch.schiemens.frontend.lexer.token.TokenType;

import java.util.HashMap;
import java.util.Map;

public class DFAFactory {

    private final DFAMatcher master;
    private final Map<String, DFAMatcher.State> namedStates = new HashMap<>();

    public DFAFactory() {
        DFAMatcher.State start = new DFAMatcher.State("start", false, TokenType.DUMMY);
        this.master = new DFAMatcher(start);
        namedStates.put("start", start);
    }

    public DFAMatcher build() {
        return master;
    }

    public void register(String name, DFAMatcher.State state) {
        namedStates.put(name, state);
    }

    public DFAMatcher.State get(String name) {
        return namedStates.get(name);
    }

    public void connect(String fromName, CharacterPredicate condition, String toName) {
        DFAMatcher.State from = namedStates.get(fromName);
        DFAMatcher.State to = namedStates.get(toName);
        if (from == null || to == null) {
            throw new IllegalArgumentException("Undefined state: " + fromName + " or " + toName);
        }
        master.addTransition(from, condition, to);
    }

    public DFAMatcher.State create(String name, boolean accepting, TokenType type) {
        DFAMatcher.State state = new DFAMatcher.State(name, accepting, type);
        register(name, state);
        return state;
    }

}
