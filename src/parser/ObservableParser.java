package parser;

public interface ObservableParser extends Parser {
    void notifyObservers();
    // TODO: add setTree/setNode, which triggers notifyObservers
}
