package co.edu.udistrital.strategy.model;

public class InsertionSort implements Algorithm {

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
            for (int i = 1; i < n; ++i) {
                Object key = arr[i];
                int j = i - 1;
                while (j >= 0 && ((Comparable<Object>) arr[j]).compareTo(key) > 0) {
                    arr[j + 1] = arr[j];
                    j = j - 1;
                }
                arr[j + 1] = key;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}