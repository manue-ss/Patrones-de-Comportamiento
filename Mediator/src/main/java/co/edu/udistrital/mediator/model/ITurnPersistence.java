// Archivo: I TurnPersistence.java | Paquete: co.edu.udistrital.mediator.model
package co.edu.udistrital.mediator.model;

import java.util.List;

/**
 * Interfaz que define el contrato de persistencia mediante serialización de objetos para los turnos atendidos.
 */
public interface ITurnPersistence {

    /**
     * Guarda la lista histórica de turnos asignados/consumidos mediante serialización.
     *
     * @param turns Colección de turnos a serializar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    boolean saveAssignedTurns(List<TurnDTO> turns);

    /**
     * Recupera la lista histórica de turnos almacenados desde el archivo serializado.
     *
     * @return Lista de turnos deserializados.
     */
    List<TurnDTO> loadAssignedTurns();
}