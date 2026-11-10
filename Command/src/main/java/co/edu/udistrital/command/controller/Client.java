// Archivo: Client.java | Paquete: co.edu.udistrital.command.controller
package co.edu.udistrital.command.controller;

import co.edu.udistrital.command.model.AdditionCommand;
import co.edu.udistrital.command.model.DivisionCommand;
import co.edu.udistrital.command.model.IArithmeticReceiver;
import co.edu.udistrital.command.model.ICommand;
import co.edu.udistrital.command.model.IInvoker;
import co.edu.udistrital.command.model.MultiplicationCommand;
import co.edu.udistrital.command.model.PowerCommand;
import co.edu.udistrital.command.model.SubtractionCommand;
import co.edu.udistrital.command.view.IIn;
import co.edu.udistrital.command.view.IOut;

/**
 * Cliente orquestador que ensambla comandos, delega la ejecucion al Invoker y gestiona la E/S[cite: 1, 11].
 */
public class Client {

    private final IInvoker invoker;
    private final IArithmeticReceiver receiver;
    private final IIn in;
    private final IOut out;

    public Client(IInvoker invoker, IArithmeticReceiver receiver, IIn in, IOut out) {
        if (invoker == null || receiver == null || in == null || out == null) {
            throw new IllegalArgumentException("Las dependencias inyectadas al Cliente no pueden ser nulas.");
        }
        this.invoker = invoker;
        this.receiver = receiver;
        this.in = in;
        this.out = out;
    }

    public void executeCommand(ICommand command) {
        try {
            if (command == null) {
                throw new IllegalArgumentException("El comando solicitado no puede ser nulo.");
            }
            double previousState = this.receiver.getCurrentState();
            this.invoker.setCommand(command);
            this.invoker.executeCommand();
            double newState = this.receiver.getCurrentState();

            this.out.showLine("--------------------------------------------------");
            this.out.showLine("Operacion ejecutada : " + command.getDescription());
            this.out.showLine(String.format("Estado previo       : %.4f", previousState));
            this.out.showLine(String.format("Resultado actual    : %.4f", newState));
            this.out.showLine("Comandos en pila    : " + this.invoker.getHistorySize());
        } catch (IllegalArgumentException e) {
            this.out.showError(e.getMessage());
        }
    }

    public void undoLastOperation() {
        try {
            double stateBeforeUndo = this.receiver.getCurrentState();
            ICommand undoneCommand = this.invoker.undoLastCommand();
            if (undoneCommand == null) {
                this.out.showError("No hay operaciones registradas en el historial para deshacer.");
                return;
            }
            double restoredState = this.receiver.getCurrentState();
            this.out.showLine("--------------------------------------------------");
            this.out.showLine("Deshaciendo comando : " + undoneCommand.getDescription());
            this.out.showLine(String.format("Estado descartado   : %.4f", stateBeforeUndo));
            this.out.showLine(String.format("Estado restaurado   : %.4f", restoredState));
            this.out.showLine("Comandos en pila    : " + this.invoker.getHistorySize());
        } catch (IllegalArgumentException e) {
            this.out.showError(e.getMessage());
        }
    }

    public void showHeader(String title) {
        this.out.showLine("\n==================================================");
        this.out.showLine(" " + title);
        this.out.showLine("==================================================");
    }

    public double requestValidDouble(String prompt) {
        while (true) {
            try {
                this.out.show(prompt);
                return this.in.readDouble();
            } catch (IllegalArgumentException e) {
                this.out.showError(e.getMessage());
            }
        }
    }

    public int requestValidOption(String prompt, int minOption, int maxOption) {
        while (true) {
            try {
                this.out.show(prompt);
                int option = this.in.readInt();
                if (option < minOption || option > maxOption) {
                    this.out.showError("Opcion fuera de rango (" + minOption + " - " + maxOption + ").");
                } else {
                    return option;
                }
            } catch (IllegalArgumentException e) {
                this.out.showError(e.getMessage());
            }
        }
    }

    public void runInteractiveSession() {
        boolean running = true;
        while (running) {
            this.out.showLine("\n--- MENU INTERACTIVO DE COMANDOS ARITMETICOS ---");
            this.out.showLine("1. Sumar (AdditionCommand)");
            this.out.showLine("2. Restar (SubtractionCommand)");
            this.out.showLine("3. Multiplicar (MultiplicationCommand)");
            this.out.showLine("4. Dividir (DivisionCommand)");
            this.out.showLine("5. Potenciar (PowerCommand)");
            this.out.showLine("6. Deshacer ultima operacion (Undo)");
            this.out.showLine("0. Salir");

            int option = this.requestValidOption("Seleccione una opcion: ", 0, 6);
            if (option == 0) {
                this.out.showLine("Finalizando sesion interactiva...");
                running = false;
            } else if (option == 6) {
                this.undoLastOperation();
            } else {
                double numA = this.requestValidDouble("Ingrese el primer operando (o base): ");
                double numB = this.requestValidDouble("Ingrese el segundo operando (o exponente): ");
                ICommand command = this.buildCommandByOption(option, numA, numB);
                this.executeCommand(command);
            }
        }
    }

    private ICommand buildCommandByOption(int option, double numA, double numB) {
        switch (option) {
            case 1:
                return new AdditionCommand(this.receiver, numA, numB);
            case 2:
                return new SubtractionCommand(this.receiver, numA, numB);
            case 3:
                return new MultiplicationCommand(this.receiver, numA, numB);
            case 4:
                return new DivisionCommand(this.receiver, numA, numB);
            case 5:
                return new PowerCommand(this.receiver, numA, numB);
            default:
                throw new IllegalArgumentException("Opcion de comando no reconocida.");
        }
    }
}