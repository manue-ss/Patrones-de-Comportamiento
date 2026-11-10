package co.edu.udistrital.strategy.model;

public class QuickSort implements Algorithm {

    @Override
    public boolean sort(Object[] arr) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        try {
            if (arr.length == 1) {
                return arr[0] instanceof Comparable;
            }
            quickSort(arr, 0, arr.length - 1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    private void quickSort(Object[] arr, int low, int high) {
        if (low < high) {
            Comparable<Object> pivot = (Comparable<Object>) arr[high];
            int i = low - 1;
            for (int j = low; j < high; j++) {
                if (((Comparable<Object>) arr[j]).compareTo(pivot) <= 0) {
                    i++;
                    Object temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            Object temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;

            int partitionIndex = i + 1;
            quickSort(arr, low, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, high);
        }
    }
}