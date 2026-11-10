// Archivo: CalculatorInvoker.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Clase Emisora concreta que dispara comandos desacoplados del receptor y registra el historial[cite: 1, 7].
 */
public class CalculatorInvoker implements IInvoker {

    private ICommand currentCommand;
    private final ICommandHistory history;

    public CalculatorInvoker(ICommandHistory history) {
        if (history == null) {
            throw new IllegalArgumentException("El historial de comandos no puede ser nulo.");
        }
        this.history = history;
    }

    @Override
    public void setCommand(ICommand command) {
        if (command == null) {
            throw new IllegalArgumentException("El comando asignado al invocador no puede ser nulo.");
        }
        this.currentCommand = command;
    }

    @Override
    public void executeCommand() {
        if (this.currentCommand == null) {
            throw new IllegalArgumentException("No se ha configurado ningun comando para ejecutar.");
        }
        if (this.currentCommand.execute()) {
            this.history.push(this.currentCommand);
        }
    }

    @Override
    public ICommand undoLastCommand() {
        ICommand lastCommand = this.history.pop();
        if (lastCommand != null) {
            lastCommand.undo();
        }
        return lastCommand;
    }

    @Override
    public int getHistorySize() {
        return this.history.size();
    }
}