// Archivo: ConsoleOut.java | Paquete: co.edu.udistrital.state.view
package co.edu.udistrital.state.view;

/**
 * Implementación concreta de {@link IOut} para la visualización de mensajes por consola.
 */
public class ConsoleOut implements IOut {

    @Override
    public void show(String message) {
        System.out.println(message);
    }

    @Override
    public void showErr(String errorMessage) {
        System.err.println("[ERROR] " + errorMessage);
    }
}
