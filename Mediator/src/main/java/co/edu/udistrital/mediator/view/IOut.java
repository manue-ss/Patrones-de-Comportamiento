// Archivo: IOut.java | Paquete: co.edu.udistrital.mediator.view
package co.edu.udistrital.mediator.view;

/**
 * Interfaz que define el contrato de salida de datos para desacoplar la visualización.
 */
public interface IOut {

    /**
     * Muestra un mensaje estándar al usuario.
     *
     * @param message Contenido del mensaje a visualizar.
     */
    void showMessage(String message);

    /**
     * Muestra un mensaje de error o advertencia al usuario.
     *
     * @param error Contenido del error a visualizar.
     */
    void showError(String error);
}