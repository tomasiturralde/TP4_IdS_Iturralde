package parser;

public class Transition {
    private String type;
    private String text;
    private State stateTo;

    public Transition(String type, String text, State stateTo) {
        this.type = type;
        this.text = text;
        this.stateTo = stateTo;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public State getStateTo() {
        return stateTo;
    }
}
