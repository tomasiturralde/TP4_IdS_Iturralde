package parser;

import java.util.ArrayList;
import java.util.List;

public class StateFactory {
    private State currentState;
    private List<State> states;

    public StateFactory() {
        currentState = new State(false);
        states = new ArrayList<>();
        states.add(currentState);
    }

    public State getStartingState() {
        return states.get(0);
    }

    public void createStates(String acceptedLine) {
        String[] transitions = acceptedLine.split("->");
        for (String s : transitions) {

            //if it's a number, go back to the number's position
            if (s.matches("[0-9]+")) {
                currentState = states.get(Integer.parseInt(s));
                continue;
            }

            //if it's an add number, add that state to the current one
            if (s.matches("A [0-9]+ [a-zA-Z]+ ?[a-zA-Z]*")) {
                createExtraTransition(s);
                continue;
            }

            //Create a new state and go to that one
            String[] transition = s.split(" ");
            if (transition.length == 2) {
                createAState(transition[0], transition[1]);
            } else {
                createAState(transition[0], ".*");
            }

        }
        currentState = getStartingState();
    }

    private void createExtraTransition(String transition) {
        String[] stateId = transition.split(" ");
        State stateToAdd = states.get(Integer.parseInt(stateId[1]));
        Transition newTransition;
        if (stateId.length == 3) {
            newTransition = new Transition(stateId[2], ".*", stateToAdd);
        } else {
            newTransition = new Transition(stateId[2], stateId[3], stateToAdd);
        }
        currentState.setTransition(newTransition);
    }

    private void createAState(String type, String text) {
        boolean acceptance = text.matches(";");
        State newState = new State(acceptance);

        Transition transition = new Transition(type, text, newState);
        currentState.setTransition(transition);
        states.add(newState);
        currentState = newState;
    }
}
