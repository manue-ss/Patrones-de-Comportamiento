// Archivo: EdgeCasesTest.java | Paquete: co.edu.udistrital.strategy
package co.edu.udistrital.strategy;

import co.edu.udistrital.strategy.controller.Client;
import co.edu.udistrital.strategy.model.*;
import co.edu.udistrital.strategy.view.*;

public class EdgeCasesTest {

    public static void main(String[] args) {
        IIn input = new ConsoleIn();
        IOut output = new ConsoleOut();
        ISortContext sortContext = new ArraySort();

        Client client = new Client(sortContext, input, output);

        Algorithm bubbleSort = new BubbleSort();
        Algorithm selectionSort = new SelectionSort();
        Algorithm insertionSort = new InsertionSort();
        Algorithm quickSort = new QuickSort();
        Algorithm mergeSort = new MergeSort();

        output.printLn("==================================================");
        output.printLn("3. PRUEBAS DE CASOS LIMITE (EDGE CASES / FALLOS)");
        output.printLn("==================================================");

        // Caso límite 1: Arreglo nulo
        client.executeSort(bubbleSort, null);

        // Caso límite 2: Arreglo vacío
        Integer[] emptyArray = {};
        client.executeSort(selectionSort, emptyArray);

        // Caso límite 3: Estrategia nula inyectada
        Integer[] validSmallArray = {3, 1, 2};
        client.executeSort(null, validSmallArray);

        // Caso límite 4: Elementos que no implementan Comparable
        Object[] uncomparableData = {new Object(), new Object()};
        client.executeSort(insertionSort, uncomparableData);

        // Caso límite 5: Tipos mutuamente incompatibles en el mismo arreglo
        Object[] mixedTypesData = {10, "TextoIncompatible", 5.5};
        client.executeSort(quickSort, mixedTypesData);

        // Caso límite 6: Arreglo con elementos nulos intercalados
        Integer[] arrayWithNulls = {10, null, 5};
        client.executeSort(mergeSort, arrayWithNulls);
    }
}