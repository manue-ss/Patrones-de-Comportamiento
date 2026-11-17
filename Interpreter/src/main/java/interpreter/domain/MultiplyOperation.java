package interpreter.domain;

public class MultiplyOperation implements FormulaExpression {
    private final FormulaExpression left;
    private final FormulaExpression right;

    public MultiplyOperation(FormulaExpression left, FormulaExpression right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public int interpret(DosageContext context) {
        return left.interpret(context) * right.interpret(context);
    }
}
