package parser;

import lexer.Lexer;
import lexer.Token;
import observer.Observer;

import java.util.List;


public class ParserImpl implements Observer, ObservableParser {
    private State startingState;
    private State currentState;
    private Lexer lexer;
    private List<Observer> observers;
    // TODO: should have a node/tree

    public ParserImpl(State startingState) {
        this.startingState = startingState;
        this.currentState = startingState;
    }

    @Override
    public void update() {
//        Token token = lexer.getToken();
//        currentState.goTo(token, this);
//        if (!currentState.isAcceptance())
//            lexer.readNext();
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    void returnToBeginning() {
        currentState = startingState;
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
