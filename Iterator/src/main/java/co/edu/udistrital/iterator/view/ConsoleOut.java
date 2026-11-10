// Archivo: ConsoleOut.java | Paquete: co.edu.udistrital.iterator.view
package co.edu.udistrital.iterator.view;

/**
 * Implementación concreta de salida por consola.
 */
public class ConsoleOut implements IOut {

    @Override
    public void show(String message) {
        System.out.print(message);
    }

    @Override
    public void showLine(String message) {
        System.out.println(message);
    }

    @Override
    public void showError(String errorMessage) {
        System.err.println("[ERROR] " + errorMessage);
    }
}
