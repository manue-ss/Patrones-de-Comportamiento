// Archivo: IOriginator.java | Paquete: co.edu.udistrital.memento.model
package co.edu.udistrital.memento.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interfaz que define el contrato del Originator en el patrón Memento
 * junto con las operaciones de negocio del parqueadero.
 */
public interface IOriginator {

    /**
     * Registra un vehículo con su hora de entrada, salida y pago.
     *
     * @param licensePlate Placa del auto.
     * @param entryTime    Fecha y hora de entrada.
     * @param exitTime     Fecha y hora de salida.
     * @param amountPaid   Valor pagado.
     */
    void registerCarExit(String licensePlate, LocalDateTime entryTime, LocalDateTime exitTime, double amountPaid);

    /**
     * Ejecuta el cierre del día asumiendo que todos los autos ya salieron.
     */
    void closeDay();

    /**
     * Inicia una nueva jornada operativa limpiando los registros del día anterior.
     *
     * @param newDate Nueva fecha de operación.
     */
    void startNewDay(LocalDate newDate);

    /**
     * Crea un Memento con la instantánea del estado actual del parqueadero.
     *
     * @param checkpointLabel Etiqueta descriptiva del estado guardado.
     * @return Instancia de IMemento con el estado encapsulado.
     */
    IMemento saveToMemento(String checkpointLabel);

    /**
     * Restaura el estado interno del parqueadero a partir de un Memento válido.
     *
     * @param memento Instantánea previa a restaurar.
     */
    void restoreFromMemento(IMemento memento);

    /**
     * Obtiene una vista inmutable de los registros de vehículos actuales.
     *
     * @return Lista de registros de parqueo.
     */
    List<ParkingRecord> getRecords();

    /**
     * Obtiene el total recaudado en la jornada actual.
     *
     * @return Total recaudado.
     */
    double getTotalRevenue();

    /**
     * Indica si la jornada actual ya fue cerrada.
     *
     * @return true si el día está cerrado, false en caso contrario.
     */
    boolean isDayClosed();

    /**
     * Obtiene un reporte detallado del estado actual del parqueadero.
     *
     * @return Resumen de estado.
     */
    String getStateReport();
}
