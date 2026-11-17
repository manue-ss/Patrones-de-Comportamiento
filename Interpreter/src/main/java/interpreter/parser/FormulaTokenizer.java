package interpreter.parser;

import java.util.ArrayList;
import java.util.List;

public class FormulaTokenizer {
    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        while (i < input.length()) {
            char c = input.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
            } else if (c == '+' || c == '-' || c == '*') {
                tokens.add(new Token(String.valueOf(c), i));
                i++;
            } else if (Character.isLetterOrDigit(c)) {
                int start = i;
                while (i < input.length() && Character.isLetterOrDigit(input.charAt(i))) {
                    i++;
                }
                tokens.add(new Token(input.substring(start, i), start));
            } else {
                throw new IllegalArgumentException("Caracter invalido '" + c + "' en posicion " + i);
            }
        }
        return tokens;
    }
}
