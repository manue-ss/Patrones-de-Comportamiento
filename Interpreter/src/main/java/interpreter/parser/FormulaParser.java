package interpreter.parser;

import interpreter.domain.AddOperation;
import interpreter.domain.FormulaExpression;
import interpreter.domain.MultiplyOperation;
import interpreter.domain.NumberLiteral;
import interpreter.domain.ReagentVariable;
import interpreter.domain.SubtractOperation;
import java.util.List;

public class FormulaParser {
    private final List<Token> tokens;
    private int index = 0;

    public FormulaParser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public FormulaExpression parse() {
        FormulaExpression expr = parseExpression();
        if (index < tokens.size()) {
            Token t = tokens.get(index);
            throw new IllegalArgumentException("Token inesperado '" + t.lexeme() + "' en posicion " + t.position());
        }
        return expr;
    }

    private FormulaExpression parseExpression() {
        FormulaExpression left = parseTerm();
        while (index < tokens.size() && (peek().equals("+") || peek().equals("-"))) {
            String op = tokens.get(index++).lexeme();
            FormulaExpression right = parseTerm();
            left = op.equals("+") ? new AddOperation(left, right) : new SubtractOperation(left, right);
        }
        return left;
    }

    private FormulaExpression parseTerm() {
        FormulaExpression left = parseFactor();
        while (index < tokens.size() && peek().equals("*")) {
            index++;
            left = new MultiplyOperation(left, parseFactor());
        }
        return left;
    }

    private FormulaExpression parseFactor() {
        if (index >= tokens.size()) {
            throw new IllegalArgumentException("Fin inesperado de la formula");
        }
        Token t = tokens.get(index++);
        if (Character.isDigit(t.lexeme().charAt(0))) {
            return new NumberLiteral(Integer.parseInt(t.lexeme()));
        }
        if (Character.isLetter(t.lexeme().charAt(0))) {
            return new ReagentVariable(t.lexeme());
        }
        throw new IllegalArgumentException("Token inesperado '" + t.lexeme() + "' en posicion " + t.position());
    }

    private String peek() {
        return tokens.get(index).lexeme();
    }
}
