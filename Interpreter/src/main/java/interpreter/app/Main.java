package interpreter.app;

import interpreter.domain.DosageContext;
import interpreter.domain.FormulaExpression;
import interpreter.io.StandardOutput;
import interpreter.io.TextOutput;
import interpreter.parser.FormulaParser;
import interpreter.parser.FormulaTokenizer;

public class Main {
    public static void main(String[] args) {
        TextOutput out = new StandardOutput();
        FormulaTokenizer tokenizer = new FormulaTokenizer();
        DosageContext context = new DosageContext();
        context.assign("basal", 10);
        context.assign("weight", 15);

        out.emit("[CASO 1]");
        FormulaExpression validTree = new FormulaParser(
                tokenizer.tokenize("basal + weight * 2 - 5")).parse();
        out.emit("DOSIS_MG=" + validTree.interpret(context));

        out.emit("[CASO 2]");
        try {
            new FormulaParser(tokenizer.tokenize("basal + * 2")).parse();
        } catch (IllegalArgumentException ex) {
            out.emit("ERROR: " + ex.getMessage());
        }
    }
}
