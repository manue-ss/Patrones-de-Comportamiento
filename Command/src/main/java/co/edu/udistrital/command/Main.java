// Archivo: Main.java | Paquete: co.edu.udistrital.command
package co.edu.udistrital.command;

import co.edu.udistrital.command.controller.Client;
import co.edu.udistrital.command.model.AdditionCommand;
import co.edu.udistrital.command.model.CalculatorInvoker;
import co.edu.udistrital.command.model.CalculatorReceiver;
import co.edu.udistrital.command.model.CommandHistory;
import co.edu.udistrital.command.model.DivisionCommand;
import co.edu.udistrital.command.model.IArithmeticReceiver;
import co.edu.udistrital.command.model.ICommandHistory;
import co.edu.udistrital.command.model.IInvoker;
import co.edu.udistrital.command.model.MultiplicationCommand;
import co.edu.udistrital.command.model.PowerCommand;
import co.edu.udistrital.command.model.SubtractionCommand;
import co.edu.udistrital.command.view.ConsoleIn;
import co.edu.udistrital.command.view.ConsoleOut;
import co.edu.udistrital.command.view.IIn;
import co.edu.udistrital.command.view.IOut;

/**
 * Raiz de Composicion (Composition Root) que ensambla las dependencias y ejecuta la progresion de pruebas[cite: 1, 11].
 */
public class Main {

    public static void main(String[] args) {
        // 1. Instanciacion de las abstracciones de E/S
        IIn in = new ConsoleIn();
        IOut out = new ConsoleOut();

        // 2. Instanciacion de los componentes del patron Command en el Modelo
        IArithmeticReceiver receiver = new CalculatorReceiver();
        ICommandHistory history = new CommandHistory();
        IInvoker invoker = new CalculatorInvoker(history);

        // 3. Ensamblaje del Cliente Orquestador mediante Inyeccion de Dependencias
        Client client = new Client(invoker, receiver, in, out);

        // 4. Fase 1: Pruebas Alfa / Deterministas (Happy Paths de las 5 operaciones e Historial Undo)
        client.showHeader("FASE 1: PRUEBAS DETERMINISTAS (HAPPY PATHS)");
        client.executeCommand(new AdditionCommand(receiver, 15.0, 25.0));
        client.executeCommand(new SubtractionCommand(receiver, 40.0, 12.5));
        client.executeCommand(new MultiplicationCommand(receiver, 27.5, 4.0));
        client.executeCommand(new DivisionCommand(receiver, 110.0, 5.0));
        client.executeCommand(new PowerCommand(receiver, 2.0, 8.0));

        client.showHeader("PRUEBA DE REVERSION DE HISTORIAL (UNDO)");
        client.undoLastOperation();
        client.undoLastOperation();

        // 5. Fase 2: Pruebas de Validacion de Invariantes y Casos Limite
        client.showHeader("FASE 2: PRUEBAS DE VALIDACION Y CASOS LIMITE");
        client.executeCommand(new DivisionCommand(receiver, 50.0, 0.0));
        client.executeCommand(new PowerCommand(receiver, 0.0, -3.0));
        client.executeCommand(new PowerCommand(receiver, -4.0, 0.5));
        client.executeCommand(null);

        // 6. Fase 3: Interaccion con el usuario desde la Vista
        client.showHeader("FASE 3: PRUEBA INTERACTIVA POR CONSOLA");
        client.runInteractiveSession();
    }
}