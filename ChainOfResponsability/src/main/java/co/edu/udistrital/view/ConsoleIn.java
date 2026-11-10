// package co.edu.udistrital.view; | Archivo: ConsoleIn.java
package co.edu.udistrital.view;

import java.util.Scanner;

/**
 * Implementación concreta para la captura de datos utilizando Scanner.
 * Única clase autorizada para gestionar la lectura de flujos de entrada.
 */
public class ConsoleIn implements IIn {

    private final Scanner scanner;

    /**
     * Constructor por defecto que enlaza la lectura a la entrada estándar del sistema.
     */
    public ConsoleIn() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Constructor sobrecargado que permite inyectar entradas simuladas para pruebas atómicas.
     *
     * @param simulatedInput Cadena con las entradas de prueba separadas por saltos de línea.
     */
    public ConsoleIn(String simulatedInput) {
        this.scanner = new Scanner(simulatedInput);
    }

    @Override
    public String readLine() {
        if (this.scanner.hasNextLine()) {
            return this.scanner.nextLine();
        }
        return "";
    }

    @Override
    public boolean hasNextLine() {
        return this.scanner.hasNextLine();
    }

    @Override
    public void close() {
        this.scanner.close();
    }
}