package parser;

import dictionary.Dictionary;
import lexer.Token;
import java.util.List;
import java.util.Stack;

class NodeHandler {
    private Dictionary dictionary;

    NodeHandler(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    ASTNode parseExpression(List<Token> tokens) {
        ASTNode expression = new ASTNode(null);
        expression.addSubNode(parse(tokens));
        expression.setType("EXP");
        return expression;
    }

    private ASTNode parse(List<Token> tokens) {
        if (tokens.size() == 1) {
            ASTNode node = new ASTNode(tokens.get(0));
            node.setType(tokens.get(0).getType().toUpperCase() + "_LITERAL");
            return node;
        }
        else {
            int i = 0;
            String[] precedence = {"+", "-", "*", "/"};

            outerLoop: for (String s : precedence) {
                for (i = tokens.size()-1; i >= 0; i--) {
                    if (tokens.get(i).getText().equals(s)) {
                        break outerLoop;
                    }
                }
            }

            ASTNode root = new ASTNode(tokens.get(i));
            root.setType(tokens.get(i).getText());
            root.addSubNode(parse(tokens.subList(0, i)));
            root.addSubNode(parse(tokens.subList(i + 1, tokens.size())));
            return root;
        }
    }

    void generateBasicNode(Stack<ASTNode> nodes, Token currentToken, String s) {
        ASTNode currentNode = new ASTNode(currentToken);
        currentNode.setType(s);
        nodes.push(currentNode);
    }

    ASTNode createLineNode(Stack<ASTNode> nodes) {
        StringBuilder text = new StringBuilder();
        ASTNode root;
        while (true) {
            root = new ASTNode(null);

            while (!nodes.empty()) {
                ASTNode node = nodes.pop();
                text.insert(0, node.getType());
                root.getSubNodes().add(0, node);
            }

            String result = dictionary.checkType(text.toString());
            if (result.equals("")) {
                root = root.getLastNode();
                break;
            } else {
                root.setType(result);
                text = new StringBuilder();
                nodes.push(root);
            }
        }
        return root;
    }
}
