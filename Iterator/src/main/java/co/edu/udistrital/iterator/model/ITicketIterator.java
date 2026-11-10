// Archivo: ITicketIterator.java | Paquete: co.edu.udistrital.iterator.model
package co.edu.udistrital.iterator.model;

/**
 * Interfaz Iterator (GoF) para recorrer colecciones de tiquetes de parqueadero
 * sin exponer su representación interna.
 */
public interface ITicketIterator {

    /**
     * Reinicia el cursor del iterador al estado previo al primer elemento.
     */
    void reset();

    /**
     * Indica si existen más tiquetes por recorrer en la secuencia.
     *
     * @return true si hay un siguiente elemento disponible, false en caso contrario
     */
    boolean hasNext();

    /**
     * Avanza el cursor y retorna el siguiente tiquete de la colección.
     *
     * @return siguiente tiquete o null si no hay más elementos
     */
    ParkingTicket next();

    /**
     * Posiciona el iterador en el primer tiquete que ingresó y lo retorna.
     *
     * @return primer tiquete registrado o null si la secuencia está vacía
     */
    ParkingTicket first();

    /**
     * Posiciona el iterador en el último tiquete que ingresó y lo retorna.
     *
     * @return último tiquete registrado o null si la secuencia está vacía
     */
    ParkingTicket last();

    /**
     * Retorna el tiquete en la posición actual del cursor sin avanzar.
     *
     * @return tiquete actual o null si el cursor no apunta a un elemento válido
     */
    ParkingTicket currentItem();
}
