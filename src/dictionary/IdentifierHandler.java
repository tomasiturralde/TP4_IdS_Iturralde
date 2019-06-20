package dictionary;

import java.util.regex.Pattern;

public class IdentifierHandler implements Dictionary {
    private Dictionary next;

    IdentifierHandler() {
        next = new ErrorHandler();
    }

    @Override
    public String checkType(String text) {
        if (Pattern.matches("_?[a-zA-Z]+[a-zA-Z0-9]*", text)) {
            return "Identifier";
        }
        else {
            return next.checkType(text);
        }
    }
}
