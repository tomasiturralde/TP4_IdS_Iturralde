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

    public Value getValue() {
        return value;
    }

    void setId(String id) {
        this.id = id;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
