package interpreter;

public class Value {
    private String type;
    private Object term;

    Value(String type) {
        this.type = type;
        term = null;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getTerm() {
        return term;
    }

    void setNumberTerm(Double term) {
        this.term = term;
    }

    void setStringTerm(String term) {
        this.term = term;
    }
}
