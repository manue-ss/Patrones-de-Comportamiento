// Archivo: AbstractArithmeticCommand.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Clase base abstracta para los comandos aritmeticos que encapsula parametros y estado de respaldo[cite: 1, 5].
 */
public abstract class AbstractArithmeticCommand implements ICommand {

    protected final IArithmeticReceiver receiver;
    protected final double operandA;
    protected final double operandB;
    protected double backupState;

    protected AbstractArithmeticCommand(IArithmeticReceiver receiver, double operandA, double operandB) {
        if (receiver == null) {
            throw new IllegalArgumentException("La referencia al receptor aritmetico no puede ser nula.");
        }
        this.receiver = receiver;
        this.operandA = operandA;
        this.operandB = operandB;
        this.backupState = 0.0;
    }

    protected void saveBackup() {
        this.backupState = this.receiver.getCurrentState();
    }

    @Override
    public void undo() {
        this.receiver.setCurrentState(this.backupState);
    }
}