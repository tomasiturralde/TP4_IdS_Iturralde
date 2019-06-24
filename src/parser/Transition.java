package parser;

class Transition {
    private String type;
    private String text;
    private State stateTo;

    Transition(String type, String text, State stateTo) {
        this.type = type;
        this.text = text;
        this.stateTo = stateTo;
    }

    String getType() {
        return type;
    }

    String getText() {
        return text;
    }

    State getStateTo() {
        return stateTo;
    }
}
