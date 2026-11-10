// Archivo: MultiplicationCommand.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Comando concreto que encapsula la solicitud de multiplicacion[cite: 1].
 */
public class MultiplicationCommand extends AbstractArithmeticCommand {

    public MultiplicationCommand(IArithmeticReceiver receiver, double operandA, double operandB) {
        super(receiver, operandA, operandB);
    }

    @Override
    public boolean execute() {
        this.saveBackup();
        this.receiver.multiply(this.operandA, this.operandB);
        return true;
    }

    @Override
    public String getDescription() {
        return String.format("Multiplicacion (%.2f * %.2f)", this.operandA, this.operandB);
    }
}