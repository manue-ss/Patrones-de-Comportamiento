// Archivo: TurnType.java | Paquete: co.edu.udistrital.mediator.model
package co.edu.udistrital.mediator.model;

import java.io.Serializable;

/**
 * Enumeración que define los diferentes tipos de turnos de la EPS y su nivel de prioridad.
 */
public enum TurnType implements Serializable {

    URGENCY("U", "Urgencia Vital / Prioritaria", 1),
    PREFERENTIAL("P", "Atención Preferencial (Adulto Mayor / Embarazo)", 2),
    SPECIALIST("E", "Consulta con Especialista", 3),
    GENERAL("G", "Consulta Médica General", 4);

    private final String prefix;
    private final String description;
    private final int priorityLevel;

    TurnType(String prefix, String description, int priorityLevel) {
        this.prefix = prefix;
        this.description = description;
        this.priorityLevel = priorityLevel;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getDescription() {
        return description;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }
}