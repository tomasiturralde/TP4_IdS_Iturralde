package parser;

import lexer.Token;

import java.util.ArrayList;
import java.util.List;

class State {
    private boolean acceptance;
    private List<Transition> transitions;

    State(boolean acceptance) {
        this.acceptance = acceptance;
        this.transitions = new ArrayList<>();
    }

    boolean isAcceptance() {
        return acceptance;
    }

    void setTransition(Transition transition) {
        transitions.add(transition);
    }

    void goTo(Token token, ParserImpl parser) {
        boolean pathNotFound = true;
        for (Transition transition : transitions) {
            if (token.getType().matches(transition.getType()) && (token.getText().equals(transition.getText()) || token.getText().matches(transition.getText()))){
                parser.addToken(token);
                parser.setCurrentState(transition.getStateTo());
                pathNotFound = false;
            }
        }
        if (pathNotFound)
            throw new RuntimeException("No transition found");
    }
}
