// Archivo: IOut.java | Paquete: co.edu.udistrital.command.view
package co.edu.udistrital.command.view;

/**
 * Contrato de abstraccion para la visualizacion de mensajes estandar y de error[cite: 4, 11].
 */
public interface IOut {

    void show(String message);

    void showLine(String message);

    void showError(String errorMessage);
}