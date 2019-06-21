package parser;

public class StateFactory {
    private State currentState;
    private State startingState;

    public StateFactory() {
        currentState = new State(false);
        startingState = currentState;
    }

    public State getStartingState() {
        return startingState;
    }

    public void createStates(String acceptedLine) {
        String[] transitions = acceptedLine.split("->");
        for (String s : transitions) {

            //TODO: this doesn't work, needs to divide by routes ([route])
            String[] transition = s.split(" ");
            if (transition.length == 2) {
                createAState(transition[0], transition[1]);
            } else {
                createAState(transition[0], ".*");
            }



        }
        currentState = startingState;
    }

    private void createAState(String type, String text) {
        boolean acceptance = text.matches(";");
        State newState = new State(acceptance);

        Transition transition = new Transition(type, text, newState);
        currentState.setTransition(transition);
        currentState = newState;
    }
}
