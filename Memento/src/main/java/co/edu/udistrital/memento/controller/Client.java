// Archivo: Client.java | Paquete: co.edu.udistrital.memento.controller
package co.edu.udistrital.memento.controller;

import co.edu.udistrital.memento.model.ICaretaker;
import co.edu.udistrital.memento.model.IMemento;
import co.edu.udistrital.memento.model.IOriginator;
import co.edu.udistrital.memento.view.IIn;
import co.edu.udistrital.memento.view.IOut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador (Cliente Orquestador) que coordina el Originator (ParkingLot),
 * el Caretaker (ParkingHistoryCaretaker) y las abstracciones de E/S (IIn, IOut).
 */
public class Client {

    private final IOriginator originator;
    private final ICaretaker caretaker;
    private final IIn in;
    private final IOut out;

    public Client(IOriginator originator, ICaretaker caretaker, IIn in, IOut out) {
        if (originator == null || caretaker == null || in == null || out == null) {
            throw new IllegalArgumentException("Las dependencias inyectadas en Client no pueden ser nulas.");
        }
        this.originator = originator;
        this.caretaker = caretaker;
        this.in = in;
        this.out = out;
    }

    /**
     * Muestra un encabezado o mensaje informativo a través de la vista.
     *
     * @param message Mensaje a mostrar.
     */
    public void showHeader(String message) {
        out.show("\n====================================================================================");
        out.show("  " + message);
        out.show("====================================================================================");
    }

    /**
     * Registra la entrada, salida y pago de un auto en el parqueadero controlando excepciones.
     *
     * @param licensePlate Placa del vehículo.
     * @param entryTime    Fecha y hora de entrada.
     * @param exitTime     Fecha y hora de salida.
     * @param amountPaid   Valor pagado.
     */
    public void registerVehicle(String licensePlate, LocalDateTime entryTime, LocalDateTime exitTime, double amountPaid) {
        try {
            originator.registerCarExit(licensePlate, entryTime, exitTime, amountPaid);
            out.show("[OK] Vehículo registrado exitosamente -> Placa: " + licensePlate.trim().toUpperCase()
                    + " | Pago: $" + String.format("%,.2f", amountPaid));
        } catch (IllegalArgumentException | IllegalStateException e) {
            out.showErr("Fallo al registrar vehículo (" + licensePlate + "): " + e.getMessage());
        }
    }

    /**
     * Cierra formalmente la jornada del día asumiendo que todos los autos ya salieron.
     */
    public void closeOperatingDay() {
        try {
            originator.closeDay();
            out.show("[CIERRE] Se ha cerrado la operación del día. Todos los vehículos han salido del parqueadero.");
        } catch (IllegalStateException e) {
            out.showErr("Fallo al cerrar el día: " + e.getMessage());
        }
    }

    /**
     * Inicia una nueva jornada operativa en el parqueadero.
     *
     * @param newDate Fecha del nuevo día.
     */
    public void startNewOperatingDay(LocalDate newDate) {
        try {
            originator.startNewDay(newDate);
            out.show("[NUEVA JORNADA] Parqueadero reiniciado para la fecha: " + newDate);
        } catch (IllegalArgumentException e) {
            out.showErr("Fallo al iniciar nueva jornada: " + e.getMessage());
        }
    }

    /**
     * Crea un punto de control (Memento) del estado actual y lo guarda en el Caretaker.
     *
     * @param checkpointLabel Etiqueta descriptiva del punto de control.
     */
    public void createCheckpoint(String checkpointLabel) {
        try {
            IMemento memento = originator.saveToMemento(checkpointLabel);
            caretaker.addMemento(memento);
            out.show("[MEMENTO GUARDADO] " + memento.getMetadata());
        } catch (IllegalArgumentException e) {
            out.showErr("Fallo al crear el Memento: " + e.getMessage());
        }
    }

    /**
     * Restaura el estado del parqueadero a un punto de control específico por su índice.
     *
     * @param index Índice en el historial del Caretaker.
     */
    public void restoreCheckpointByIndex(int index) {
        try {
            IMemento memento = caretaker.getMemento(index);
            out.show("[RESTAURANDO POR ÍNDICE " + index + "] -> " + memento.getCheckpointLabel());
            originator.restoreFromMemento(memento);
            out.show(originator.getStateReport());
        } catch (IllegalArgumentException e) {
            out.showErr("Fallo al restaurar por índice: " + e.getMessage());
        }
    }

    /**
     * Deshace el último cambio extrayendo el Memento más reciente del Caretaker.
     */
    public void undoLastCheckpoint() {
        try {
            IMemento memento = caretaker.popLastMemento();
            out.show("[DESHACER / UNDO] Restaurando instantánea: " + memento.getCheckpointLabel());
            originator.restoreFromMemento(memento);
            out.show(originator.getStateReport());
        } catch (IllegalStateException | IllegalArgumentException e) {
            out.showErr("Fallo al ejecutar Undo: " + e.getMessage());
        }
    }

    /**
     * Intenta restaurar un Memento directamente (útil para validar referencias nulas o inválidas).
     *
     * @param memento Instancia de IMemento a evaluar.
     */
    public void restoreDirectMemento(IMemento memento) {
        try {
            originator.restoreFromMemento(memento);
        } catch (IllegalArgumentException e) {
            out.showErr("Protección de invariante activada en restauración: " + e.getMessage());
        }
    }

    /**
     * Muestra el estado actual del Originator (Parqueadero).
     */
    public void displayCurrentState() {
        out.show(originator.getStateReport());
    }

    /**
     * Muestra el listado completo de Mementos resguardados en el Caretaker.
     */
    public void displaySavedHistory() {
        List<IMemento> history = caretaker.getHistory();
        out.show("\n--- HISTORIAL DE MEMENTOS EN EL CARETAKER (" + history.size() + " disponibles) ---");
        if (history.isEmpty()) {
            out.show("  [No hay instantáneas almacenadas]");
            return;
        }
        for (int i = 0; i < history.size(); i++) {
            out.show("  [" + i + "] " + history.get(i).getMetadata());
        }
    }

    /**
     * Permite usar el canal de entrada si se requiere captura interactiva.
     *
     * @return Referencia a la abstracción de entrada.
     */
    public IIn getInput() {
        return in;
    }
}
