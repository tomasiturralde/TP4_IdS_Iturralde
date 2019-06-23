package parser;

public interface Parser {
    ASTNode getRootNode();
    void generateNextTree();
}
