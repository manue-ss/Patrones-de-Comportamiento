// Archivo: CommandHistory.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Implementacion de la pila de historial de comandos para soportar operaciones reversibles[cite: 1, 7].
 */
public class CommandHistory implements ICommandHistory {

    private final Deque<ICommand> historyStack;

    public CommandHistory() {
        this.historyStack = new ArrayDeque<>();
    }

    @Override
    public void push(ICommand command) {
        if (command == null) {
            throw new IllegalArgumentException("No se puede registrar un comando nulo en el historial.");
        }
        this.historyStack.push(command);
    }

    @Override
    public ICommand pop() {
        if (this.historyStack.isEmpty()) {
            return null;
        }
        return this.historyStack.pop();
    }

    @Override
    public boolean isEmpty() {
        return this.historyStack.isEmpty();
    }

    @Override
    public int size() {
        return this.historyStack.size();
    }
}