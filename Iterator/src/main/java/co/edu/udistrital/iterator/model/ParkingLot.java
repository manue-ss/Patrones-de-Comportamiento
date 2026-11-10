// Archivo: ParkingLot.java | Paquete: co.edu.udistrital.iterator.model
package co.edu.udistrital.iterator.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Agregado concreto (GoF) que administra la colección de vehículos del parqueadero
 * y fabrica los iteradores correspondientes.
 */
public class ParkingLot implements IParkingAggregate {

    private final String name;
    private final int maxCapacity;
    private final List<ParkingTicket> tickets;

    /**
     * Construye un parqueadero con nombre y capacidad máxima definidos.
     *
     * @param name        nombre del parqueadero
     * @param maxCapacity cupo máximo de vehículos simultáneos
     */
    public ParkingLot(String name, int maxCapacity) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del parqueadero no puede ser nulo ni vacío.");
        }
        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("La capacidad máxima del parqueadero debe ser mayor a cero.");
        }
        this.name = name.trim();
        this.maxCapacity = maxCapacity;
        this.tickets = new ArrayList<>();
    }

    @Override
    public void addTicket(ParkingTicket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException("No se puede registrar un tiquete nulo.");
        }
        if (countActiveVehicles() >= maxCapacity) {
            throw new IllegalStateException("El parqueadero '" + name + "' ha alcanzado su capacidad máxima (" + maxCapacity + " cupos).");
        }
        if (isVehicleCurrentlyParked(ticket.getLicensePlate())) {
            throw new IllegalArgumentException("El vehículo con placa " + ticket.getLicensePlate() + " ya se encuentra dentro del parqueadero.");
        }
        if (!tickets.isEmpty()) {
            ParkingTicket previous = tickets.get(tickets.size() - 1);
            if (ticket.getEntryTime().isBefore(previous.getEntryTime())) {
                throw new IllegalArgumentException("El ingreso debe registrarse en orden cronológico (no puede ser anterior a " + previous.getEntryTime().toLocalTime() + ").");
            }
        }
        tickets.add(ticket);
    }

    @Override
    public ParkingTicket checkoutVehicle(String licensePlate, LocalDateTime exitTime) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa para registrar salida no puede ser nula ni vacía.");
        }
        String normalizedPlate = licensePlate.trim().toUpperCase();
        ITicketIterator activeIterator = createActiveTicketsIterator();
        while (activeIterator.hasNext()) {
            ParkingTicket current = activeIterator.next();
            if (current.getLicensePlate().equals(normalizedPlate)) {
                current.registerExit(exitTime);
                return current;
            }
        }
        throw new IllegalArgumentException("No se encontró ningún vehículo activo con la placa " + normalizedPlate + " en el parqueadero.");
    }

    @Override
    public ITicketIterator createAllTicketsIterator() {
        return new AllTicketsIterator(Collections.unmodifiableList(tickets));
    }

    @Override
    public ITicketIterator createActiveTicketsIterator() {
        return new ActiveTicketsIterator(Collections.unmodifiableList(tickets));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }

    /**
     * Cuenta cuántos vehículos permanecen actualmente en el parqueadero usando su iterador.
     *
     * @return cantidad de vehículos estacionados
     */
    private int countActiveVehicles() {
        int count = 0;
        ITicketIterator iterator = createActiveTicketsIterator();
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }

    /**
     * Verifica si una placa ya tiene un ingreso activo sin salida.
     *
     * @param licensePlate placa a verificar
     * @return true si el vehículo sigue estacionado, false en caso contrario
     */
    private boolean isVehicleCurrentlyParked(String licensePlate) {
        ITicketIterator iterator = createActiveTicketsIterator();
        while (iterator.hasNext()) {
            if (iterator.next().getLicensePlate().equalsIgnoreCase(licensePlate)) {
                return true;
            }
        }
        return false;
    }
}
