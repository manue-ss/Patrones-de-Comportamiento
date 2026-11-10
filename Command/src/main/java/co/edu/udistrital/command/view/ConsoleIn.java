// Archivo: ConsoleIn.java | Paquete: co.edu.udistrital.command.view
package co.edu.udistrital.command.view;

import java.util.Scanner;

/**
 * Implementacion concreta de entrada de datos por consola estandar[cite: 4, 11].
 */
public class ConsoleIn implements IIn {

    private final Scanner scanner;

    public ConsoleIn() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String readString() {
        return this.scanner.nextLine().trim();
    }

    @Override
    public double readDouble() {
        String rawInput = this.readString();
        try {
            return Double.parseDouble(rawInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato numerico decimal invalido: '" + rawInput + "'.");
        }
    }

    @Override
    public int readInt() {
        String rawInput = this.readString();
        try {
            return Integer.parseInt(rawInput);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato numerico entero invalido: '" + rawInput + "'.");
        }
    }
}