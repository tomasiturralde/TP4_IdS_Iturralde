package parser;

import dictionary.Dictionary;
import lexer.Lexer;
import lexer.Token;
import observer.Observer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;


public class ParserImpl implements Observer, ObservableParser {
    private State startingState;
    private State currentState;
    private Lexer lexer;
    private List<Observer> observers;
    private List<Token> readTokens;
    private ASTNode root;
    private Dictionary dictionary;

    public ParserImpl(State startingState, List<Observer> observers, Dictionary dictionary) {
        this.startingState = startingState;
        this.currentState = startingState;
        this.observers = observers;
        this.dictionary = dictionary;
        readTokens = new ArrayList<>();
    }

    @Override
    public void update() {
        Token token = lexer.getToken();
        currentState.goTo(token, this);
        if (!currentState.isAcceptance())
            lexer.readNext();
        else
            parseList();
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
        ASTNode currentNode = null;
        Token currentToken;
        String[] nodeTransitions = {"FACTOR", "TERM", "EXP"};
        int i = 0;
        while (i < readTokens.size()-1) {
            currentToken = readTokens.get(i);
            Token lookAhead = readTokens.get(i+1);

            // Si el current es un varType o id, crear un nodo con el token, asignarle el tipo, pushearlo al stack y seguir
            if (currentToken.getType().equals("VarType") || currentToken.getType().equals("Identifier")) {
                currentNode = getAstNode(nodes, currentToken, currentToken.getType().toUpperCase());
                i += 1;
                continue;
            }

            // Si es un separador, guardarlo con el tipo del nodo siendo el texto del token
            if (currentToken.getType().matches("Separator|Keyword")) {
                currentNode = getAstNode(nodes, currentToken, currentToken.getText());
                i += 1;
                continue;
            }

            // Si el lookAhead es un + o -, el current tiene que ser un EXP
            if (lookAhead.getText().matches("[+]|-")) {
                if (currentNode == null) {
                    currentNode = new ASTNode(currentToken);
                    currentNode.setType("LITERAL");
                    for (String nodeTransition : nodeTransitions) {
                        currentNode = getUpperAstNode(currentNode, nodeTransition);
                    }
                } else {
                    List<String> temp = Arrays.asList(nodeTransitions);
                    for (String nodeTransition : nodeTransitions) {
                        if (currentNode.getType().equals(nodeTransition)) {
                            break;
                        }
                        temp.remove(nodeTransition);
                    }
                    for (String nodeTransition : temp) {
                        currentNode = getUpperAstNode(currentNode, nodeTransition);
                    }
                }
                nodes.push(currentNode);
                currentNode = null;
                i += 1;
            }
            // Si el lookAhead es un * o /, el current tiene que ser un TERM
            else if (lookAhead.getText().matches("[*]|/")) {
                if (currentNode == null) {
                    currentNode = new ASTNode(currentToken);
                    currentNode.setType("LITERAL");
                    for (int j = 0; j < nodeTransitions.length-1; j++) {
                        currentNode = getUpperAstNode(currentNode, nodeTransitions[j]);
                    }
                } else {
                    if (currentNode.getType().equals("FACTOR")) {
                        currentNode = getUpperAstNode(currentNode, "TERM");
                    }
                }
                nodes.push(currentNode);
                currentNode = null;
                i += 1;
            }
            // Si el current es un + o -, el lookAhead tiene que ser un TERM
            else if (currentToken.getText().matches("[+]|-")) {
                currentNode = new ASTNode(currentToken);
                currentNode.setType(currentToken.getText());
                nodes.push(currentNode);
                currentNode = new ASTNode(lookAhead);
                currentNode.setType("LITERAL");
                String[] temp = {"FACTOR", "TERM"};
                for (String nodeTransition : temp) {
                    currentNode = getUpperAstNode(currentNode, nodeTransition);
                }
                i += 1;
            }
            // Si el current es un * o /, el lookAhead tiene que ser un FACTOR
            else if (currentToken.getText().matches("[*]|/")) {
                currentNode = new ASTNode(currentToken);
                currentNode.setType(currentToken.getText());
                nodes.push(currentNode);

                ASTNode nextNode = new ASTNode(lookAhead);
                nextNode.setType("LITERAL");

                ASTNode nextNode1 = new ASTNode(null);
                nextNode1.setType("FACTOR");
                nextNode1.addSubNode(nextNode);

                currentNode = nextNode1;
                i += 1;
            }
            // Si no cumple nada de lo anterior, es porque ya no se realizan modificaciones sobre el nodo, entonces se almacena
            else {
                nodes.push(currentNode);
                currentNode = null;
                i += 1;
            }
        }

        // Ir popeando el stack, y concatenando los tipos, y probar que cumplan alguna regla
        createASTLine(nodes, i);
    }

