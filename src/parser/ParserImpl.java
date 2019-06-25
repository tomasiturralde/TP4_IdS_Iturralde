package parser;

import dictionary.Dictionary;
import lexer.Token;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ParserImpl implements Parser {
    private State startingState;
    private State currentState;
    private ASTNode root;
    private NodeHandler handler;

    public ParserImpl(State startingState, Dictionary dictionary) {
        this.startingState = startingState;
        this.currentState = startingState;
        handler = new NodeHandler(dictionary);
        root = new ASTNode(null);
        root.setType("PROGRAM");
    }

    private void returnToBeginning() {
        currentState = startingState;
    }

    void setCurrentState(State currentState) {
        this.currentState = currentState;
    }

    private ASTNode parse(List<Token> tokens) {
        Stack<ASTNode> nodes = new Stack<>();
        List<Token> expressionTokens = new ArrayList<>();
        int i = 0;
        while (true) {
            if (tokens.get(i).getType().matches("Identifier|VarType")){
                handler.generateBasicNode(nodes, tokens.get(i), tokens.get(i).getType().toUpperCase());
                tokens.remove(i);
            }
            else if (tokens.get(i).getType().matches("Keyword|Separator")) {
                handler.generateBasicNode(nodes, tokens.get(i), tokens.get(i).getText());
                Token token = tokens.remove(i);
                if (token.getText().matches("=|[(]|;")) {
                    break;
                }
            }
        }

        if (!tokens.isEmpty()) {
            while (!tokens.get(i).getType().equals("Separator")) {
                expressionTokens.add(tokens.remove(i));
            }
            nodes.push(handler.parseExpression(expressionTokens));
        }
        while (!tokens.isEmpty()) {
            handler.generateBasicNode(nodes, tokens.get(i), tokens.get(i).getText());
            tokens.remove(i);
        }

        returnToBeginning();
        return handler.createLineNode(nodes);
    }

    @Override
    public ASTNode generateTree(List<Token> tokens) {
        List<Token> readTokens = new ArrayList<>();
        for (Token token : tokens) {
            currentState.goTo(token, this);
            readTokens.add(token);
            if (currentState.isAcceptance()) {
                root.addSubNode(parse(readTokens));
                readTokens = new ArrayList<>();
            }
        }
        return root;
    }
}
