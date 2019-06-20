package dictionary;

import java.util.regex.Pattern;

public class OperatorHandler implements Dictionary {
    private Dictionary next;

    OperatorHandler() {
        next = new SeparatorHandler();
    }

    @Override
    public String checkType(String text) {
        if (Pattern.matches("[+*/-]", text)) {
            return "Operator";
        } else {
            return next.checkType(text);
        }
    }
}
