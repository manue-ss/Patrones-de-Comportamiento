// Archivo: IOut.java | Paquete: co.edu.udistrital.iterator.view
package co.edu.udistrital.iterator.view;

/**
 * Abstracción para la visualización de mensajes estándar y de error.
 */
public interface IOut {

    /**
     * Muestra un mensaje sin salto de línea final.
     *
     * @param message contenido a mostrar
     */
    void show(String message);

    /**
     * Muestra un mensaje con salto de línea final.
     *
     * @param message contenido a mostrar
     */
    void showLine(String message);

    /**
     * Muestra un mensaje de error.
     *
     * @param errorMessage descripción del error
     */
    void showError(String errorMessage);
}
