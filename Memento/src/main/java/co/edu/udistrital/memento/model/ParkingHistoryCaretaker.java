// Archivo: ParkingHistoryCaretaker.java | Paquete: co.edu.udistrital.memento.model
package co.edu.udistrital.memento.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Caretaker concreto encargado de almacenar y gestionar los puntos de restauración (Mementos)
 * del sistema de parqueadero (por ejemplo, cortes parciales y cierres de fin de día).
 */
public class ParkingHistoryCaretaker implements ICaretaker {

    private final List<IMemento> history;

    public ParkingHistoryCaretaker() {
        this.history = new ArrayList<>();
    }

    @Override
    public void addMemento(IMemento memento) {
        if (memento == null) {
            throw new IllegalArgumentException("No se puede almacenar un Memento nulo en el historial.");
        }
        history.add(memento);
    }

    @Override
    public IMemento getMemento(int index) {
        if (index < 0 || index >= history.size()) {
            throw new IllegalArgumentException("Índice de Memento fuera de rango: " + index
                    + ". Rango válido: [0 - " + (history.size() - 1) + "].");
        }
        return history.get(index);
    }

    @Override
    public IMemento popLastMemento() {
        if (history.isEmpty()) {
            throw new IllegalStateException("El historial de Mementos está vacío. No hay estados previos para deshacer.");
        }
        return history.remove(history.size() - 1);
    }

    @Override
    public List<IMemento> getHistory() {
        return Collections.unmodifiableList(history);
    }

    @Override
    public int getHistorySize() {
        return history.size();
    }
}
