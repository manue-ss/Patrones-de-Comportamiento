// Archivo: Mediator.java | Paquete: co.edu.udistrital.mediator.model
package co.edu.udistrital.mediator.model;

/**
 * Interfaz central del patrón de diseño Mediator que desacopla la comunicación entre colegas.
 */
public interface Mediator {

    /**
     * Notifica al mediador sobre un evento ocurrido en un componente colega.
     *
     * @param sender  Instancia del colega que origina el evento.
     * @param event   Identificador semántico del evento.
     * @param context Objeto de transferencia o estado asociado al evento.
     */
    void notify(Colleague sender, String event, Object context);
}