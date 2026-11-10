// Archivo: ParkingLot.java | Paquete: co.edu.udistrital.memento.model
package co.edu.udistrital.memento.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Originator concreto que representa el sistema de parqueadero.
 * Gestiona el registro de vehículos (fecha de entrada, fecha de salida y pago),
 * el cierre del día (donde todos los autos ya salieron) y la creación/restauración de Mementos
 * mediante una clase anidada con constructor y getters estrictamente privados.
 */
public class ParkingLot implements IOriginator {

    private final String name;
    private LocalDate operatingDate;
    private final List<ParkingRecord> records;
    private double totalRevenue;
    private boolean dayClosed;

    public ParkingLot(String name, LocalDate operatingDate) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del parqueadero no puede ser nulo o vacío.");
        }
        if (operatingDate == null) {
            throw new IllegalArgumentException("La fecha de operación inicial no puede ser nula.");
        }
        this.name = name.trim();
        this.operatingDate = operatingDate;
        this.records = new ArrayList<>();
        this.totalRevenue = 0.0;
        this.dayClosed = false;
    }

    @Override
    public void registerCarExit(String licensePlate, LocalDateTime entryTime, LocalDateTime exitTime, double amountPaid) {
        if (dayClosed) {
            throw new IllegalStateException("La jornada del día " + operatingDate + " ya está cerrada. No se admiten más registros.");
        }
        if (entryTime != null && !entryTime.toLocalDate().equals(operatingDate)) {
            throw new IllegalArgumentException("La fecha de entrada (" + entryTime.toLocalDate()
                    + ") no coincide con la jornada operativa actual (" + operatingDate + ").");
        }

        ParkingRecord record = new ParkingRecord(licensePlate, entryTime, exitTime, amountPaid);
        this.records.add(record);
        this.totalRevenue += record.getAmountPaid();
    }

    @Override
    public void closeDay() {
        if (dayClosed) {
            throw new IllegalStateException("El cierre del día " + operatingDate + " ya fue realizado previamente.");
        }
        if (records.isEmpty()) {
            throw new IllegalStateException("No se puede cerrar el día sin haber registrado al menos un vehículo.");
        }
        this.dayClosed = true;
    }

    @Override
    public void startNewDay(LocalDate newDate) {
        if (newDate == null) {
            throw new IllegalArgumentException("La nueva fecha operativa no puede ser nula.");
        }
        if (!newDate.isAfter(this.operatingDate)) {
            throw new IllegalArgumentException("La nueva jornada debe ser posterior a la fecha actual (" + this.operatingDate + ").");
        }
        this.operatingDate = newDate;
        this.records.clear();
        this.totalRevenue = 0.0;
        this.dayClosed = false;
    }

    @Override
    public IMemento saveToMemento(String checkpointLabel) {
        return new ParkingMemento(this.operatingDate, this.records, this.totalRevenue, this.dayClosed, checkpointLabel);
    }

    @Override
    public void restoreFromMemento(IMemento memento) {
        if (memento == null) {
            throw new IllegalArgumentException("El Memento proporcionado para restaurar no puede ser nulo.");
        }
        if (!(memento instanceof ParkingMemento)) {
            throw new IllegalArgumentException("Tipo de Memento incompatible con este Originator.");
        }

        ParkingMemento concreteMemento = (ParkingMemento) memento;
        this.operatingDate = concreteMemento.getOperatingDate();
        this.records.clear();
        this.records.addAll(concreteMemento.getRecordsSnapshot());
        this.totalRevenue = concreteMemento.getTotalRevenueSnapshot();
        this.dayClosed = concreteMemento.isDayClosedSnapshot();
    }

    @Override
    public List<ParkingRecord> getRecords() {
        return Collections.unmodifiableList(records);
    }

    @Override
    public double getTotalRevenue() {
        return totalRevenue;
    }

    @Override
    public boolean isDayClosed() {
        return dayClosed;
    }

    @Override
    public String getStateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------------------------------------------------\n");
        sb.append(String.format("PARQUEADERO: %s | Jornada: %s | Estado: %s\n",
                name, operatingDate, dayClosed ? "CERRADO (Todos los autos salieron)" : "EN OPERACIÓN"));
        sb.append("------------------------------------------------------------------------------------\n");
        if (records.isEmpty()) {
            sb.append("  [Sin registros de vehículos en este estado]\n");
        } else {
            for (int i = 0; i < records.size(); i++) {
                sb.append(String.format("  %d. %s\n", i + 1, records.get(i).toString()));
            }
        }
        sb.append(String.format("TOTAL AUTOS ATENDIDOS: %d | RECAUDO TOTAL DEL DÍA: $%,.2f\n",
                records.size(), totalRevenue));
        sb.append("------------------------------------------------------------------------------------");
        return sb.toString();
    }

    /**
     * Clase anidada privada que implementa el Memento.
     * Su constructor y todos sus métodos de acceso al estado interno son estrictamente privados,
     * garantizando que únicamente ParkingLot (Originator) pueda instanciarlo y leer su estado.
     */
    private static final class ParkingMemento implements IMemento {

        private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        private final LocalDate operatingDate;
        private final List<ParkingRecord> recordsSnapshot;
        private final double totalRevenueSnapshot;
        private final boolean dayClosedSnapshot;
        private final String checkpointLabel;
        private final LocalDateTime savedAt;

        private ParkingMemento(LocalDate operatingDate, List<ParkingRecord> records, double totalRevenue, boolean dayClosed, String checkpointLabel) {
            if (operatingDate == null) {
                throw new IllegalArgumentException("La fecha operativa para el Memento no puede ser nula.");
            }
            if (records == null) {
                throw new IllegalArgumentException("La lista de registros para el Memento no puede ser nula.");
            }
            if (checkpointLabel == null || checkpointLabel.trim().isEmpty()) {
                throw new IllegalArgumentException("La etiqueta del punto de control no puede estar vacía.");
            }
            this.operatingDate = operatingDate;
            this.recordsSnapshot = Collections.unmodifiableList(new ArrayList<>(records));
            this.totalRevenueSnapshot = totalRevenue;
            this.dayClosedSnapshot = dayClosed;
            this.checkpointLabel = checkpointLabel.trim();
            this.savedAt = LocalDateTime.now();
        }

        private LocalDate getOperatingDate() {
            return operatingDate;
        }

        private List<ParkingRecord> getRecordsSnapshot() {
            return recordsSnapshot;
        }

        private double getTotalRevenueSnapshot() {
            return totalRevenueSnapshot;
        }

        private boolean isDayClosedSnapshot() {
            return dayClosedSnapshot;
        }

        @Override
        public String getCheckpointLabel() {
            return checkpointLabel;
        }

        @Override
        public LocalDateTime getSavedAt() {
            return savedAt;
        }

        @Override
        public String getMetadata() {
            return String.format("Instantánea ['%s' | Fecha Jornada: %s | Autos: %d | Recaudo: $%,.2f | Cierre: %s | Guardado: %s]",
                    checkpointLabel,
                    operatingDate,
                    recordsSnapshot.size(),
                    totalRevenueSnapshot,
                    dayClosedSnapshot ? "SÍ" : "NO",
                    savedAt.format(TIME_FORMATTER));
        }
    }
}
