// Archivo: ICommand.java | Paquete: co.edu.udistrital.command.model
package co.edu.udistrital.command.model;

/**
 * Interfaz Command que declara el contrato de ejecucion sin parametros y reversion[cite: 1, 7].
 */
public interface ICommand {

    boolean execute();

    void undo();

    String getDescription();
}