    private void createASTLine(Stack<ASTNode> nodes, int i) {
        Token currentToken;
        ASTNode currentNode;
        ASTNode newLine = new ASTNode(null);
        StringBuilder addedTexts = new StringBuilder();
        Stack<ASTNode> removedNodes = new Stack<>();
        while (nodes.size() > 1) {
            ASTNode node = nodes.pop();
            removedNodes.push(node);
            if (nodes.size() >= 3) {
                ASTNode node1 = nodes.pop();
                removedNodes.push(node1);
                ASTNode node2 = nodes.pop();
                removedNodes.push(node2);

                addedTexts.insert(0, node.getType());
                addedTexts.insert(0, node1.getType());
                addedTexts.insert(0, node2.getType());

                String res = dictionary.checkType(addedTexts.toString());
                if (!res.equals("")) {
                    createParentNode(nodes, removedNodes, res);
                    addedTexts = new StringBuilder();
                } else {
                    removedNodes.pop();
                    removedNodes.pop();
                    nodes.push(node2);
                    nodes.push(node1);
                    addedTexts = new StringBuilder();
                    String res1 = dictionary.checkType(addedTexts.toString());
                    createParentNode(nodes, removedNodes, res1);
                    addedTexts = new StringBuilder();
                }
                continue;
            }

            addedTexts.insert(0, node.getType());
            removedNodes.push(node);
            String res = dictionary.checkType(addedTexts.toString());
            //TODO: hacer que esto funcione
            if (!res.equals("")) {
                createParentNode(nodes, removedNodes, res);
                addedTexts = new StringBuilder();
            }
        }

        // Crear el nodo del ;, y concatenarlo con el resto
        currentToken = readTokens.get(i);
        currentNode = new ASTNode(currentToken);
        currentNode.setType(currentToken.getText());
        ASTNode statementNode = nodes.pop();
        String statement = statementNode.getType() + currentNode.getType();
        String line = dictionary.checkType(statement);
        newLine.setType(line);
        newLine.addSubNode(statementNode);
        newLine.addSubNode(currentNode);

        returnToBeginning();
        setRootNode(newLine);
    }

    private void createParentNode(Stack<ASTNode> nodes, Stack<ASTNode> removedNodes, String res) {
        ASTNode parentNode = new ASTNode(null);
        parentNode.setType(res);
        while (!removedNodes.empty()) {
            parentNode.addSubNode(removedNodes.pop());
        }
        nodes.push(parentNode);
    }

    private ASTNode getAstNode(Stack<ASTNode> nodes, Token currentToken, String s) {
        ASTNode currentNode;
        currentNode = new ASTNode(currentToken);
        currentNode.setType(s);
        nodes.push(currentNode);
        currentNode = null;
        return currentNode;
    }

    private ASTNode getUpperAstNode(ASTNode currentNode, String nodeTransition2) {
        ASTNode newNode = new ASTNode(null);
        newNode.setType(nodeTransition2);
        newNode.addSubNode(currentNode);
        return newNode;
    }

    @Override
    public void setRootNode(ASTNode node) {
        root = node;
        notifyObservers();
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
