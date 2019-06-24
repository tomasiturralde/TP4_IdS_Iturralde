package interpreter;

import observer.Observer;
import parser.ASTNode;
import parser.Parser;

public class InterpreterImpl implements Observer, Interpreter {
    private Parser parser;

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    @Override
    public void update() {
        runCode(parser.getRootNode());
    }

    @Override
    public void readASTTree() {
        parser.generateNextTree();
    }

    private void runCode(ASTNode root) {

    }
}
