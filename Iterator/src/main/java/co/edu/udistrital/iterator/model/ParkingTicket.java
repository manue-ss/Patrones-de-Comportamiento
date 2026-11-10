// Archivo: ParkingTicket.java | Paquete: co.edu.udistrital.iterator.model
package co.edu.udistrital.iterator.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Entidad de dominio rica que representa el registro de parqueo de un vehículo.
 */
public class ParkingTicket {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final String licensePlate;
    private final String vehicleType;
    private final LocalDateTime entryTime;
    private final double ratePerMinute;
    private LocalDateTime exitTime;

    /**
     * Crea un nuevo tiquete de parqueadero validando sus invariantes de negocio.
     *
     * @param licensePlate  placa del vehículo
     * @param vehicleType   tipo de vehículo (Automóvil, Motocicleta, Camioneta, etc.)
     * @param entryTime     fecha y hora de ingreso
     * @param ratePerMinute tarifa por minuto en pesos
     */
    public ParkingTicket(String licensePlate, String vehicleType, LocalDateTime entryTime, double ratePerMinute) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo no puede ser nula ni vacía.");
        }
        if (vehicleType == null || vehicleType.trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de vehículo no puede ser nulo ni vacío.");
        }
        if (entryTime == null) {
            throw new IllegalArgumentException("La hora de ingreso no puede ser nula.");
        }
        if (ratePerMinute <= 0) {
            throw new IllegalArgumentException("La tarifa por minuto debe ser estrictamente positiva.");
        }

        this.licensePlate = licensePlate.trim().toUpperCase();
        this.vehicleType = vehicleType.trim();
        this.entryTime = entryTime;
        this.ratePerMinute = ratePerMinute;
        this.exitTime = null;
    }

    /**
     * Registra la salida del vehículo cerrando su cuenta.
     *
     * @param exitTime fecha y hora de salida
     */
    public void registerExit(LocalDateTime exitTime) {
        if (exitTime == null) {
            throw new IllegalArgumentException("La hora de salida no puede ser nula.");
        }
        if (!isActive()) {
            throw new IllegalStateException("El vehículo con placa " + licensePlate + " ya registró su salida.");
        }
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("La hora de salida no puede ser anterior a la hora de ingreso.");
        }
        this.exitTime = exitTime;
    }

    /**
     * Indica si el vehículo aún permanece dentro del parqueadero.
     *
     * @return true si sigue estacionado, false si ya salió
     */
    public boolean isActive() {
        return exitTime == null;
    }

    /**
     * Calcula los minutos transcurridos desde la entrada hasta la salida o hasta el tiempo de corte dado.
     *
     * @param referenceTime hora de corte para vehículos aún estacionados
     * @return cantidad de minutos transcurridos (mínimo 1 minuto)
     */
    public long getElapsedMinutes(LocalDateTime referenceTime) {
        if (referenceTime == null) {
            throw new IllegalArgumentException("La hora de referencia no puede ser nula.");
        }
        LocalDateTime effectiveEnd = isActive() ? referenceTime : exitTime;
        if (effectiveEnd.isBefore(entryTime)) {
            throw new IllegalArgumentException("La hora de referencia es anterior a la hora de ingreso del vehículo " + licensePlate + ".");
        }
        long minutes = Duration.between(entryTime, effectiveEnd).toMinutes();
        return Math.max(1L, minutes);
    }

    /**
     * Calcula el valor a cobrar según el tiempo de permanencia y la tarifa por minuto.
     *
     * @param referenceTime hora de corte para el cálculo
     * @return monto total en pesos
     */
    public double calculateFee(LocalDateTime referenceTime) {
        return getElapsedMinutes(referenceTime) * ratePerMinute;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getRatePerMinute() {
        return ratePerMinute;
    }

    /**
     * Genera un resumen legible del tiquete con respecto a una hora de corte.
     *
     * @param referenceTime hora de referencia actual
     * @return descripción formateada del estado y cobro del vehículo
     */
    public String formatSummary(LocalDateTime referenceTime) {
        String status = isActive()
                ? "EN PARQUEADERO"
                : "SALIÓ (" + exitTime.format(TIME_FORMATTER) + ")";
        return String.format(
                "Placa: %-7s | Tipo: %-11s | Ingreso: %s | Estado: %-20s | Tiempo: %3d min | Tarifa/min: $%6.2f | Total: $%,10.2f",
                licensePlate,
                vehicleType,
                entryTime.format(TIME_FORMATTER),
                status,
                getElapsedMinutes(referenceTime),
                ratePerMinute,
                calculateFee(referenceTime)
        );
    }
}
