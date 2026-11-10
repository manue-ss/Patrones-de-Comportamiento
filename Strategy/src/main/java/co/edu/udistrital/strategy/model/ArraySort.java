package co.edu.udistrital.strategy.model;

public class ArraySort implements ISortContext {
    private Algorithm sortAlgorithm;

    public ArraySort() {
        this.sortAlgorithm = null;
    }

    public ArraySort(Algorithm sortAlgorithm) {
        this.sortAlgorithm = sortAlgorithm;
    }

    @Override
    public void setSortAlgorithm(Algorithm sortAlgorithm) {
        this.sortAlgorithm = sortAlgorithm;
    }

    @Override
    public Algorithm getSortAlgorithm() {
        return this.sortAlgorithm;
    }

    @Override
    public boolean sort(Object[] arr) {
        if (this.sortAlgorithm == null || arr == null || arr.length == 0) {
            return false;
        }
        try {
            return this.sortAlgorithm.sort(arr);
        } catch (Exception e) {
            return false;
        }
    }
}