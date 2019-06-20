package lexer;

public interface Lexer {
    void readNext();
    Token getToken();
}
