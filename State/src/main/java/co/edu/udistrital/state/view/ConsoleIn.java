// Archivo: ConsoleIn.java | Paquete: co.edu.udistrital.state.view
package co.edu.udistrital.state.view;

import java.util.Scanner;

/**
 * Implementación concreta de {@link IIn} para la captura de datos desde la entrada estándar.
 */
public class ConsoleIn implements IIn {

    private final Scanner scanner;

    public ConsoleIn() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    @Override
    public int nextInt() {
        return Integer.parseInt(scanner.nextLine().trim());
    }

    @Override
    public double nextDouble() {
        return Double.parseDouble(scanner.nextLine().trim());
    }
}
