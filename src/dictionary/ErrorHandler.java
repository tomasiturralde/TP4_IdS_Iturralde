package dictionary;

public class ErrorHandler implements Dictionary {
    ErrorHandler() {}

    @Override
    public String checkType(String text) {
        return "Error";
    }
}
