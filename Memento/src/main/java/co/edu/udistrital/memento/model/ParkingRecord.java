// Archivo: ParkingRecord.java | Paquete: co.edu.udistrital.memento.model
package co.edu.udistrital.memento.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entidad de dominio inmutable que representa el registro de parqueo de un vehículo
 * al finalizar el día (con su fecha de entrada, fecha de salida y valor pagado).
 */
public final class ParkingRecord {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String licensePlate;
    private final LocalDateTime entryTime;
    private final LocalDateTime exitTime;
    private final double amountPaid;

    public ParkingRecord(String licensePlate, LocalDateTime entryTime, LocalDateTime exitTime, double amountPaid) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo no puede ser nula o vacía.");
        }
        if (entryTime == null || exitTime == null) {
            throw new IllegalArgumentException("Las fechas de entrada y salida son obligatorias.");
        }
        if (!entryTime.toLocalDate().equals(exitTime.toLocalDate())) {
            throw new IllegalArgumentException("Al final del día todos los autos deben haber salido en la misma jornada.");
        }
        if (exitTime.isBefore(entryTime) || exitTime.isEqual(entryTime)) {
            throw new IllegalArgumentException("La hora de salida debe ser estrictamente posterior a la hora de entrada.");
        }
        if (amountPaid <= 0) {
            throw new IllegalArgumentException("El monto pagado debe ser mayor que cero.");
        }

        this.licensePlate = licensePlate.trim().toUpperCase();
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.amountPaid = amountPaid;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public long getParkedMinutes() {
        return Duration.between(entryTime, exitTime).toMinutes();
    }

    @Override
    public String toString() {
        return String.format("Auto [Placa: %-7s | Entrada: %s | Salida: %s | Minutos: %3d | Pagó: $%,.2f]",
                licensePlate,
                entryTime.format(FORMATTER),
                exitTime.format(FORMATTER),
                getParkedMinutes(),
                amountPaid);
    }
}
