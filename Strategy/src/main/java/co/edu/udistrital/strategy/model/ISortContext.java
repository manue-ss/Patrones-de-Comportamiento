package co.edu.udistrital.strategy.model;

public interface ISortContext {
    void setSortAlgorithm(Algorithm sortAlgorithm);
    Algorithm getSortAlgorithm();
    boolean sort(Object[] arr);
}