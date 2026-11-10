// Archivo: DoctorDTO.java | Paquete: co.edu.udistrital.mediator.model
package co.edu.udistrital.mediator.model;

import java.io.Serializable;

/**
 * Entidad de dominio y transferencia de datos que representa un médico de la EPS.
 */
public class DoctorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String id;
    private final String name;
    private final String specialty;
    private boolean available;

    /**
     * Crea una nueva instancia de un doctor validando sus invariantes de dominio.
     *
     * @param id        Identificador único del médico.
     * @param name      Nombre completo del médico.
     * @param specialty Especialidad médica.
     */
    public DoctorDTO(String id, String name, String specialty) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El ID del doctor no puede ser nulo ni vacío.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del doctor no puede ser nulo ni vacío.");
        }
        if (specialty == null || specialty.isBlank()) {
            throw new IllegalArgumentException("La especialidad del doctor no puede ser nula ni vacía.");
        }
        this.id = id.trim();
        this.name = name.trim();
        this.specialty = specialty.trim();
        this.available = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public synchronized boolean isAvailable() {
        return available;
    }

    public synchronized void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return String.format("Dr(a). %s [ID: %s | Especialidad: %s]", name, id, specialty);
    }
}