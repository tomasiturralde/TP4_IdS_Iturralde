package parser;

import lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class State {
    private boolean acceptance;
    private List<Transition> transitions;

    State(boolean acceptance) {
        this.acceptance = acceptance;
        this.transitions = new ArrayList<>();
    }

    public boolean isAcceptance() {
        return acceptance;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransition(Transition transitions) {
        this.transitions.add(transitions);
    }

    public void goTo(Token token, ParserImpl parser) {
        boolean pathNotFound = true;
        for (Transition transition : transitions) {
            if (token.getType().matches(transition.getType()) && token.getText().matches(transition.getText())){
                // TODO: Add a node to the tree
                parser.setCurrentState(transition.getStateTo());
                pathNotFound = false;
            }
        }
        if (pathNotFound)
            throw new RuntimeException("Invalid transitions");
    }
}
