package lexer;

public class Token {
    private int line;
    private int startingPosition;
    private String type;
    private String text;

    Token(int line, int startingPosition, String type, String text) {
        this.line = line;
        this.startingPosition = startingPosition;
        this.type = type;
        this.text = text;
    }

    int getLine() {
        return line;
    }

    int getStartingPosition() {
        return startingPosition;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return text + ", type: " + type + ", start: " + startingPosition + ", line: " + line;
    }
}
