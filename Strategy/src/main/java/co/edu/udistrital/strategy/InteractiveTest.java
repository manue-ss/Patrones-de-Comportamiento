// Archivo: InteractiveTest.java | Paquete: co.edu.udistrital.strategy
package co.edu.udistrital.strategy;

import co.edu.udistrital.strategy.controller.Client;
import co.edu.udistrital.strategy.model.*;
import co.edu.udistrital.strategy.view.*;

public class InteractiveTest {

    public static void main(String[] args) {
        IIn input = new ConsoleIn();
        IOut output = new ConsoleOut();
        ISortContext sortContext = new ArraySort();

        Client client = new Client(sortContext, input, output);

        Algorithm[] allStrategies = {
            new BubbleSort(),
            new SelectionSort(),
            new InsertionSort(),
            new QuickSort(),
            new MergeSort()
        };

        output.printLn("==================================================");
        output.printLn("4. PRUEBA INTERACTIVA POR CONSOLA");
        output.printLn("==================================================");

        client.runInteractiveLoop(allStrategies);
    }
}