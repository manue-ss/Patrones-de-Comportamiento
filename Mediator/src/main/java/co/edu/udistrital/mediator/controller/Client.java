// Archivo: Client.java | Paquete: co.edu.udistrital.mediator.controller
package co.edu.udistrital.mediator.controller;

import co.edu.udistrital.mediator.model.DoctorDTO;
import co.edu.udistrital.mediator.model.DoctorPool;
import co.edu.udistrital.mediator.model.EPS;
import co.edu.udistrital.mediator.model.TurnDTO;
import co.edu.udistrital.mediator.model.TurnMachine;
import co.edu.udistrital.mediator.model.TurnType;
import co.edu.udistrital.mediator.view.IIn;
import co.edu.udistrital.mediator.view.IOut;
import java.util.List;

/**
 * Cliente Orquestador (Caja Negra) que coordina las operaciones del usuario, formatea salidas de estado
 * (antes y después) y centraliza el control de excepciones.
 */
public class Client {

    private final TurnMachine turnMachine;
    private final DoctorPool doctorPool;
    private final EPS epsMediator;
    private final IIn in;
    private final IOut out;

    /**
     * Construye el cliente orquestador con todas sus dependencias inyectadas.
     *
     * @param turnMachine Máquina productora de turnos.
     * @param doctorPool  Pool consumidor de turnos.
     * @param epsMediator Mediador central de la EPS.
     * @param in          Interfaz de entrada de datos.
     * @param out         Interfaz de salida de datos.
     */
    public Client(TurnMachine turnMachine, DoctorPool doctorPool, EPS epsMediator, IIn in, IOut out) {
        if (turnMachine == null || doctorPool == null || epsMediator == null || in == null || out == null) {
            throw new IllegalArgumentException("Ninguna dependencia inyectada en Client puede ser nula.");
        }
        this.turnMachine = turnMachine;
        this.doctorPool = doctorPool;
        this.epsMediator = epsMediator;
        this.in = in;
        this.out = out;
    }

    /**
     * Registra un médico en el pool validando los datos y capturando cualquier excepción de dominio.
     *
     * @param id        Identificador del médico.
     * @param name      Nombre completo del médico.
     * @param specialty Especialidad médica.
     */
    public void registerDoctor(String id, String name, String specialty) {
        try {
            out.showMessage(String.format("-> Solicitud de registro médico [ID: '%s', Nombre: '%s', Especialidad: '%s']",
                    id, name, specialty));
            DoctorDTO doctor = new DoctorDTO(id, name, specialty);
            doctorPool.addDoctor(doctor);
        } catch (IllegalArgumentException e) {
            out.showError("Validación rechazada al registrar doctor: " + e.getMessage());
        }
    }

    /**
     * Solicita la emisión de un turno mostrando el estado previo y posterior de la cola del mediador.
     *
     * @param type        Tipo de turno a emitir.
     * @param patientName Nombre del paciente.
     */
    public void requestTurn(TurnType type, String patientName) {
        try {
            out.showMessage(String.format("\n-> Solicitud de Turno [Tipo: %s | Paciente: '%s'] | Estado Previo (En espera: %d, Doctores libres: %d)",
                    (type != null ? type.name() : "NULL"), patientName,
                    epsMediator.getPendingTurnsCount(), epsMediator.getAvailableDoctorsCount()));

            if (type == TurnType.URGENCY) {
                turnMachine.createUrgencyTurn(patientName);
            } else if (type == TurnType.PREFERENTIAL) {
                turnMachine.createPreferentialTurn(patientName);
            } else if (type == TurnType.SPECIALIST) {
                turnMachine.createSpecialistTurn(patientName);
            } else if (type == TurnType.GENERAL) {
                turnMachine.createGeneralTurn(patientName);
            } else {
                turnMachine.issueTurn(null, patientName);
            }

            out.showMessage(String.format("   Estado Posterior -> Total emitidos: %d | En espera en cola: %d | Doctores libres: %d",
                    turnMachine.getIssuedTurnsCount(), epsMediator.getPendingTurnsCount(), epsMediator.getAvailableDoctorsCount()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            out.showError("Validación rechazada al emitir turno: " + e.getMessage());
        }
    }

    /**
     * Espera un tiempo determinado para permitir que los hilos consumidores procesen la cola de turnos.
     *
     * @param millis Milisegundos de espera.
     */
    public void waitForAsyncConsumption(long millis) {
        try {
            out.showMessage("\n... Esperando procesamiento asíncrono de los hilos de atención médica (" + millis + " ms) ...");
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            out.showError("Espera asíncrona interrumpida.");
        }
    }

    /**
     * Muestra el historial de turnos asignados recuperados directamente desde el archivo serializado.
     */
    public void showSerializedHistory() {
        out.showMessage("\n--- HISTORIAL DE TURNOS PERSISTIDOS (DESERIALIZACIÓN DE OBJETOS) ---");
        List<TurnDTO> history = epsMediator.getPersistedHistory();
        if (history.isEmpty()) {
            out.showMessage("No existen turnos registrados en el archivo de persistencia.");
            return;
        }
        for (int i = 0; i < history.size(); i++) {
            out.showMessage(String.format("  %d. %s", (i + 1), history.get(i).toString()));
        }
        out.showMessage("Total de registros deserializados: " + history.size());
    }

    /**
     * Ejecuta un menú interactivo opcional por consola para emitir turnos manualmente.
     */
    public void runInteractiveMenu() {
        boolean running = true;
        while (running) {
            out.showMessage("\n================ MENÚ INTERACTIVO EPS (MEDIATOR) ================");
            out.showMessage("1. Emitir Turno de Urgencia (Prioridad 1)");
            out.showMessage("2. Emitir Turno Preferencial (Prioridad 2)");
            out.showMessage("3. Emitir Turno Especialista (Prioridad 3)");
            out.showMessage("4. Emitir Turno Medicina General (Prioridad 4)");
            out.showMessage("5. Ver Historial Serializado de Turnos Asignados");
            out.showMessage("0. Finalizar y apagar pool de hilos");

            try {
                int option = in.readInt("Seleccione una opción:");
                if (option == 0) {
                    running = false;
                    out.showMessage("Cerrando módulo interactivo de asignación de turnos...");
                } else if (option >= 1 && option <= 4) {
                    String patient = in.readString("Ingrese el nombre del paciente:");
                    TurnType selectedType = switch (option) {
                        case 1 -> TurnType.URGENCY;
                        case 2 -> TurnType.PREFERENTIAL;
                        case 3 -> TurnType.SPECIALIST;
                        default -> TurnType.GENERAL;
                    };
                    requestTurn(selectedType, patient);
                } else if (option == 5) {
                    showSerializedHistory();
                } else {
                    out.showError("Opción fuera de rango. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                out.showError("Formato numérico inválido. Debe ingresar un número entero.");
            }
        }
    }

    /**
     * Libera los recursos de entrada y apaga los hilos secundarios del pool de doctores.
     */
    public void shutdown() {
        doctorPool.shutdownPool();
        in.close();
    }

    public void printHeader(String title) {
        out.showMessage("\n======================================================================");
        out.showMessage(" " + title);
        out.showMessage("======================================================================");
    }
}