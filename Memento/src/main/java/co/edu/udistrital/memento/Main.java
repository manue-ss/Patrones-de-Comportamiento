// Archivo: Main.java | Paquete: co.edu.udistrital.memento
package co.edu.udistrital.memento;

import co.edu.udistrital.memento.controller.Client;
import co.edu.udistrital.memento.model.ICaretaker;
import co.edu.udistrital.memento.model.IOriginator;
import co.edu.udistrital.memento.model.ParkingHistoryCaretaker;
import co.edu.udistrital.memento.model.ParkingLot;
import co.edu.udistrital.memento.view.ConsoleIn;
import co.edu.udistrital.memento.view.ConsoleOut;
import co.edu.udistrital.memento.view.IIn;
import co.edu.udistrital.memento.view.IOut;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Composition Root que ensambla las dependencias bajo inversión de dependencias (DIP)
 * y ejecuta la progresión de pruebas del patrón Memento aplicado al sistema de parqueadero.
 */
public class Main {

    public static void main(String[] args) {
        // 1. Instanciación de abstracciones de Vista (E/S)
        IOut out = new ConsoleOut();
        IIn in = new ConsoleIn();

        // 2. Instanciación de abstracciones del Modelo (Originator y Caretaker)
        LocalDate dayOne = LocalDate.of(2026, 11, 10);
        IOriginator parkingLot = new ParkingLot("Parqueadero Central UD", dayOne);
        ICaretaker caretaker = new ParkingHistoryCaretaker();

        // 3. Ensamblaje del Controlador / Orquestador (Client)
        Client client = new Client(parkingLot, caretaker, in, out);

        // =========================================================================
        // FASE 1: PRUEBAS ALFA / DETERMINISTAS (HAPPY PATHS)
        // =========================================================================
        client.showHeader("FASE 1: PRUEBAS ALFA - OPERACIÓN DIARIA, CIERRE Y PATRÓN MEMENTO");

        // Registro de los primeros vehículos de la mañana (todos con su entrada, salida y pago)
        client.registerVehicle("ABC-123",
                LocalDateTime.of(2026, 11, 10, 7, 30),
                LocalDateTime.of(2026, 11, 10, 9, 45),
                12500.0);
        client.registerVehicle("XYZ-789",
                LocalDateTime.of(2026, 11, 10, 8, 15),
                LocalDateTime.of(2026, 11, 10, 11, 30),
                18000.0);

        client.displayCurrentState();

        // Guardamos el primer Memento: Corte de mediodía
        client.createCheckpoint("Corte Mediodía - 2026-11-10 (2 autos)");

        // Registro de vehículos de la tarde
        client.registerVehicle("MNP-456",
                LocalDateTime.of(2026, 11, 10, 13, 0),
                LocalDateTime.of(2026, 11, 10, 16, 20),
                19500.0);
        client.registerVehicle("QWE-852",
                LocalDateTime.of(2026, 11, 10, 15, 10),
                LocalDateTime.of(2026, 11, 10, 18, 10),
                16500.0);

        // Guardamos el segundo Memento: Antes de cerrar el día (4 autos)
        client.createCheckpoint("Pre-Cierre Tarde - 2026-11-10 (4 autos)");

        // Registro del último vehículo de la noche y cierre oficial del día (todos los autos ya salieron)
        client.registerVehicle("JKL-999",
                LocalDateTime.of(2026, 11, 10, 18, 30),
                LocalDateTime.of(2026, 11, 10, 21, 0),
                14000.0);
        client.closeOperatingDay();
        client.displayCurrentState();

        // Guardamos el tercer Memento: Cierre definitivo del Día 1
        client.createCheckpoint("Cierre Oficial Fin de Día - 2026-11-10 (5 autos)");

        // Se inicia el siguiente día operativo (el parqueadero queda limpio para la nueva jornada)
        LocalDate dayTwo = LocalDate.of(2026, 11, 11);
        client.startNewOperatingDay(dayTwo);
        client.registerVehicle("NEW-001",
                LocalDateTime.of(2026, 11, 11, 8, 0),
                LocalDateTime.of(2026, 11, 11, 9, 0),
                6000.0);
        client.displayCurrentState();

        // Visualizar todos los Mementos custodiados por el Caretaker
        client.displaySavedHistory();

        // Demostración de restauración: Auditoría solicita regresar al Cierre Oficial del Día 1 (índice 2)
        client.showHeader("DEMOSTRACIÓN DE RESTAURACIÓN DE MEMENTOS (ROLLBACK / AUDITORÍA)");
        client.restoreCheckpointByIndex(2);

        // Demostración de restauración al Corte de Mediodía (índice 0)
        client.restoreCheckpointByIndex(0);

        // Demostración de Undo (extrae el último Memento de la pila del Caretaker)
        client.undoLastCheckpoint();

        // =========================================================================
        // FASE 2: PRUEBAS DE VALIDACIÓN, INVARIANTES Y CASOS LÍMITE
        // =========================================================================
        client.showHeader("FASE 2: PRUEBAS DE VALIDACIÓN Y PROTECCIÓN DE INVARIANTES");

        // 2.1 Intentar registrar un auto cuando el día restaurado ya está cerrado
        client.registerVehicle("ERR-001",
                LocalDateTime.of(2026, 11, 10, 21, 10),
                LocalDateTime.of(2026, 11, 10, 22, 0),
                5000.0);

        // Restauramos al estado de Pre-Cierre (índice 1, donde el día aún estaba abierto) para probar validaciones de registro
        client.restoreCheckpointByIndex(1);

        // 2.2 Placa nula o vacía
        client.registerVehicle("   ",
                LocalDateTime.of(2026, 11, 10, 10, 0),
                LocalDateTime.of(2026, 11, 10, 11, 0),
                5000.0);

        // 2.3 Hora de salida anterior o igual a la hora de entrada
        client.registerVehicle("BAD-111",
                LocalDateTime.of(2026, 11, 10, 14, 0),
                LocalDateTime.of(2026, 11, 10, 12, 30),
                8000.0);

        // 2.4 Auto que no sale el mismo día (viola la regla de que al final del día todos los autos ya salieron)
        client.registerVehicle("OVN-222",
                LocalDateTime.of(2026, 11, 10, 22, 0),
                LocalDateTime.of(2026, 11, 11, 6, 0),
                35000.0);

        // 2.5 Monto pagado negativo o cero
        client.registerVehicle("FRE-333",
                LocalDateTime.of(2026, 11, 10, 10, 0),
                LocalDateTime.of(2026, 11, 10, 11, 0),
                -4500.0);

        // 2.6 Índice de Memento inexistente en el Caretaker
        client.restoreCheckpointByIndex(99);

        // 2.7 Etiqueta de Memento vacía
        client.createCheckpoint("   ");

        // 2.8 Intento de restaurar un Memento nulo
        client.restoreDirectMemento(null);

        // Verificación final de que el estado del parqueadero permanece íntegro tras los errores controlados
        client.showHeader("ESTADO FINAL ÍNTEGRO TRAS PRUEBAS DE PROTECCIÓN");
        client.displayCurrentState();
    }
}
