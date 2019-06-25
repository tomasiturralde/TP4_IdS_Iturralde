import compiler.Compiler;
import compiler.CompilerImpl;
import dictionary.Handler;
import interpreter.Interpreter;
import interpreter.InterpreterImpl;
import lexer.Lexer;
import lexer.LexerImpl;
import parser.Parser;
import parser.ParserImpl;
import parser.StateFactory;
import reader.StringWordReader;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String code = "let x: string;" +
                "x = 'aloha';\n" +
                "print(x);\n" +
                "x = \"alo\" + 'hola';\n" +
                "print(x);\n" +
                "x = 4 + 3 * 2 + 'hola' + 5 * 3 + 2 + x;\n" +
                "print(x);\n" +
                "let y: number = 5 - 4 - 3;\n" +
                "print(y);\n" +
                "print(5 + ', x = ' + x);\n" +
                "print(\"result: \" + 24/y);\n" +
                "y = 5 * 2 + 3 - 2/1 * 3;\n" +
                "print(y);\n" +
                "print('hola');\n" +
                "print(53*4-314);\n" +
                "print(3);";

        StringWordReader reader = new StringWordReader(code);

        Handler errorHandler = new Handler(null, ".*", "Error");
        Handler identifierHandler = new Handler(errorHandler, "_?[a-zA-Z]+[a-zA-Z0-9]*", "Identifier");
        Handler varTypeHandler = new Handler(identifierHandler, "string|number", "VarType");
        Handler keywordHandler = new Handler(varTypeHandler, "let|print", "Keyword");
        Handler stringHandler = new Handler(keywordHandler, "'.+'|\".+\"", "String");
        Handler separatorHandler = new Handler(stringHandler, "[=;:()]", "Separator");
        Handler operatorHandler = new Handler(separatorHandler, "[+*/-]", "Operator");
        Handler numberHandler = new Handler(operatorHandler, "[0-9]+[.]?[0-9]*", "Number");

        Lexer lexer = new LexerImpl(reader, numberHandler);

        LineFileReader lineReader = new LineFileReader();
        lineReader.initialize("C:\\Users\\tomy\\IdeaProjects\\TP4_IdS_Iturralde\\src\\acceptedLines.txt");
        lineReader.readLines();
        List<String> lines = lineReader.getLines();

        StateFactory factory = new StateFactory();
        lines.forEach(factory::createStates);

        Handler emptyHandler = new Handler(null, ".*", "");
        Handler reassignHandler = new Handler(emptyHandler, "IDENTIFIER=EXP;", "REASSIGN");
        Handler printHandler = new Handler(reassignHandler, "print[(]EXP[)];", "PRINT");
        Handler assignHandler = new Handler(printHandler, "letIDENTIFIER:VARTYPE(=EXP)?;", "ASSIGN");

        Parser parser = new ParserImpl(factory.getStartingState(), assignHandler);

        Interpreter interpreter = new InterpreterImpl();

        Compiler compiler = new CompilerImpl(lexer, parser, interpreter);
        compiler.compile();
    }
}
