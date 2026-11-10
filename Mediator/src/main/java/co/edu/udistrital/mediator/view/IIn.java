// Archivo: IIn.java | Paquete: co.edu.udistrital.mediator.view
package co.edu.udistrital.mediator.view;

/**
 * Interfaz que define el contrato de entrada de datos para aislar el sistema del mecanismo de captura.
 */
public interface IIn {

    /**
     * Captura una línea de texto desde el medio de entrada configurado.
     *
     * @param message Mensaje de solicitud para el usuario.
     * @return Cadena de texto ingresada.
     */
    String readString(String message);

    /**
     * Captura un valor entero desde el medio de entrada configurado.
     *
     * @param message Mensaje de solicitud para el usuario.
     * @return Valor entero ingresado.
     */
    int readInt(String message);

    /**
     * Cierra el recurso de entrada subyacente de forma segura.
     */
    void close();
}