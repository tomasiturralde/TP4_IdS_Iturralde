package lexer;

public interface ObservableLexer extends Lexer {
    void notifyObservers();

    void setToken(Token token);
}
