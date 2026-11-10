package co.edu.udistrital.strategy.model;

public class SelectionSort implements Algorithm {

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
                int minIdx = i;
                for (int j = i + 1; j < n; j++) {
                    if (((Comparable<Object>) arr[j]).compareTo(arr[minIdx]) < 0) {
                        minIdx = j;
                    }
                }
                Object temp = arr[minIdx];
                arr[minIdx] = arr[i];
                arr[i] = temp;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}