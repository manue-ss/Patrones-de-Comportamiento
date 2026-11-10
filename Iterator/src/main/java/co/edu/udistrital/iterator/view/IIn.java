// Archivo: IIn.java | Paquete: co.edu.udistrital.iterator.view
package co.edu.udistrital.iterator.view;

/**
 * Abstracción para la captura de datos de entrada.
 */
public interface IIn {

    /**
     * Captura una cadena de texto desde la entrada.
     *
     * @return texto ingresado por el usuario
     */
    String nextLine();

    /**
     * Captura un número entero desde la entrada.
     *
     * @return entero ingresado por el usuario
     */
    int nextInt();

    /**
     * Captura un número decimal desde la entrada.
     *
     * @return decimal ingresado por el usuario
     */
    double nextDouble();

    /**
     * Libera el recurso de entrada subyacente.
     */
    void close();
}
