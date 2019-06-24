package interpreter;

import observer.Observer;
import parser.Parser;

public class InterpreterImpl implements Observer, Interpreter {
    private Parser parser;

    @Override
    public void update() {

    }

    @Override
    public void readASTTree() {
        parser.generateNextTree();
    }
}
