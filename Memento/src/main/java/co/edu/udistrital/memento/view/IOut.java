// Archivo: IOut.java | Paquete: co.edu.udistrital.memento.view
package co.edu.udistrital.memento.view;

/**
 * Interfaz que abstrae las operaciones de salida de datos.
 */
public interface IOut {

    /**
     * Muestra un mensaje estándar al usuario.
     *
     * @param message Texto a mostrar.
     */
    void show(String message);

    /**
     * Muestra un mensaje de error al usuario.
     *
     * @param errorMessage Texto de error a mostrar.
     */
    void showErr(String errorMessage);
}
