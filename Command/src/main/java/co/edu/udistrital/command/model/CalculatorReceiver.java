// Archivo: CalculatorReceiver.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Clase Receptora concreta que ejecuta las operaciones aritmeticas y protege sus invariantes[cite: 1, 12].
 */
public class CalculatorReceiver implements IArithmeticReceiver {

    private double currentState;

    public CalculatorReceiver() {
        this.currentState = 0.0;
    }

    @Override
    public double add(double operandA, double operandB) {
        this.validateFiniteOperands(operandA, operandB);
        double result = operandA + operandB;
        this.validateFiniteResult(result);
        this.currentState = result;
        return result;
    }

    @Override
    public double subtract(double operandA, double operandB) {
        this.validateFiniteOperands(operandA, operandB);
        double result = operandA - operandB;
        this.validateFiniteResult(result);
        this.currentState = result;
        return result;
    }

    @Override
    public double multiply(double operandA, double operandB) {
        this.validateFiniteOperands(operandA, operandB);
        double result = operandA * operandB;
        this.validateFiniteResult(result);
        this.currentState = result;
        return result;
    }

    @Override
    public double divide(double operandA, double operandB) {
        this.validateFiniteOperands(operandA, operandB);
        if (operandB == 0.0) {
            throw new IllegalArgumentException("Invariante violado: No se permite la division por cero.");
        }
        double result = operandA / operandB;
        this.validateFiniteResult(result);
        this.currentState = result;
        return result;
    }

    @Override
    public double power(double base, double exponent) {
        this.validateFiniteOperands(base, exponent);
        if (base == 0.0 && exponent < 0.0) {
            throw new IllegalArgumentException("Invariante violado: Cero elevado a un exponente negativo es indefinido.");
        }
        if (base < 0.0 && exponent != Math.floor(exponent)) {
            throw new IllegalArgumentException("Invariante violado: Base negativa con exponente fraccionario no produce un numero real.");
        }
        double result = Math.pow(base, exponent);
        this.validateFiniteResult(result);
        this.currentState = result;
        return result;
    }

    @Override
    public double getCurrentState() {
        return this.currentState;
    }

    @Override
    public void setCurrentState(double state) {
        if (Double.isNaN(state) || Double.isInfinite(state)) {
            throw new IllegalArgumentException("Estado invalido: El valor debe ser un numero real finito.");
        }
        this.currentState = state;
    }

    private void validateFiniteOperands(double a, double b) {
        if (Double.isNaN(a) || Double.isInfinite(a) || Double.isNaN(b) || Double.isInfinite(b)) {
            throw new IllegalArgumentException("Los operandos de entrada deben ser numeros reales finitos.");
        }
    }

    private void validateFiniteResult(double result) {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new IllegalArgumentException("Desbordamiento matematico: El resultado excede los limites reales finitos.");
        }
    }
}