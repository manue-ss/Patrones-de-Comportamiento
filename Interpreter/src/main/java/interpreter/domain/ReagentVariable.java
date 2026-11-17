package interpreter.domain;

public class ReagentVariable implements FormulaExpression {
    private final String name;

    public ReagentVariable(String name) {
        this.name = name;
    }

    @Override
    public int interpret(DosageContext context) {
        return context.resolve(name);
    }
}
