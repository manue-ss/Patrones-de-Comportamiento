// Archivo: IIn.java | Paquete: co.edu.udistrital.command.view
package co.edu.udistrital.command.view;

/**
 * Contrato de abstraccion para la captura de datos de entrada[cite: 4, 11].
 */
public interface IIn {

    String readString();

    double readDouble();

    int readInt();
}