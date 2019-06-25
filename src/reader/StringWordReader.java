package reader;

public class StringWordReader implements WordReader {
    private String input;
    private int charCount;
    private int lineCount;
    private int startingPosition;

    public StringWordReader(String input) {
        this.input = input;
        charCount = 0;
        lineCount = 0;
        startingPosition = 0;
    }

    @Override
    public Word read() {
        StringBuilder temp = new StringBuilder();
        int currentStart = startingPosition;

        while (true) {

            //if input is starting a string with ""
            if (input.charAt(charCount) == '"') {
                createStringWord(temp, '"');
            }

            //if input is starting a string with ''
            else if (input.charAt(charCount) == '\'') {
                createStringWord(temp, '\'');
            }

            //if char is an enter, add one to line counter
            if (input.charAt(charCount) == '\n') {
                lineCount += 1;
                startingPosition = 0;
                currentStart = 0;
                charCount += 1;
                continue;
            }

            //if char is a letter
            if (!charIsSeparator(input.charAt(charCount)) && input.charAt(charCount) != ' ') {
                temp.append(input.charAt(charCount++));
            }

            //if char is a separator
            else if (charIsSeparator(input.charAt(charCount))) {
                //if temp is empty, add the separator
                if (temp.length() == 0) {
                    temp.append(input.charAt(charCount++));
                }
                //current word is complete, so send it
                break;
            }

            //char is a space
            else {
                //if temp is empty, move past the space and continue
                if (temp.length() == 0) {
                    charCount += 1;
                    startingPosition += 1;
                    currentStart += 1;
                }
                //if temp isn't empty, word is complete, so send it
                else {
                    charCount += 1;
                    startingPosition += 1;
                    break;
                }
            }
        }
        startingPosition += temp.length();
        return new Word(lineCount, currentStart, temp.toString());
    }

    private void createStringWord(StringBuilder temp, char c) {
        temp.append(input.charAt(charCount++));
        while (charCount < input.length() && input.charAt(charCount) != c) {
            temp.append(input.charAt(charCount++));
        }
        temp.append(input.charAt(charCount++));
    }

    @Override
    public boolean reachedEnd() {
        return charCount == input.length();
    }

    private boolean charIsSeparator(char input) {
        String temp = "" + input;
        return temp.matches("[=;:()+*/-]");
    }
}
