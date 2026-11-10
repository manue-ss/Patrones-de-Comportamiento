// Archivo: TurnDTO.java | Paquete: co.edu.udistrital.mediator.model
package co.edu.udistrital.mediator.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entidad de dominio rica y serializable que representa un turno emitido en la EPS.
 * Implementa Comparable para su ordenamiento automático en colas de prioridad.
 */
public class TurnDTO implements Comparable<TurnDTO>, Serializable {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String code;
    private final TurnType type;
    private final String patientName;
    private final long sequenceNumber;
    private final String emissionTime;
    private DoctorDTO assignedDoctor;

    /**
     * Construye un nuevo turno validando todas sus invariantes de negocio.
     *
     * @param code           Código único del turno (ej. U-001).
     * @param type           Categoría y prioridad del turno.
     * @param patientName    Nombre del paciente afiliado.
     * @param sequenceNumber Secuencia de creación para desempate FIFO en misma prioridad.
     */
    public TurnDTO(String code, TurnType type, String patientName, long sequenceNumber) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("El código del turno es obligatorio.");
        }
        if (type == null) {
            throw new IllegalArgumentException("El tipo de turno no puede ser nulo.");
        }
        if (patientName == null || patientName.isBlank()) {
            throw new IllegalArgumentException("El nombre del paciente no puede estar vacío.");
        }
        if (sequenceNumber <= 0) {
            throw new IllegalArgumentException("El número de secuencia debe ser positivo.");
        }
        this.code = code.trim();
        this.type = type;
        this.patientName = patientName.trim();
        this.sequenceNumber = sequenceNumber;
        this.emissionTime = LocalDateTime.now().format(TIME_FORMATTER);
    }

    /**
     * Asigna un médico al turno protegiendo la invariante de referencia no nula.
     *
     * @param doctor Médico encargado de atender el turno.
     */
    public void assignDoctor(DoctorDTO doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("No se puede asignar un doctor nulo al turno.");
        }
        this.assignedDoctor = doctor;
    }

    public String getCode() {
        return code;
    }

    public TurnType getType() {
        return type;
    }

    public String getPatientName() {
        return patientName;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public String getEmissionTime() {
        return emissionTime;
    }

    public DoctorDTO getAssignedDoctor() {
        return assignedDoctor;
    }

    @Override
    public int compareTo(TurnDTO other) {
        int priorityComparison = Integer.compare(this.type.getPriorityLevel(), other.type.getPriorityLevel());
        if (priorityComparison != 0) {
            return priorityComparison;
        }
        return Long.compare(this.sequenceNumber, other.sequenceNumber);
    }

    @Override
    public String toString() {
        String doctorInfo = (assignedDoctor != null)
                ? " -> Atendido por: " + assignedDoctor.toString()
                : " [En espera de asignación]";
        return String.format("Turno [%s | %s (Prioridad %d) | Paciente: %s | Hora: %s]%s",
                code, type.getDescription(), type.getPriorityLevel(), patientName, emissionTime, doctorInfo);
    }
}