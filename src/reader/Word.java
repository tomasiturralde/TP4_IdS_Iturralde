package reader;

public class Word {
    private int line;
    private int startingPosition;
    private String text;

    Word(int line, int startingPosition, String text) {
        this.line = line;
        this.startingPosition = startingPosition;
        this.text = text;
    }

    public int getLine() {
        return line;
    }

    public int getStartingPosition() {
        return startingPosition;
    }

    public String getText() {
        return text;
    }
}
