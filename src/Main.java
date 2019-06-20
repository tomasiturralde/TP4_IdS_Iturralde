import dictionary.NumberHandler;
import lexer.LexerImpl;
import lexer.ObservableLexer;
import observer.Observer;
import parser.ParserImpl;
import reader.StringWordReader;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Observer parser = new ParserImpl();
        List<Observer> observers = new ArrayList<>();
        observers.add(parser);
        StringWordReader reader = new StringWordReader("let x: string = \"hello world\";\nlet y: number = 5.42a + 2;\nprint('henlo world');");

        ObservableLexer lexer = new LexerImpl(observers, reader, new NumberHandler());

        while (!reader.reachedEnd()) {
            lexer.readNext();
            System.out.println();
        }
    }
}
