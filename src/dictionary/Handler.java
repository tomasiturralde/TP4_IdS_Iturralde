package dictionary;

public class Handler implements Dictionary {
    private Dictionary next;
    private String pattern;
    private String returnType;

    public Handler(Dictionary next, String pattern, String returnType) {
        this.next = next;
        this.pattern = pattern;
        this.returnType = returnType;
    }

    @Override
    public String checkType(String text) {
        if (text.matches(pattern)) {
            return returnType;
        } else {
            return next.checkType(text);
        }
    }
}
