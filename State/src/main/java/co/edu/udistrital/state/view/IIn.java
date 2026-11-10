// Archivo: IIn.java | Paquete: co.edu.udistrital.state.view
package co.edu.udistrital.state.view;

/**
 * Interfaz que abstrae las operaciones de captura de datos del usuario.
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
