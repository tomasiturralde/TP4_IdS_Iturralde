package dictionary;

import java.util.regex.Pattern;

public class NumberHandler implements Dictionary {
    private Dictionary next;

    public NumberHandler() {
        next = new OperatorHandler();
    }

    public String checkType(String text) {
        if (Pattern.matches("[0-9]+[.]?[0-9]*", text)) {
            return "Number";
        } else {
            return next.checkType(text);
        }
    }
}
