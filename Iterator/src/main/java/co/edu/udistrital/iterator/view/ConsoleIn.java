// Archivo: ConsoleIn.java | Paquete: co.edu.udistrital.iterator.view
package co.edu.udistrital.iterator.view;

import java.util.Scanner;

/**
 * Implementación concreta de entrada por consola.
 */
public class ConsoleIn implements IIn {

    private final Scanner scanner;

    /**
     * Construye el lector de consola estándar.
     */
    public ConsoleIn() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String nextLine() {
        return scanner.nextLine();
    }

    @Override
    public int nextInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor ingresado no es un número entero válido.");
        }
    }

    @Override
    public double nextDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El valor ingresado no es un número decimal válido.");
        }
    }

    @Override
    public void close() {
        scanner.close();
    }
}
