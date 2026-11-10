// Archivo: IIn.java | Paquete: co.edu.udistrital.memento.view
package co.edu.udistrital.memento.view;

/**
 * Interfaz que abstrae las operaciones de captura de datos.
 */
public interface IIn {

    /**
     * Captura una cadena de texto ingresada por el usuario.
     *
     * @return Cadena leída.
     */
    String nextLine();

    /**
     * Captura un número entero ingresado por el usuario.
     *
     * @return Entero leído.
     */
    int nextInt();

    /**
     * Captura un número decimal ingresado por el usuario.
     *
     * @return Decimal leído.
     */
    double nextDouble();
}
