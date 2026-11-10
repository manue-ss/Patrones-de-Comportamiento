package co.edu.udistrital.strategy.model;

public class BubbleSort implements Algorithm {

    @Override
    @SuppressWarnings("unchecked")
    public boolean sort(Object[] arr) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        try {
            int n = arr.length;
            if (n == 1) {
                return arr[0] instanceof Comparable;
            }
            for (int i = 0; i < n - 1; i++) {
                boolean swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    if (((Comparable<Object>) arr[j]).compareTo(arr[j + 1]) > 0) {
                        Object temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;
                        swapped = true;
                    }
                }
                if (!swapped) {
                    break;
                }
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}