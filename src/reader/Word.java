package reader;

public class Word {
    private int line;
    private int startingPosition;
    private String text;

    public Word(int line, int startingPosition, String text) {
        this.line = line;
        this.startingPosition = startingPosition;
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getStartingPosition() {
        return startingPosition;
    }

    public void setStartingPosition(int startingPosition) {
        this.startingPosition = startingPosition;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
