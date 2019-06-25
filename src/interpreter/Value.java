package interpreter;

class Value {
    private String type;
    private Object term;

    Value(String type) {
        this.type = type;
        term = null;
    }

    String getType() {
        return type;
    }

    void setType(String type) {
        this.type = type;
    }

    Object getTerm() {
        return term;
    }

    void setNumberTerm(Double term) {
        this.term = term;
    }

    void setStringTerm(String term) {
        this.term = term;
    }
}
