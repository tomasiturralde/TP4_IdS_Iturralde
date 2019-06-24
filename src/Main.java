import dictionary.Handler;
import interpreter.InterpreterImpl;
import lexer.LexerImpl;
import lexer.ObservableLexer;
import observer.Observer;
import parser.ParserImpl;
import parser.StateFactory;
import reader.LineFileReader;
import reader.StringWordReader;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        LineFileReader lineReader = new LineFileReader();
        lineReader.initialize("C:\\Users\\tomy\\IdeaProjects\\TP4_IdS_Iturralde\\src\\acceptedLines.txt");
        lineReader.readLines();
        List<String> lines = lineReader.getLines();

        StateFactory factory = new StateFactory();
        for (String line : lines) {
            factory.createStates(line);
        }

        InterpreterImpl interpreter = new InterpreterImpl();
        List<Observer> obs = new ArrayList<>();
        obs.add(interpreter);

        Handler emptyHandler = new Handler(null, ".*", "");
        Handler reassignHandler = new Handler(emptyHandler, "IDENTIFIER=EXP;", "REASSIGN");
        Handler printHandler = new Handler(reassignHandler, "print[(]EXP[)];", "PRINT");
        Handler assignHandler = new Handler(printHandler, "letIDENTIFIER:VARTYPE(=EXP)?;", "ASSIGN");
        Handler lineHandler = new Handler(assignHandler, "ASSIGN|PRINT|REASSIGN", "LINE");

        ParserImpl parser = new ParserImpl(factory.getStartingState(), obs, lineHandler);
        List<Observer> observers = new ArrayList<>();
        observers.add(parser);
        StringWordReader reader = new StringWordReader("let x: number = 3 * y + 'hola';\nx = 4 - 2 + 1;\nprint(x);");

        Handler errorHandler = new Handler(null, ".*", "Error");
        Handler identifierHandler = new Handler(errorHandler, "_?[a-zA-Z]+[a-zA-Z0-9]*", "Identifier");
        Handler varTypeHandler = new Handler(identifierHandler, "string|number", "VarType");
        Handler keywordHandler = new Handler(varTypeHandler, "let|print", "Keyword");
        Handler stringHandler = new Handler(keywordHandler, "'.+'|\".+\"", "String");
        Handler separatorHandler = new Handler(stringHandler, "[=;:()]", "Separator");
        Handler operatorHandler = new Handler(separatorHandler, "[+*/-]", "Operator");
        Handler numberHandler = new Handler(operatorHandler, "[0-9]+[.]?[0-9]*", "Number");

        ObservableLexer lexer = new LexerImpl(observers, reader, numberHandler);

        parser.setLexer(lexer);

        while (!reader.reachedEnd()) {
            lexer.readNext();
            System.out.println();
        }
    }
}
