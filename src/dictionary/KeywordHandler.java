package dictionary;

import java.util.regex.Pattern;

public class KeywordHandler implements Dictionary {
    private Dictionary next;

    KeywordHandler() {
        next = new VarTypeHandler();
    }

    @Override
    public String checkType(String text) {
        if (Pattern.matches("let|print", text)) {
            return "Keyword";
        } else {
            return next.checkType(text);
        }
    }
}
