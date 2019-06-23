package parser;

public interface ObservableParser extends Parser {
    void notifyObservers();
    void setRootNode(ASTNode node);
}
