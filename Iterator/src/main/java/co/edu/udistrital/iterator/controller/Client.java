// Archivo: Client.java | Paquete: co.edu.udistrital.iterator.controller
package co.edu.udistrital.iterator.controller;

import co.edu.udistrital.iterator.model.IParkingAggregate;
import co.edu.udistrital.iterator.model.ITicketIterator;
import co.edu.udistrital.iterator.model.ParkingTicket;
import co.edu.udistrital.iterator.view.IIn;
import co.edu.udistrital.iterator.view.IOut;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Controlador Orquestador que gestiona el sistema de parqueadero a través del patrón Iterator
 * y coordina las abstracciones de Entrada/Salida.
 */
public class Client {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final IParkingAggregate parkingLot;
    private final IIn in;
    private final IOut out;

    /**
     * Construye el cliente inyectando las abstracciones del modelo y de la vista.
     *
     * @param parkingLot agregado del parqueadero
     * @param in         abstracción de entrada
     * @param out        abstracción de salida
     */
    public Client(IParkingAggregate parkingLot, IIn in, IOut out) {
        if (parkingLot == null || in == null || out == null) {
            throw new IllegalArgumentException("Las dependencias inyectadas en Client no pueden ser nulas.");
        }
        this.parkingLot = parkingLot;
        this.in = in;
        this.out = out;
    }

