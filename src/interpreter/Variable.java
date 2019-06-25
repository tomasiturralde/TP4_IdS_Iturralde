package interpreter;

class Variable {
    private String id;
    private Value value;

    Variable() {
        this.id = null;
        this.value = null;
    }

    String getId() {
        return id;
    }

    Value getValue() {
        return value;
    }

    void setId(String id) {
        this.id = id;
    }

    void setValue(Value value) {
        this.value = value;
    }
}
