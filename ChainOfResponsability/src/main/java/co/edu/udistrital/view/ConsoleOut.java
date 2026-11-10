// package co.edu.udistrital.view; | Archivo: ConsoleOut.java
package co.edu.udistrital.view;

/**
 * Implementación concreta para la visualización de mensajes por consola estándar.
 * Única clase autorizada para invocar System.out dentro de la arquitectura.
 */
public class ConsoleOut implements IOut {

    /**
     * Constructor por defecto para el impresor de consola.
     */
    public ConsoleOut() {
        // Inicialización sin estado requerido
    }

    @Override
    public void printLine(String msg) {
        System.out.println(msg);
    }

    @Override
    public void print(String msg) {
        System.out.print(msg);
    }
}