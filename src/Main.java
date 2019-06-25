import dictionary.Handler;
import interpreter.InterpreterImpl;
import lexer.Lexer;
import lexer.LexerImpl;
import observer.Observer;
import parser.ParserImpl;
import parser.StateFactory;
import reader.LineFileReader;
import reader.StringWordReader;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String code = "let x: string;" +
                "x = 'aloha';\n" +
                "print(x);\n" +
                "x = \"alo\" + 'hola';\n" +
                "print(x);\n" +
                "x = 4 + 3 * 2 + 'hola' + 5 + x;\n" +
                "print(x);\n" +
                "print(5 + ', ' + x);";

        LineFileReader lineReader = new LineFileReader();
        lineReader.initialize("C:\\Users\\tomy\\IdeaProjects\\TP4_IdS_Iturralde\\src\\acceptedLines.txt");
        lineReader.readLines();
        List<String> lines = lineReader.getLines();

        StateFactory factory = new StateFactory();
        lines.forEach(factory::createStates);

        InterpreterImpl interpreter = new InterpreterImpl();
        List<Observer> obs = new ArrayList<>();
        obs.add(interpreter);

        Handler emptyHandler = new Handler(null, ".*", "");
        Handler reassignHandler = new Handler(emptyHandler, "IDENTIFIER=EXP;", "REASSIGN");
        Handler printHandler = new Handler(reassignHandler, "print[(]EXP[)];", "PRINT");
        Handler assignHandler = new Handler(printHandler, "letIDENTIFIER:VARTYPE(=EXP)?;", "ASSIGN");

        ParserImpl parser = new ParserImpl(factory.getStartingState(), obs, assignHandler);
        interpreter.setParser(parser);
        List<Observer> observers = new ArrayList<>();
        observers.add(parser);
        StringWordReader reader = new StringWordReader(code);

        Handler errorHandler = new Handler(null, ".*", "Error");
        Handler identifierHandler = new Handler(errorHandler, "_?[a-zA-Z]+[a-zA-Z0-9]*", "Identifier");
        Handler varTypeHandler = new Handler(identifierHandler, "string|number", "VarType");
        Handler keywordHandler = new Handler(varTypeHandler, "let|print", "Keyword");
        Handler stringHandler = new Handler(keywordHandler, "'.+'|\".+\"", "String");
        Handler separatorHandler = new Handler(stringHandler, "[=;:()]", "Separator");
        Handler operatorHandler = new Handler(separatorHandler, "[+*/-]", "Operator");
        Handler numberHandler = new Handler(operatorHandler, "[0-9]+[.]?[0-9]*", "Number");

        Lexer lexer = new LexerImpl(observers, reader, numberHandler);

        parser.setLexer(lexer);

        interpreter.readASTTree();
    }
}
