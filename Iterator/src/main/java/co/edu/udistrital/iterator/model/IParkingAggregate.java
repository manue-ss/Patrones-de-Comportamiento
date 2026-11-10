// Archivo: IParkingAggregate.java | Paquete: co.edu.udistrital.iterator.model
package co.edu.udistrital.iterator.model;

import java.time.LocalDateTime;

/**
 * Interfaz Aggregate (GoF) que define el contrato de la colección de parqueadero
 * y los métodos de creación de iteradores especializados.
 */
public interface IParkingAggregate {

    /**
     * Registra un nuevo tiquete de ingreso en el parqueadero.
     *
     * @param ticket tiquete a registrar
     */
    void addTicket(ParkingTicket ticket);

    /**
     * Registra la salida de un vehículo activo por su placa.
     *
     * @param licensePlate placa del vehículo que se retira
     * @param exitTime     hora de salida
     * @return tiquete actualizado tras la salida
     */
    ParkingTicket checkoutVehicle(String licensePlate, LocalDateTime exitTime);

    /**
     * Crea un iterador sobre todos los tiquetes registrados en el día (activos y finalizados).
     *
     * @return iterador de todos los registros del día
     */
    ITicketIterator createAllTicketsIterator();

    /**
     * Crea un iterador filtrado únicamente sobre los vehículos que permanecen en el parqueadero.
     *
     * @return iterador de tiquetes activos
     */
    ITicketIterator createActiveTicketsIterator();

    /**
     * Obtiene el nombre identificador del parqueadero.
     *
     * @return nombre del parqueadero
     */
    String getName();

    /**
     * Obtiene la capacidad máxima del parqueadero.
     *
     * @return número máximo de celdas disponibles
     */
    int getMaxCapacity();
}
