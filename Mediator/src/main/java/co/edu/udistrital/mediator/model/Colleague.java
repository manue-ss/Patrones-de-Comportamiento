// Archivo: Colleague.java | Paquete: co.edu.udistrital.mediator.model
package co.edu.udistrital.mediator.model;

/**
 * Clase abstracta que representa a un participante (Colega) dentro del patrón Mediator.
 */
public abstract class Colleague {

    protected Mediator mediator;

    /**
     * Constructor por defecto para permitir inyección tardía o por constructor.
     */
    protected Colleague() {
    }

    /**
     * Constructor con inyección directa del mediador.
     *
     * @param mediator Instancia del mediador coordinador.
     */
    protected Colleague(Mediator mediator) {
        this.mediator = mediator;
    }

    /**
     * Configura o actualiza el mediador asociado a este colega.
     *
     * @param mediator Instancia del mediador.
     */
    public void setMediator(Mediator mediator) {
        if (mediator == null) {
            throw new IllegalArgumentException("El mediador inyectado no puede ser nulo.");
        }
        this.mediator = mediator;
    }

    /**
     * Obtiene la referencia actual al mediador.
     *
     * @return Instancia del mediador.
     */
    public Mediator getMediator() {
        return mediator;
    }

    /**
     * Recibe órdenes o notificaciones enviadas desde el mediador.
     *
     * @param event   Tipo de instrucción o evento emitido por el mediador.
     * @param context Datos adjuntos para procesar la instrucción.
     */
    public abstract void receive(String event, Object context);
}