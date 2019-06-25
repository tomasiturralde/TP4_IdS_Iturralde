package parser;

import dictionary.Dictionary;
import lexer.Lexer;
import lexer.Token;
import observer.Observer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParserImpl implements Observer, ObservableParser {
    private State startingState;
    private State currentState;
    private Lexer lexer;
    private List<Observer> observers;
    private List<Token> readTokens;
    private ASTNode root;
    private NodeHandler handler;

    public ParserImpl(State startingState, List<Observer> observers, Dictionary dictionary) {
        this.startingState = startingState;
        this.currentState = startingState;
        this.observers = observers;
        readTokens = new ArrayList<>();
        handler = new NodeHandler(dictionary);
        root = new ASTNode(null);
        root.setType("PROGRAM");
    }

    @Override
    public void update() {
        Token token = lexer.getToken();
        if (token != null) {
            currentState.goTo(token, this);
            if (currentState.isAcceptance())
                parseList();
            lexer.readNext();
        } else {
            notifyObservers();
        }
    }

    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    private void returnToBeginning() {
        currentState = startingState;
    }

    void addToken(Token token) {
        readTokens.add(token);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    private void parseList() {
        Stack<ASTNode> nodes = new Stack<>();
        List<Token> expressionTokens = new ArrayList<>();
        int i = 0;
        while (true) {
            if (readTokens.get(i).getType().matches("Identifier|VarType")){
                handler.generateBasicNode(nodes, readTokens.get(i), readTokens.get(i).getType().toUpperCase());
                readTokens.remove(i);
            }
            else if (readTokens.get(i).getType().matches("Keyword|Separator")) {
                handler.generateBasicNode(nodes, readTokens.get(i), readTokens.get(i).getText());
                Token token = readTokens.remove(i);
                if (token.getText().matches("=|[(]|;")) {
                    break;
                }
            }
        }

        if (!readTokens.isEmpty()) {
            while (!readTokens.get(i).getType().equals("Separator")) {
                expressionTokens.add(readTokens.remove(i));
            }
            nodes.push(handler.parseExpression(expressionTokens));
        }
        while (!readTokens.isEmpty()) {
            handler.generateBasicNode(nodes, readTokens.get(i), readTokens.get(i).getText());
            readTokens.remove(i);
        }

        returnToBeginning();
        setRootNode(handler.createLineNode(nodes));
    }

    @Override
    public void setRootNode(ASTNode node) {
        root.addSubNode(node);
    }

    @Override
    public ASTNode getRootNode() {
        return root;
    }

    @Override
    public void generateNextTree() {
        lexer.readNext();
    }
}
