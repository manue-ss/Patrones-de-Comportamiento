// package co.edu.udistrital.view; | Archivo: IOut.java
package co.edu.udistrital.view;

/**
 * Interfaz que define el contrato para la salida de mensajes hacia el usuario.
 * Aplica el Principio de Inversión de Dependencias (DIP) y Responsabilidad Única (SRP).
 */
public interface IOut {

    /**
     * Muestra un mensaje con salto de línea.
     *
     * @param msg Texto en español a mostrar en la salida.
     */
    void printLine(String msg);

    /**
     * Muestra un mensaje sin salto de línea (útil para solicitar datos).
     *
     * @param msg Texto en español a mostrar antes de la lectura.
     */
    void print(String msg);
}