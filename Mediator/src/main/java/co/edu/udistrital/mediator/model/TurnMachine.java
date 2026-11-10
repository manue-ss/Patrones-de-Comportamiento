// Archivo: TurnMachine.java | Paquete: co.edu.udistrital.mediator.model
package co.edu.udistrital.mediator.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Componente Colega Productor encargado de emitir diferentes tipos de turnos en la EPS.
 */
public class TurnMachine extends Colleague {

    public static final String EVENT_NEW_TURN = "NEW_TURN";

    private final AtomicLong totalCounter;

    /**
     * Inicializa la máquina dispensadora de turnos con su contador en cero.
     */
    public TurnMachine() {
        super();
        this.totalCounter = new AtomicLong(0);
    }

    /**
     * Emite un turno de Urgencia (Prioridad 1).
     *
     * @param patientName Nombre del paciente.
     * @return Instancia del turno generado.
     */
    public TurnDTO createUrgencyTurn(String patientName) {
        return issueTurn(TurnType.URGENCY, patientName);
    }

    /**
     * Emite un turno Preferencial (Prioridad 2).
     *
     * @param patientName Nombre del paciente.
     * @return Instancia del turno generado.
     */
    public TurnDTO createPreferentialTurn(String patientName) {
        return issueTurn(TurnType.PREFERENTIAL, patientName);
    }

    /**
     * Emite un turno de Especialista (Prioridad 3).
     *
     * @param patientName Nombre del paciente.
     * @return Instancia del turno generado.
     */
    public TurnDTO createSpecialistTurn(String patientName) {
        return issueTurn(TurnType.SPECIALIST, patientName);
    }

    /**
     * Emite un turno de Medicina General (Prioridad 4).
     *
     * @param patientName Nombre del paciente.
     * @return Instancia del turno generado.
     */
    public TurnDTO createGeneralTurn(String patientName) {
        return issueTurn(TurnType.GENERAL, patientName);
    }

    /**
     * Método polimórfico general para emitir un turno según su tipo y notificar al mediador.
     *
     * @param type        Tipo de turno solicitado.
     * @param patientName Nombre del paciente.
     * @return Turno creado y registrado.
     */
    public TurnDTO issueTurn(TurnType type, String patientName) {
        if (this.mediator == null) {
            throw new IllegalStateException("La máquina de turnos no tiene un mediador configurado.");
        }
        if (type == null) {
            throw new IllegalArgumentException("El tipo de turno no puede ser nulo.");
        }
        if (patientName == null || patientName.isBlank()) {
            throw new IllegalArgumentException("El nombre del paciente es obligatorio para emitir el turno.");
        }

        long sequence = totalCounter.incrementAndGet();
        String code = String.format("%s-%03d", type.getPrefix(), sequence);
        TurnDTO newTurn = new TurnDTO(code, type, patientName, sequence);

        this.mediator.notify(this, EVENT_NEW_TURN, newTurn);
        return newTurn;
    }

    public long getIssuedTurnsCount() {
        return totalCounter.get();
    }

    @Override
    public void receive(String event, Object context) {
        // TurnMachine actúa como productor puro; puede reaccionar a reinicios de secuencia si el mediador lo indica.
        if ("RESET_COUNTER".equals(event)) {
            this.totalCounter.set(0);
        }
    }
}