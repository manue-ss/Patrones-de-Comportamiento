// Archivo: AdditionCommand.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Comando concreto que encapsula la solicitud de suma[cite: 1].
 */
public class AdditionCommand extends AbstractArithmeticCommand {

    public AdditionCommand(IArithmeticReceiver receiver, double operandA, double operandB) {
        super(receiver, operandA, operandB);
    }

    @Override
    public boolean execute() {
        this.saveBackup();
        this.receiver.add(this.operandA, this.operandB);
        return true;
    }

    @Override
    public String getDescription() {
        return String.format("Suma (%.2f + %.2f)", this.operandA, this.operandB);
    }
}