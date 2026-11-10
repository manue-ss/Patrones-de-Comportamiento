// Archivo: EPS.java | Paquete: co.edu.udistrital.mediator.model
package co.edu.udistrital.mediator.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Mediador Concreto (EPS) que centraliza la cola de prioridad de turnos, el emparejamiento con doctores
 * disponibles, la persistencia por serialización y la notificación hacia el colega controlador de vista.
 */
public class EPS implements Mediator {

    public static final String EVENT_SHOW_CREATED = "SHOW_CREATED";
    public static final String EVENT_SHOW_ASSIGNED = "SHOW_ASSIGNED";
    public static final String EVENT_SHOW_DOCTOR_READY = "SHOW_DOCTOR_READY";

    private final TurnMachine turnMachine;
    private final DoctorPool doctorPool;
    private final Colleague viewController;
    private final ITurnPersistence persistence;

    private final PriorityQueue<TurnDTO> pendingTurns;
    private final Queue<DoctorDTO> availableDoctors;
    private final List<TurnDTO> assignedHistory;

    /**
     * Construye el mediador EPS inyectando sus colegas y el servicio de persistencia.
     *
     * @param turnMachine    Colega productor de turnos.
     * @param doctorPool     Colega consumidor de turnos (pool de médicos).
     * @param viewController Colega orquestador de salida (ViewController).
     * @param persistence    Servicio de serialización de objetos.
     */
    public EPS(TurnMachine turnMachine, DoctorPool doctorPool, Colleague viewController, ITurnPersistence persistence) {
        if (turnMachine == null || doctorPool == null || viewController == null || persistence == null) {
            throw new IllegalArgumentException("Ninguno de los colegas ni el servicio de persistencia puede ser nulo en EPS.");
        }
        this.turnMachine = turnMachine;
        this.doctorPool = doctorPool;
        this.viewController = viewController;
        this.persistence = persistence;

        this.pendingTurns = new PriorityQueue<>();
        this.availableDoctors = new ArrayDeque<>();
        this.assignedHistory = new ArrayList<>(persistence.loadAssignedTurns());

        this.turnMachine.setMediator(this);
        this.doctorPool.setMediator(this);
        this.viewController.setMediator(this);
    }

    @Override
    public synchronized void notify(Colleague sender, String event, Object context) {
        if (sender == null || event == null || event.isBlank()) {
            throw new IllegalArgumentException("El remitente y el evento de notificación no pueden ser nulos.");
        }

        if (sender == turnMachine && TurnMachine.EVENT_NEW_TURN.equals(event)) {
            handleNewTurn((TurnDTO) context);
        } else if (sender == doctorPool && DoctorPool.EVENT_DOCTOR_REGISTERED.equals(event)) {
            viewController.receive(EVENT_SHOW_DOCTOR_READY, context);
        } else if (sender == doctorPool && DoctorPool.EVENT_DOCTOR_AVAILABLE.equals(event)) {
            handleDoctorAvailable((DoctorDTO) context);
        }
    }

    /**
     * Encola un nuevo turno según su prioridad, notifica su emisión y verifica si puede asignarse de inmediato.
     *
     * @param turn Turno emitido por TurnMachine.
     */
    private void handleNewTurn(TurnDTO turn) {
        if (turn == null) {
            throw new IllegalArgumentException("El turno recibido por el mediador no puede ser nulo.");
        }
        pendingTurns.offer(turn);
        viewController.receive(EVENT_SHOW_CREATED, turn);
        matchTurnWithDoctor();
    }

    /**
     * Registra un médico libre en la cola de disponibilidad e intenta asignarle el turno más prioritario.
     *
     * @param doctor Médico que acaba de quedar disponible en DoctorPool.
     */
    private void handleDoctorAvailable(DoctorDTO doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("El doctor reportado como disponible no puede ser nulo.");
        }
        if (!availableDoctors.contains(doctor)) {
            availableDoctors.offer(doctor);
        }
        matchTurnWithDoctor();
    }

    /**
     * Empareja el turno de mayor prioridad en espera con el primer médico disponible.
     * Persiste el estado serializado y delega la atención al DoctorPool y la visualización al ViewController.
     */
    private void matchTurnWithDoctor() {
        while (!pendingTurns.isEmpty() && !availableDoctors.isEmpty()) {
            TurnDTO nextTurn = pendingTurns.poll();
            DoctorDTO freeDoctor = availableDoctors.poll();

            freeDoctor.setAvailable(false);
            nextTurn.assignDoctor(freeDoctor);

            assignedHistory.add(nextTurn);
            persistence.saveAssignedTurns(assignedHistory);

            viewController.receive(EVENT_SHOW_ASSIGNED, nextTurn);
            doctorPool.receive(DoctorPool.COMMAND_ATTEND_TURN, nextTurn);
        }
    }

    public synchronized int getPendingTurnsCount() {
        return pendingTurns.size();
    }

    public synchronized int getAvailableDoctorsCount() {
        return availableDoctors.size();
    }

    public synchronized List<TurnDTO> getPersistedHistory() {
        return persistence.loadAssignedTurns();
    }
}