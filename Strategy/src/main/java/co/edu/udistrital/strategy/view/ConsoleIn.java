package co.edu.udistrital.strategy.view;

import java.util.Scanner;

public class ConsoleIn implements IIn {
    private final Scanner scanner;

    public ConsoleIn() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public String readLine() {
        return this.scanner.nextLine();
    }
}