package dictionary;


import java.util.regex.Pattern;

public class StringHandler implements Dictionary {
    private Dictionary next;

    StringHandler() {
        next = new KeywordHandler();
    }

    @Override
    public String checkType(String text) {
        if (Pattern.matches("'.+'", text) || Pattern.matches("\".+\"", text)) {
            return "String";
        } else {
            return next.checkType(text);
        }
    }
}
