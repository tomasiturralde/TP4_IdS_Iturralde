package parser;

import lexer.Token;

import java.util.ArrayList;
import java.util.List;

public class ASTNode {
    private List<ASTNode> subNodes;
    private Token token;
    private String type;


    public ASTNode(Token token) {
        this.token = token;
        type = "";
        subNodes = new ArrayList<>();
    }

    public List<ASTNode> getSubNodes() {
        return subNodes;
    }

    public Token getToken() {
        return token;
    }

    public String getType() {
        return type;
    }

    public void addSubNode(ASTNode subNode) {
        subNodes.add(subNode);
    }

    public void setType(String type) {
        this.type = type;
    }
}
