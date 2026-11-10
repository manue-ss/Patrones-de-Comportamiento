// Archivo: DivisionCommand.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Comando concreto que encapsula la solicitud de division[cite: 1].
 */
public class DivisionCommand extends AbstractArithmeticCommand {

    public DivisionCommand(IArithmeticReceiver receiver, double operandA, double operandB) {
        super(receiver, operandA, operandB);
    }

    @Override
    public boolean execute() {
        this.saveBackup();
        this.receiver.divide(this.operandA, this.operandB);
        return true;
    }

    @Override
    public String getDescription() {
        return String.format("Division (%.2f / %.2f)", this.operandA, this.operandB);
    }
}