package interpreter.domain;

public class AddOperation implements FormulaExpression {
    private final FormulaExpression left;
    private final FormulaExpression right;

    public AddOperation(FormulaExpression left, FormulaExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret(DosageContext context) {
        return left.interpret(context) + right.interpret(context);
    }
}
