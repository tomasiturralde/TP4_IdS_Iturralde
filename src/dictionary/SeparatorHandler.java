package dictionary;

import java.util.regex.Pattern;

public class SeparatorHandler implements Dictionary {
    private Dictionary next;

    SeparatorHandler() {
        next = new StringHandler();
    }

    @Override
    public String checkType(String text) {
        if (Pattern.matches("[=;:()]", text)) {
            return "Separator";
        } else {
            return next.checkType(text);
        }
    }
}
