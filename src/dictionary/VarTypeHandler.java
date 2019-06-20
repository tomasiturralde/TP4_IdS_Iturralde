package dictionary;

import java.util.regex.Pattern;

public class VarTypeHandler implements Dictionary {
    private Dictionary next;

    VarTypeHandler() {
        next = new IdentifierHandler();
    }

    @Override
    public String checkType(String text) {
        if (Pattern.matches("string|number", text)) {
            return "VarType";
        } else {
            return next.checkType(text);
        }
    }
}
