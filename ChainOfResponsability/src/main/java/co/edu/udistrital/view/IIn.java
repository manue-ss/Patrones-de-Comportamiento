// package co.edu.udistrital.view; | Archivo: IIn.java
package co.edu.udistrital.view;

/**
 * Interfaz que define el contrato para la captura de datos de entrada.
 * Aplica el Principio de Inversión de Dependencias (DIP) y Responsabilidad Única (SRP).
 */
public interface IIn {

    /**
     * Captura una línea de texto ingresada por el usuario.
     *
     * @return Cadena de texto capturada.
     */
    String readLine();

    /**
     * Verifica si existen más líneas disponibles en el flujo de entrada.
     *
     * @return true si hay datos por leer, false en caso contrario.
     */
    boolean hasNextLine();

    /**
     * Libera el recurso de lectura asociado.
     */
    void close();
}