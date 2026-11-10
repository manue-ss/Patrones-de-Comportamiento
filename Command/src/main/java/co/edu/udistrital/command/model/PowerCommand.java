// Archivo: PowerCommand.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Comando concreto que encapsula la solicitud de potenciacion[cite: 1].
 */
public class PowerCommand extends AbstractArithmeticCommand {

    public PowerCommand(IArithmeticReceiver receiver, double base, double exponent) {
        super(receiver, base, exponent);
    }

    @Override
    public boolean execute() {
        this.saveBackup();
        this.receiver.power(this.operandA, this.operandB);
        return true;
    }

    @Override
    public String getDescription() {
        return String.format("Potenciacion (%.2f ^ %.2f)", this.operandA, this.operandB);
    }
}