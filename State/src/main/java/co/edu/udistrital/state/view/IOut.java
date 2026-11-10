// Archivo: IOut.java | Paquete: co.edu.udistrital.state.view
package co.edu.udistrital.state.view;

/**
 * Interfaz que abstrae las operaciones de salida y visualización de mensajes.
 */
public interface IOut {

    /**
     * Muestra un mensaje estándar al usuario.
     *
     * @param message Texto informativo a mostrar.
     */
    void show(String message);

    /**
     * Muestra un mensaje de error al usuario.
     *
     * @param errorMessage Texto de error a mostrar.
     */
    void showErr(String errorMessage);
}
