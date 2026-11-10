// Archivo: IArithmeticReceiver.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Contrato para la clase Receptora que contiene la logica de negocio aritmetica[cite: 1].
 */
public interface IArithmeticReceiver {

    double add(double operandA, double operandB);

    double subtract(double operandA, double operandB);

    double multiply(double operandA, double operandB);

    double divide(double operandA, double operandB);

    double power(double base, double exponent);

    double getCurrentState();

    void setCurrentState(double state);
}