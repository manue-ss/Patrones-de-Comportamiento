// Archivo: ActiveTicketsIterator.java | Paquete: co.edu.udistrital.iterator.model
package co.edu.udistrital.iterator.model;

import java.util.List;

/**
 * Iterador concreto con filtro (GoF) que recorre exclusivamente los vehículos
 * que aún se encuentran dentro del parqueadero.
 */
public class ActiveTicketsIterator implements ITicketIterator {

    private final List<ParkingTicket> tickets;
    private int currentIndex;

    /**
     * Construye un iterador filtrado sobre los vehículos activos en el parqueadero.
     *
     * @param tickets lista interna de tiquetes
     */
    public ActiveTicketsIterator(List<ParkingTicket> tickets) {
        if (tickets == null) {
            throw new IllegalArgumentException("La colección de tiquetes para el iterador activo no puede ser nula.");
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
        return findNextActiveIndex(currentIndex + 1) != -1;
    }

    @Override
    public ParkingTicket next() {
        int nextActive = findNextActiveIndex(currentIndex + 1);
        if (nextActive == -1) {
            return null;
        }
        currentIndex = nextActive;
        return tickets.get(currentIndex);
    }

    @Override
    public ParkingTicket first() {
        int firstActive = findNextActiveIndex(0);
        if (firstActive == -1) {
            currentIndex = -1;
            return null;
        }
        currentIndex = firstActive;
        return tickets.get(currentIndex);
    }

    @Override
    public ParkingTicket last() {
        for (int i = tickets.size() - 1; i >= 0; i--) {
            if (tickets.get(i).isActive()) {
                currentIndex = i;
                return tickets.get(currentIndex);
            }
        }
        currentIndex = -1;
        return null;
    }

    @Override
    public ParkingTicket currentItem() {
        if (currentIndex < 0 || currentIndex >= tickets.size()) {
            return null;
        }
        ParkingTicket ticket = tickets.get(currentIndex);
        return ticket.isActive() ? ticket : null;
    }

    /**
     * Busca el índice del siguiente tiquete activo a partir de una posición dada.
     *
     * @param startIndex índice inicial de búsqueda
     * @return índice del tiquete activo o -1 si no existe ninguno
     */
    private int findNextActiveIndex(int startIndex) {
        for (int i = Math.max(0, startIndex); i < tickets.size(); i++) {
            if (tickets.get(i).isActive()) {
                return i;
            }
        }
        return -1;
    }
}
