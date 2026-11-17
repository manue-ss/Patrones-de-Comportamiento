package interpreter.domain;

public class NumberLiteral implements FormulaExpression {
    private final int value;

    public NumberLiteral(int value) {
        this.value = value;
    }

    @Override
    public int interpret(DosageContext context) {
        return value;
    }
}
