// Archivo: ICaretaker.java | Paquete: co.edu.udistrital.memento.model
package co.edu.udistrital.memento.model;

import java.util.List;

/**
 * Interfaz que define el contrato del Caretaker (Guardián) en el patrón Memento.
 * Custodia el historial de instantáneas sin conocer ni modificar su contenido interno.
 */
public interface ICaretaker {

    /**
     * Almacena un nuevo Memento en el historial.
     *
     * @param memento Instantánea a resguardar.
     */
    void addMemento(IMemento memento);

    /**
     * Obtiene un Memento específico según su índice en el historial.
     *
     * @param index Posición en el historial (base 0).
     * @return Memento solicitado.
     */
    IMemento getMemento(int index);

    /**
     * Extrae y retorna el último Memento guardado (operación Undo).
     *
     * @return El Memento más reciente.
     */
    IMemento popLastMemento();

    /**
     * Obtiene una lista inmutable con todos los Mementos almacenados.
     *
     * @return Historial de Mementos.
     */
    List<IMemento> getHistory();

    /**
     * Retorna la cantidad de puntos de restauración disponibles.
     *
     * @return Tamaño del historial.
     */
    int getHistorySize();
}
