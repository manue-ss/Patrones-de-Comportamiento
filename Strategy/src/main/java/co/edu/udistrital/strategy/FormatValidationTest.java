// Archivo: FormatValidationTest.java | Paquete: co.edu.udistrital.strategy
package co.edu.udistrital.strategy;

import co.edu.udistrital.strategy.controller.Client;
import co.edu.udistrital.strategy.model.*;
import co.edu.udistrital.strategy.view.*;

public class FormatValidationTest {

    public static void main(String[] args) {
        IIn input = new ConsoleIn();
        IOut output = new ConsoleOut();
        ISortContext sortContext = new ArraySort();

        Client client = new Client(sortContext, input, output);

        Algorithm bubbleSort = new BubbleSort();
        Algorithm quickSort = new QuickSort();
        Algorithm mergeSort = new MergeSort();

        output.printLn("==================================================");
        output.printLn("2. PRUEBAS DE PARSEO Y VALIDACION DE FORMATO");
        output.printLn("==================================================");

        // Caso 1: Cadena válida con números enteros positivos, negativos y cero
        client.executeFromRawInput(mergeSort, "100, -5, 42, 0, 18, 7");

        // Caso 2: Cadena con token alfanumérico inválido
        client.executeFromRawInput(quickSort, "15, 20, texto_invalido, 9");

        // Caso 3: Cadena compuesta únicamente por espacios en blanco
        client.executeFromRawInput(bubbleSort, "   ");
    }
}