package parser;

import lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class State {
    private boolean acceptance;
    private List<Transition> transitions;

    public State(boolean acceptance, List<Transition> transitions) {
        this.acceptance = acceptance;
        this.transitions = transitions;
    }

    public boolean isAcceptance() {
        return acceptance;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void goTo(Token token, ParserImpl parser) {
        List<Transition> temp = new ArrayList<>();
        for (Transition transition : transitions) {
            if (token.getType().equals(transition.getType()))
                temp.add(transition);
        }
        if (temp.size() > 1) {
            for (Transition transition : temp) {
                if (token.getText().equals(transition.getText())) {
                    // TODO: Add a node to the tree
                    parser.setCurrentState(transition.getStateTo());
                }
            }
            throw new RuntimeException("Invalid transition");
        }
        else if (temp.size() == 1) {
            // TODO: Add a node to the tree
            parser.setCurrentState(temp.get(0).getStateTo());
        }
        else {
            if (acceptance) {
                // TODO: Finish tree, add it to parser
            } else {
                throw new RuntimeException("Invalid transition");
            }
        }
    }
}
