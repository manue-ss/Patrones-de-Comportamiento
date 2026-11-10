// Archivo: DoctorPool.java | Paquete: co.edu.udistrital.mediator.model
package co.edu.udistrital.mediator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Componente Colega Consumidor que administra el pool de médicos y simula la atención en hilos secundarios.
 */
public class DoctorPool extends Colleague {

    public static final String EVENT_DOCTOR_AVAILABLE = "DOCTOR_AVAILABLE";
    public static final String EVENT_DOCTOR_REGISTERED = "DOCTOR_REGISTERED";
    public static final String COMMAND_ATTEND_TURN = "ATTEND_TURN";

    private final List<DoctorDTO> doctors;
    private final long attentionTimeMillis;
    private final ExecutorService attentionExecutor;
    private volatile boolean active;

    /**
     * Construye el pool de doctores definiendo el tiempo simulado de consulta por turno.
     *
     * @param attentionTimeMillis Tiempo en milisegundos que dura la atención médica en el hilo secundario.
     */
    public DoctorPool(long attentionTimeMillis) {
        super();
        if (attentionTimeMillis < 50) {
            throw new IllegalArgumentException("El tiempo de atención debe ser de al menos 50 ms.");
        }
        this.doctors = Collections.synchronizedList(new ArrayList<>());
        this.attentionTimeMillis = attentionTimeMillis;
        this.attentionExecutor = Executors.newCachedThreadPool();
        this.active = true;
    }

    /**
     * Registra un nuevo médico en el pool y notifica inmediatamente su disponibilidad al mediador.
     *
     * @param doctor Instancia del médico a incorporar.
     */
    public void addDoctor(DoctorDTO doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("No se puede agregar un doctor nulo al pool.");
        }
        doctors.add(doctor);
        if (this.mediator != null) {
            this.mediator.notify(this, EVENT_DOCTOR_REGISTERED, doctor);
            if (doctor.isAvailable()) {
                this.mediator.notify(this, EVENT_DOCTOR_AVAILABLE, doctor);
            }
        }
    }

    /**
     * Obtiene una copia inmutable de la lista de médicos registrados.
     *
     * @return Lista de doctores.
     */
    public List<DoctorDTO> getDoctors() {
        synchronized (doctors) {
            return new ArrayList<>(doctors);
        }
    }

    @Override
    public void receive(String event, Object context) {
        if (COMMAND_ATTEND_TURN.equals(event) && context instanceof TurnDTO) {
            TurnDTO assignedTurn = (TurnDTO) context;
            simulateMedicalAttentionAsync(assignedTurn);
        }
    }

    /**
     * Ejecuta en un hilo secundario el consumo del turno (tiempo de consulta) y libera al doctor al finalizar.
     *
     * @param turn Turno que contiene al paciente y al doctor asignado.
     */
    private void simulateMedicalAttentionAsync(TurnDTO turn) {
        DoctorDTO doctor = turn.getAssignedDoctor();
        if (doctor == null || !active) {
            return;
        }

        attentionExecutor.submit(() -> {
            try {
                Thread.sleep(attentionTimeMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
            if (active && this.mediator != null) {
                doctor.setAvailable(true);
                this.mediator.notify(this, EVENT_DOCTOR_AVAILABLE, doctor);
            }
        });
    }

    /**
     * Detiene ordenadamente el pool de hilos consumidores.
     */
    public void shutdownPool() {
        this.active = false;
        attentionExecutor.shutdown();
        try {
            if (!attentionExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                attentionExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            attentionExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}