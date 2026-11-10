// Archivo: SubtractionCommand.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Comando concreto que encapsula la solicitud de resta[cite: 1].
 */
public class SubtractionCommand extends AbstractArithmeticCommand {

    public SubtractionCommand(IArithmeticReceiver receiver, double operandA, double operandB) {
        super(receiver, operandA, operandB);
    }

    @Override
    public boolean execute() {
        this.saveBackup();
        this.receiver.subtract(this.operandA, this.operandB);
        return true;
    }

    @Override
    public String getDescription() {
        return String.format("Resta (%.2f - %.2f)", this.operandA, this.operandB);
    }
}