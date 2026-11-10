// Archivo: IteratorTest.java | Paquete: co.edu.udistrital.iterator
package co.edu.udistrital.iterator;

import co.edu.udistrital.iterator.controller.Client;
import co.edu.udistrital.iterator.model.IParkingAggregate;
import co.edu.udistrital.iterator.model.ParkingLot;
import co.edu.udistrital.iterator.view.ConsoleIn;
import co.edu.udistrital.iterator.view.ConsoleOut;
import co.edu.udistrital.iterator.view.IIn;
import co.edu.udistrital.iterator.view.IOut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Clase principal de ensamblaje (Composition Root) y progresión de pruebas del patrón Iterator.
 */
public class IteratorTest {

    /**
     * Punto de entrada de la aplicación que concatena pruebas deterministas, casos límite e interacción.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        IIn in = new ConsoleIn();
        IOut out = new ConsoleOut();

        IParkingAggregate parkingLot = new ParkingLot("Parqueadero Central UD", 5);
        Client client = new Client(parkingLot, in, out);

        LocalDate today = LocalDate.now();
        LocalDateTime referenceCutOffTime = LocalDateTime.of(today, LocalTime.of(12, 0));

        // =========================================================================
        // 1. PRUEBAS ALFA / DETERMINISTAS (HAPPY PATHS)
        // =========================================================================
        client.showHeader("FASE 1: PRUEBAS DETERMINISTAS (INGRESO, SALIDA Y RECORRIDOS CON ITERATOR)");

        // Registro secuencial de vehículos durante la mañana
        client.registerVehicleEntry("ABC123", "Automóvil", LocalDateTime.of(today, LocalTime.of(8, 0)), 100.0);
        client.registerVehicleEntry("XYZ789", "Motocicleta", LocalDateTime.of(today, LocalTime.of(8, 30)), 60.0);
        client.registerVehicleEntry("DEF456", "Camioneta", LocalDateTime.of(today, LocalTime.of(9, 15)), 130.0);
        client.registerVehicleEntry("GHI321", "Automóvil", LocalDateTime.of(today, LocalTime.of(10, 0)), 100.0);
        client.registerVehicleEntry("JKL654", "Motocicleta", LocalDateTime.of(today, LocalTime.of(11, 20)), 60.0);

        // Dos vehículos se retiran antes del corte de las 12:00 para evidenciar la diferencia
        // entre "todas las cuentas de hoy" y "lo que hay hasta el momento en el parqueadero"
        client.registerVehicleExit("ABC123", LocalDateTime.of(today, LocalTime.of(10, 30)));
        client.registerVehicleExit("GHI321", LocalDateTime.of(today, LocalTime.of(11, 30)));

        // Nuevo ingreso después de liberarse cupo (este será el último que entró)
        client.registerVehicleEntry("MNO987", "Camioneta", LocalDateTime.of(today, LocalTime.of(11, 40)), 130.0);

        // Ejecución de los 4 objetivos del problema al corte de las 12:00 m.
        client.showLastEnteredVehicleFee(referenceCutOffTime);
        client.showFirstEnteredElapsedTime(referenceCutOffTime);
        client.showTotalDailyAccounts(referenceCutOffTime);
        client.showCurrentParkedValue(referenceCutOffTime);

        // =========================================================================
        // 2. PRUEBAS DE VALIDACIÓN, PROTECCIÓN DE INVARIANTES Y CASOS LÍMITE
        // =========================================================================
        client.showHeader("FASE 2: PRUEBAS DE VALIDACIÓN Y CASOS LÍMITE (PROTECCIÓN DE INVARIANTES)");

        // Caso límite 1: Placa vacía
        client.registerVehicleEntry("   ", "Automóvil", LocalDateTime.of(today, LocalTime.of(11, 45)), 100.0);

        // Caso límite 2: Tarifa negativa o cero
        client.registerVehicleEntry("ERR001", "Automóvil", LocalDateTime.of(today, LocalTime.of(11, 45)), -50.0);

        // Caso límite 3: Vehículo duplicado que aún sigue dentro del parqueadero
        client.registerVehicleEntry("MNO987", "Camioneta", LocalDateTime.of(today, LocalTime.of(11, 50)), 130.0);

        // Caso límite 4: Hora de salida anterior a la hora de ingreso
        client.registerVehicleExit("XYZ789", LocalDateTime.of(today, LocalTime.of(7, 45)));

        // Caso límite 5: Salida de un vehículo que no está en el parqueadero
        client.registerVehicleExit("NOEXIS", LocalDateTime.of(today, LocalTime.of(11, 55)));

        // Caso límite 6: Consultas sobre un parqueadero recién creado (iteradores vacíos)
        IParkingAggregate emptyParking = new ParkingLot("Parqueadero Auxiliar Vacío", 3);
        Client emptyClient = new Client(emptyParking, in, out);
        emptyClient.showLastEnteredVehicleFee(referenceCutOffTime);
        emptyClient.showFirstEnteredElapsedTime(referenceCutOffTime);
        emptyClient.showTotalDailyAccounts(referenceCutOffTime);
        emptyClient.showCurrentParkedValue(referenceCutOffTime);

        // =========================================================================
        // 3. PRUEBA INTERACTIVA CON EL USUARIO
        // =========================================================================
        client.showHeader("FASE 3: PRUEBA INTERACTIVA POR CONSOLA");
        client.captureInteractiveVehicle(today, referenceCutOffTime);

        in.close();
    }
}
