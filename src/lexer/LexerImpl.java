package lexer;

import dictionary.Dictionary;
import reader.Word;
import reader.WordReader;
import java.util.ArrayList;
import java.util.List;

public class LexerImpl implements Lexer {
    private WordReader wordReader;
    private Dictionary dictionary;

    public LexerImpl(WordReader wordReader, Dictionary dictionary) {
        this.wordReader = wordReader;
        this.dictionary = dictionary;
    }

    @Override
    public List<Token> getTokens() {
        List<Token> tokens = new ArrayList<>();
        while (!wordReader.reachedEnd()) {
            Word nextWord = wordReader.read();
            String type = dictionary.checkType(nextWord.getText());
            Token newToken = new Token(nextWord.getLine(), nextWord.getStartingPosition(), type, nextWord.getText());
            if (newToken.getType().matches("Error")) {
                System.out.println("Error with tokens:" + newToken.getText() + ", at (" + newToken.getLine() + ", " + newToken.getStartingPosition() + ")");
            }
            tokens.add(newToken);
        }
        return tokens;
    }
}
