package interpreter.domain;

public class SubtractOperation implements FormulaExpression {
    private final FormulaExpression left;
    private final FormulaExpression right;

    public SubtractOperation(FormulaExpression left, FormulaExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret(DosageContext context) {
        return left.interpret(context) - right.interpret(context);
    }
}
