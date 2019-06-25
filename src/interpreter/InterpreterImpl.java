package interpreter;

import parser.ASTNode;
import java.util.ArrayList;
import java.util.List;

public class InterpreterImpl implements Interpreter {
    private List<Variable> variables;

    public InterpreterImpl() {
        variables = new ArrayList<>();
    }

    @Override
    public void readASTTree(ASTNode root) {
        List<ASTNode> lines = root.getSubNodes();
        for (ASTNode line : lines) {
            switch (line.getType()){
                case "ASSIGN":
                    interpretAssignment(line);
                    break;
                case "REASSIGN":
                    interpretReassignment(line);
                    break;
                case "PRINT":
                    interpretPrint(line);
                    break;
            }
        }
    }

    private void interpretAssignment(ASTNode node) {
        Variable newVar = new Variable();
        Value val = new Value("");
        newVar.setValue(val);

        for (ASTNode subNode : node.getSubNodes()) {
            switch (subNode.getType()) {
                case "IDENTIFIER":
                    newVar.setId(subNode.getToken().getText());
                    for (Variable variable : variables) {
                        if (variable.getId().equals(newVar.getId()))
                            throw new RuntimeException("cannot redeclare variable,\nproblem with token:" + subNode.getToken().toString());
                    }
                    break;
                case "VARTYPE":
                    val.setType(subNode.getToken().getText());
                    break;
                case "EXP":
                    Value newValue = interpretExpression(subNode.getLastNode());
                    if (!newValue.getType().equals(val.getType()))
                        throw new RuntimeException("Type mismatch");
                    newVar.setValue(newValue);
                    break;
            }
        }
        variables.add(newVar);
    }

    private void interpretReassignment(ASTNode node) {
        Variable v = new Variable();
        for (ASTNode subNode : node.getSubNodes()) {
            switch (subNode.getType()) {
                case "IDENTIFIER":
                    boolean varNotFound = true;
                    for (Variable variable : variables) {
                        if (variable.getId().equals(subNode.getToken().getText())) {
                            v = variable;
                            varNotFound = false;
                        }
                    }
                    if (varNotFound)
                        throw new RuntimeException("cannot find variable,\nproblem with token:" + subNode.getToken().toString());
                    break;
                case "EXP":
                    Value newValue = interpretExpression(subNode.getLastNode());
                    if (!newValue.getType().equals(v.getValue().getType()))
                        throw new RuntimeException("Type mismatch");
                    v.setValue(newValue);
                    break;
            }
        }
    }

    private void interpretPrint(ASTNode node) {
        for (ASTNode subNode : node.getSubNodes()) {
            if (subNode.getType().equals("EXP")) {
                System.out.println(interpretExpression(subNode.getLastNode()).getTerm());
            }
        }
    }

    private Value interpretExpression(ASTNode expression) {
        if (expression.getSubNodes().isEmpty()) {
            Value newValue = new Value("");
            if (expression.getType().equals("STRING_LITERAL")) {
                String val = expression.getToken().getText().substring(1, expression.getToken().getText().length() - 1);
                newValue.setStringTerm(val);
                newValue.setType(expression.getToken().getType().toLowerCase());
            }
            else if (expression.getType().equals("NUMBER_LITERAL")) {
                newValue.setNumberTerm(Double.parseDouble(expression.getToken().getText()));
                newValue.setType(expression.getToken().getType().toLowerCase());
            }
            else {
                for (Variable var : variables) {
                    if (var.getId().equals(expression.getToken().getText()) && var.getValue().getTerm() != null) {
                        return var.getValue();
                    }
                }
                throw new RuntimeException("Identifier not initialized");
            }
            return newValue;
        } else {
            Value leftValue = interpretExpression(expression.getSubNodes().get(0));
            Value rightValue = interpretExpression(expression.getSubNodes().get(1));

            if (leftValue.getType().equals(rightValue.getType()) && leftValue.getType().equals("number")) {
                Value newValue = new Value("number");
                switch (expression.getType()) {
                    case "+":
                        newValue.setNumberTerm((Double) leftValue.getTerm() + (Double) rightValue.getTerm());
                        break;
                    case "-":
                        newValue.setNumberTerm((Double) leftValue.getTerm() - (Double) rightValue.getTerm());
                        break;
                    case "*":
                        newValue.setNumberTerm((Double) leftValue.getTerm() * (Double) rightValue.getTerm());
                        break;
                    case "/":
                        newValue.setNumberTerm((Double) leftValue.getTerm() / (Double) rightValue.getTerm());
                        break;
                }
                return newValue;
            }
            else {
                if (expression.getType().equals("+")) {
                    Value newValue = new Value("string");
                    if (leftValue.getType().equals("number"))
                        leftValue.setStringTerm(Double.toString((Double)leftValue.getTerm()));
                    if (rightValue.getType().equals("number"))
                        rightValue.setStringTerm(Double.toString((Double)rightValue.getTerm()));
                    newValue.setStringTerm(((String)leftValue.getTerm()) + rightValue.getTerm());
                    return newValue;
                }
                throw new RuntimeException("Only + concatenation is possible between different types");
            }
        }
    }
}
