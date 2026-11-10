// Archivo: IInvoker.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Contrato para la clase Emisora (Invoker) que activa los comandos y gestiona su historial[cite: 1].
 */
public interface IInvoker {

    void setCommand(ICommand command);

    void executeCommand();

    ICommand undoLastCommand();

    int getHistorySize();
}