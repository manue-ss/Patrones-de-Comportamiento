// Archivo: ConsoleOut.java | Paquete: co.edu.udistrital.mediator.view
package co.edu.udistrital.mediator.view;

/**
 * Implementación concreta de salida de datos por consola estándar (System.out / System.err).
 */
public class ConsoleOut implements IOut {

    @Override
    public synchronized void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public synchronized void showError(String error) {
        System.err.println("[ERROR] " + error);
    }
}