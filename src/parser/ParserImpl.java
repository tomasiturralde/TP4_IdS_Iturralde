package parser;

import lexer.Lexer;
import lexer.Token;

import java.util.List;


public class ParserImpl implements Observer, ObservableParser {
    private State currentState;
    private Lexer lexer;
    private List<Observer> observers;
    // TODO: should have a node/tree

    public ParserImpl(State currentState, Lexer lexer, List<Observer> observers) {
        this.currentState = currentState;
        this.lexer = lexer;
        this.observers = observers;
    }

    @Override
    public void update() {
        Token token = lexer.getToken();
        currentState.goTo(token, this);
        if (!currentState.isAcceptance())
            lexer.readNext();
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
