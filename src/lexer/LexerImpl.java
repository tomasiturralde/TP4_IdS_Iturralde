package lexer;

import dictionary.Dictionary;
import observer.Observer;
import reader.Word;
import reader.WordReader;

import java.util.List;

public class LexerImpl implements ObservableLexer {
    private List<Observer> observers;
    private WordReader wordReader;
    private Token token;
    private Dictionary dictionary;

    public LexerImpl(List<Observer> observers, WordReader wordReader, Dictionary dictionary) {
        this.observers = observers;
        this.wordReader = wordReader;
        this.dictionary = dictionary;
        token = null;
    }

    @Override
    public Token getToken() {
        return token;
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    @Override
    public void setToken(Token token) {
        this.token = token;
        notifyObservers();
    }

    @Override
    public void readNext() {
        if (!wordReader.reachedEnd()) {
            Word nextWord = wordReader.read();
            String type = dictionary.checkType(nextWord.getText());
            Token newToken = new Token(nextWord.getLine(), nextWord.getStartingPosition(), type, nextWord.getText());
            if (newToken.getType().matches("Error")) {
                System.out.println("Error with token:" + newToken.getText() + ", at (" + newToken.getLine() + ", " + newToken.getStartingPosition() + ")");
            }
            setToken(newToken);
        }
        else {
            setToken(null);
        }
    }
}
