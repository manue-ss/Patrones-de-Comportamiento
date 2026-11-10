// Archivo: ConsoleIn.java | Paquete: co.edu.udistrital.mediator.view
package co.edu.udistrital.mediator.view;

import java.util.Scanner;

/**
 * Implementación concreta de entrada de datos por consola estándar usando Scanner.
 */
public class ConsoleIn implements IIn {

    private final Scanner scanner;

    /**
     * Constructor que inicializa el lector de flujo estándar.
     */
    public ConsoleIn() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String readString(String message) {
        if (message != null && !message.isBlank()) {
            System.out.print(message + " ");
        }
        if (scanner.hasNextLine()) {
            return scanner.nextLine().trim();
        }
        return "";
    }

    @Override
    public int readInt(String message) {
        String rawInput = readString(message);
        return Integer.parseInt(rawInput);
    }

    @Override
    public void close() {
        scanner.close();
    }
}