    /**
     * Registra el ingreso de un vehículo controlando excepciones de negocio.
     *
     * @param licensePlate  placa del vehículo
     * @param vehicleType   tipo de vehículo
     * @param entryTime     hora de ingreso
     * @param ratePerMinute tarifa por minuto
     */
    public void registerVehicleEntry(String licensePlate, String vehicleType, LocalDateTime entryTime, double ratePerMinute) {
        try {
            ParkingTicket ticket = new ParkingTicket(licensePlate, vehicleType, entryTime, ratePerMinute);
            parkingLot.addTicket(ticket);
            out.showLine(String.format(
                    "[INGRESO OK] Vehículo %s (%s) ingresó a las %s | Tarifa: $%.2f/min",
                    ticket.getLicensePlate(),
                    ticket.getVehicleType(),
                    ticket.getEntryTime().format(TIME_FORMATTER),
                    ticket.getRatePerMinute()
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            out.showError("No se pudo registrar el ingreso: " + e.getMessage());
        }
    }

    /**
     * Registra la salida de un vehículo y muestra el valor liquidado de su cuenta.
     *
     * @param licensePlate placa del vehículo
     * @param exitTime     hora de salida
     */
    public void registerVehicleExit(String licensePlate, LocalDateTime exitTime) {
        try {
            ParkingTicket ticket = parkingLot.checkoutVehicle(licensePlate, exitTime);
            out.showLine(String.format(
                    "[SALIDA OK]  Vehículo %s salió a las %s | Tiempo total: %d min | Cuenta pagada: $%,.2f",
                    ticket.getLicensePlate(),
                    ticket.getExitTime().format(TIME_FORMATTER),
                    ticket.getElapsedMinutes(exitTime),
                    ticket.calculateFee(exitTime)
            ));
        } catch (IllegalArgumentException | IllegalStateException e) {
            out.showError("No se pudo registrar la salida: " + e.getMessage());
        }
    }

    /**
     * 1. Accede directamente al último vehículo que entró (usando el iterador)
     * para determinar cuánto hay que cobrarle hasta el momento.
     *
     * @param currentTime hora de corte actual
     */
    public void showLastEnteredVehicleFee(LocalDateTime currentTime) {
        out.showLine("\n--- [CONSULTA 1] ÚLTIMO VEHÍCULO EN INGRESAR AL PARQUEADERO ---");
        try {
            ITicketIterator activeIterator = parkingLot.createActiveTicketsIterator();
            ParkingTicket lastTicket = activeIterator.last();

            if (lastTicket == null) {
                out.showLine("No hay vehículos estacionados actualmente en el parqueadero.");
                return;
            }

            long minutes = lastTicket.getElapsedMinutes(currentTime);
            double totalFee = lastTicket.calculateFee(currentTime);

            out.showLine("Hora de corte actual       : " + currentTime.format(TIME_FORMATTER));
            out.showLine("Último vehículo ingresado  : " + lastTicket.getLicensePlate() + " (" + lastTicket.getVehicleType() + ")");
            out.showLine("Hora de ingreso            : " + lastTicket.getEntryTime().format(TIME_FORMATTER));
            out.showLine("Tiempo transcurrido        : " + minutes + " minutos");
            out.showLine(String.format("Valor a cobrar al último   : $%,.2f", totalFee));
        } catch (IllegalArgumentException e) {
            out.showError("Error al consultar el último vehículo: " + e.getMessage());
        }
    }

    /**
     * 2. Determina hace cuánto tiempo entró el primer vehículo registrado
     * (tanto el primero del día como el primero que aún permanece estacionado).
     *
     * @param currentTime hora de corte actual
     */
    public void showFirstEnteredElapsedTime(LocalDateTime currentTime) {
        out.showLine("\n--- [CONSULTA 2] TIEMPO TRANSCURRIDO DESDE EL PRIMER INGRESO ---");
        try {
            ITicketIterator allIterator = parkingLot.createAllTicketsIterator();
            ParkingTicket firstOfDay = allIterator.first();

            if (firstOfDay == null) {
                out.showLine("Aún no se han registrado vehículos en la jornada de hoy.");
                return;
            }

            long minutesSinceFirstOfDay = java.time.Duration.between(firstOfDay.getEntryTime(), currentTime).toMinutes();
            out.showLine(String.format(
                    "Primer vehículo de la jornada : %s (%s) | Ingresó a las %s (hace %d minutos, equivalentes a %d h %d min)",
                    firstOfDay.getLicensePlate(),
                    firstOfDay.getVehicleType(),
                    firstOfDay.getEntryTime().format(TIME_FORMATTER),
                    minutesSinceFirstOfDay,
                    minutesSinceFirstOfDay / 60,
                    minutesSinceFirstOfDay % 60
            ));

            ITicketIterator activeIterator = parkingLot.createActiveTicketsIterator();
            ParkingTicket firstStillParked = activeIterator.first();
            if (firstStillParked != null) {
                long minutesActive = firstStillParked.getElapsedMinutes(currentTime);
                out.showLine(String.format(
                        "Primero aún en parqueadero    : %s (%s) | Ingresó a las %s (lleva %d minutos estacionado, cobro acumulado: $%,.2f)",
                        firstStillParked.getLicensePlate(),
                        firstStillParked.getVehicleType(),
                        firstStillParked.getEntryTime().format(TIME_FORMATTER),
                        minutesActive,
                        firstStillParked.calculateFee(currentTime)
                ));
            } else {
                out.showLine("Primero aún en parqueadero    : Ninguno (todos los vehículos ya salieron).");
            }
        } catch (IllegalArgumentException e) {
            out.showError("Error al consultar el primer vehículo: " + e.getMessage());
        }
    }

    /**
     * 3. Recorre con el iterador general todos los registros del día (los que ya salieron
     * más los que siguen adentro) para sumar todas las cuentas de hoy.
     *
     * @param currentTime hora de corte actual
     */
    public void showTotalDailyAccounts(LocalDateTime currentTime) {
        out.showLine("\n--- [CONSULTA 3] SUMA DE TODAS LAS CUENTAS DE HOY (HISTÓRICO + ACTIVOS) ---");
        try {
            ITicketIterator allIterator = parkingLot.createAllTicketsIterator();
            if (!allIterator.hasNext()) {
                out.showLine("No existen cuentas registradas en el día de hoy.");
                return;
            }

            double totalDaySum = 0.0;
            int count = 0;

            while (allIterator.hasNext()) {
                ParkingTicket ticket = allIterator.next();
                double ticketFee = ticket.calculateFee(currentTime);
                totalDaySum += ticketFee;
                count++;
                out.showLine("  " + count + ". " + ticket.formatSummary(currentTime));
            }

            out.showLine("------------------------------------------------------------------------------------------------------------");
            out.showLine(String.format("Total de vehículos atendidos hoy : %d", count));
            out.showLine(String.format("SUMA TOTAL DE LAS CUENTAS DE HOY : $%,.2f", totalDaySum));
        } catch (IllegalArgumentException e) {
            out.showError("Error al sumar las cuentas del día: " + e.getMessage());
        }
    }

    /**
     * 4. Recorre con el iterador de activos únicamente los vehículos que están actualmente
     * en el parqueadero para saber cuánto vale lo que hay estacionado hasta el momento.
     *
     * @param currentTime hora de corte actual
     */
    public void showCurrentParkedValue(LocalDateTime currentTime) {
        out.showLine("\n--- [CONSULTA 4] VALOR DE LO QUE HAY HASTA EL MOMENTO EN EL PARQUEADERO ---");
        try {
            ITicketIterator activeIterator = parkingLot.createActiveTicketsIterator();
            if (!activeIterator.hasNext()) {
                out.showLine("El parqueadero se encuentra vacío en este momento ($0.00).");
                return;
            }

            double activeTotalValue = 0.0;
            int activeCount = 0;

            while (activeIterator.hasNext()) {
                ParkingTicket ticket = activeIterator.next();
                double currentFee = ticket.calculateFee(currentTime);
                activeTotalValue += currentFee;
                activeCount++;
                out.showLine("  " + activeCount + ". " + ticket.formatSummary(currentTime));
            }

            out.showLine("------------------------------------------------------------------------------------------------------------");
            out.showLine(String.format("Vehículos actualmente en parqueadero : %d / %d cupos", activeCount, parkingLot.getMaxCapacity()));
            out.showLine(String.format("VALOR ACUMULADO EN EL PARQUEADERO    : $%,.2f", activeTotalValue));
        } catch (IllegalArgumentException e) {
            out.showError("Error al calcular el valor actual en el parqueadero: " + e.getMessage());
        }
    }

    /**
     * Permite registrar interactivamente un vehículo desde la vista y re-ejecutar el balance.
     *
     * @param baseDate    fecha base de operación
     * @param currentTime hora actual de corte del sistema
     */
    public void captureInteractiveVehicle(LocalDate baseDate, LocalDateTime currentTime) {
        out.showLine("\n--- [INTERACCIÓN] REGISTRO MANUAL DE VEHÍCULO DESDE CONSOLA ---");
        out.show("¿Desea ingresar un vehículo manualmente para probar el iterador? (S/N): ");
        String answer = in.nextLine().trim();
        if (!answer.equalsIgnoreCase("S")) {
            out.showLine("Registro interactivo omitido por el usuario.");
            return;
        }

        try {
            out.show("Ingrese la placa del vehículo (ej. ZZZ999): ");
            String plate = in.nextLine();

            out.show("Ingrese el tipo de vehículo (Automóvil / Motocicleta / Camioneta): ");
            String type = in.nextLine();

            out.show("Ingrese la hora de entrada en formato HH:mm (debe ser <= " + currentTime.format(TIME_FORMATTER) + "): ");
            String timeText = in.nextLine().trim();
            LocalTime parsedTime = LocalTime.parse(timeText, TIME_FORMATTER);
            LocalDateTime entryDateTime = LocalDateTime.of(baseDate, parsedTime);

            out.show("Ingrese la tarifa por minuto en pesos (ej. 120): ");
            double rate = in.nextDouble();

            registerVehicleEntry(plate, type, entryDateTime, rate);
            showLastEnteredVehicleFee(currentTime);
            showCurrentParkedValue(currentTime);
            showTotalDailyAccounts(currentTime);
        } catch (DateTimeParseException e) {
            out.showError("Formato de hora inválido. Debe utilizar HH:mm (ejemplo 11:45).");
        } catch (IllegalArgumentException e) {
            out.showError("Error en los datos ingresados: " + e.getMessage());
        }
    }

    /**
     * Muestra un encabezado descriptivo en la vista.
     *
     * @param title título de la sección
     */
    public void showHeader(String title) {
        out.showLine("\n============================================================================================================");
        out.showLine("  " + title);
        out.showLine("============================================================================================================");
    }
}
