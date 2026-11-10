// Archivo: ICommandHistory.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Contrato para la pila de historial de comandos ejecutados[cite: 1, 7].
 */
public interface ICommandHistory {

    void push(ICommand command);

    ICommand pop();

    boolean isEmpty();

    int size();
}