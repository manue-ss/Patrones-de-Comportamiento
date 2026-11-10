// Archivo: AllTicketsIterator.java | Paquete: co.edu.udistrital.iterator.model
package co.edu.udistrital.iterator.model;

import java.util.List;

/**
 * Iterador concreto (GoF) que recorre la totalidad de tiquetes del día
 * en orden cronológico de ingreso.
 */
public class AllTicketsIterator implements ITicketIterator {

    private final List<ParkingTicket> tickets;
    private int currentIndex;

    /**
     * Construye un iterador sobre la lista completa de tiquetes del día.
     *
     * @param tickets lista interna de tiquetes
     */
    public AllTicketsIterator(List<ParkingTicket> tickets) {
        if (tickets == null) {
            throw new IllegalArgumentException("La colección de tiquetes para el iterador no puede ser nula.");
        }
        this.tickets = tickets;
        this.currentIndex = -1;
    }

    @Override
    public void reset() {
        this.currentIndex = -1;
    }

    @Override
    public boolean hasNext() {
        return (currentIndex + 1) < tickets.size();
    }

    @Override
    public ParkingTicket next() {
        if (!hasNext()) {
            return null;
        }
        currentIndex++;
        return tickets.get(currentIndex);
    }

    @Override
    public ParkingTicket first() {
        if (tickets.isEmpty()) {
            currentIndex = -1;
            return null;
        }
        currentIndex = 0;
        return tickets.get(currentIndex);
    }

    @Override
    public ParkingTicket last() {
        if (tickets.isEmpty()) {
            currentIndex = -1;
            return null;
        }
        currentIndex = tickets.size() - 1;
        return tickets.get(currentIndex);
    }

    @Override
    public ParkingTicket currentItem() {
        if (currentIndex < 0 || currentIndex >= tickets.size()) {
            return null;
        }
        return tickets.get(currentIndex);
    }
}
