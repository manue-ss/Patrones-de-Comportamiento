package interpreter.domain;

import java.util.HashMap;
import java.util.Map;

public class DosageContext {
    private final Map<String, Integer> variables = new HashMap<>();

    public void assign(String name, int valueMg) {
        variables.put(name, valueMg);
    }

    public int resolve(String name) {
        Integer val = variables.get(name);
        if (val == null) {
            throw new IllegalArgumentException("Reactivo no definido: " + name);
        }
        return val;
    }
}
