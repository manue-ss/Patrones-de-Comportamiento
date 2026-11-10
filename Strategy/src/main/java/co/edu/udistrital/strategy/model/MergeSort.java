package co.edu.udistrital.strategy.model;

public class MergeSort implements Algorithm {

    @Override
    public boolean sort(Object[] arr) {
        if (arr == null || arr.length == 0) {
            return false;
        }
        try {
            if (arr.length == 1) {
                return arr[0] instanceof Comparable;
            }
            Object[] temp = new Object[arr.length];
            mergeSort(arr, temp, 0, arr.length - 1);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void mergeSort(Object[] arr, Object[] temp, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, temp, left, mid);
            mergeSort(arr, temp, mid + 1, right);
            merge(arr, temp, left, mid, right);
        }
    }

    @SuppressWarnings("unchecked")
    private void merge(Object[] arr, Object[] temp, int left, int mid, int right) {
        System.arraycopy(arr, left, temp, left, right - left + 1);
        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            if (((Comparable<Object>) temp[i]).compareTo(temp[j]) <= 0) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }
        while (i <= mid) {
            arr[k++] = temp[i++];
        }
    }
}