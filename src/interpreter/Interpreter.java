package interpreter;

import parser.ASTNode;

public interface Interpreter {
    void readASTTree(ASTNode root);
}
