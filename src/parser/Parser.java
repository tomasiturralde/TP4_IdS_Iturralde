package parser;

import lexer.Token;
import java.util.List;

public interface Parser {
    ASTNode generateTree(List<Token> tokens);
}
