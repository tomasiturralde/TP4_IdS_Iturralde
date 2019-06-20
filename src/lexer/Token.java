package lexer;

public class Token {
    private int line;
    private int length;
    private int startingPosition;
    private String type;
    private String text;

    public Token(int line, int length, int startingPosition, String type, String text) {
        this.line = line;
        this.length = length;
        this.startingPosition = startingPosition;
        this.type = type;
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text + ", type: " + type + ", start: " + startingPosition + ", line: " + line + ", length: " + length;
    }
}
