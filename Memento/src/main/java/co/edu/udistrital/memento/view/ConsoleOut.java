// Archivo: ConsoleOut.java | Paquete: co.edu.udistrital.memento.view
package co.edu.udistrital.memento.view;

/**
 * Implementación concreta de salida de datos por consola.
 */
public class ConsoleOut implements IOut {

    @Override
    public void show(String message) {
        System.out.println(message);
    }

    @Override
    public void showErr(String errorMessage) {
        System.err.println("[ERROR]: " + errorMessage);
    }
}
