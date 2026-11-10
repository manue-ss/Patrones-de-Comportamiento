// Archivo: IMemento.java | Paquete: co.edu.udistrital.memento.model
package co.edu.udistrital.memento.model;

import java.time.LocalDateTime;

/**
 * Interfaz estrecha (Narrow Interface) del Memento expuesta al Caretaker y al Cliente.
 * Permite consultar metadatos del punto de restauración sin violar el encapsulamiento del estado interno.
 */
public interface IMemento {

    /**
     * Obtiene la etiqueta descriptiva de la instantánea.
     *
     * @return Etiqueta del memento.
     */
    String getCheckpointLabel();

    /**
     * Obtiene la fecha y hora en que se capturó el memento.
     *
     * @return Fecha y hora de creación.
     */
    LocalDateTime getSavedAt();

    /**
     * Obtiene un resumen legible de los metadatos del memento.
     *
     * @return Descripción de la instantánea.
     */
    String getMetadata();
}
