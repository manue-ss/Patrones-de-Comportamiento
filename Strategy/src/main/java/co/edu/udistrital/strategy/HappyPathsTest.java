package co.edu.udistrital.strategy;

import co.edu.udistrital.strategy.controller.Client;
import co.edu.udistrital.strategy.model.*;
import co.edu.udistrital.strategy.view.*;

public class HappyPathsTest {

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
        output.printLn("1. PRUEBAS NOMINALES (HAPPY PATHS - 5 ESTRATEGIAS)");
        output.printLn("==================================================");

        Integer[] dataBubble = {64, 34, 25, 12, 22, 11, 90};
        client.executeSort(bubbleSort, dataBubble);

        Integer[] dataSelection = {29, 10, 14, 37, 13};
        client.executeSort(selectionSort, dataSelection);

        Integer[] dataInsertion = {12, 11, 13, 5, 6};
        client.executeSort(insertionSort, dataInsertion);

        Integer[] dataQuick = {45, 12, 89, 33, 1, 9, 21};
        client.executeSort(quickSort, dataQuick);

        Integer[] dataMerge = {38, 27, 43, 3, 9, 82, 10};
        client.executeSort(mergeSort, dataMerge);

        String[] dataStrings = {"Zorro", "Alfa", "Delta", "Beta"};
        client.executeSort(quickSort, dataStrings);
    }
}