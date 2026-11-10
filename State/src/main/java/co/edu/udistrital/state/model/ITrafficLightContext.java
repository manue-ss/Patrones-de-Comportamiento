// Archivo: ITrafficLightContext.java | Paquete: co.edu.udistrital.state.model
package co.edu.udistrital.state.model;

/**
 * Interfaz que define el contrato del Contexto en el patrón State y del Sujeto (Subject)
 * en el patrón Observer paralelo para el semáforo moderno de 2 lámparas.
 */
public interface ITrafficLightContext {

    /**
     * Cambia el estado actual del semáforo, reinicia la cuenta regresiva para el display
     * de 7 segmentos y notifica a todos los observadores registrados.
     *
     * @param newState Nuevo estado concreto a establecer.
     */
    void setState(ITrafficLightState newState);

    /**
     * Obtiene la referencia al estado concreto actual.
     *
     * @return Instancia del estado activo.
     */
    ITrafficLightState getState();

    /**
     * Simula el paso de un segundo en el reloj del semáforo.
     * Si el tiempo restante llega a cero, delega la transición al estado actual mediante {@code nextState()}.
     */
    void tick();

    /**
     * Fuerza inmediatamente la transición al siguiente estado del ciclo sin esperar a que termine el conteo.
     */
    void requestNextState();

    /**
     * Obtiene los segundos restantes para el próximo cambio de estado (mostrados en la lámpara de 7 segmentos).
     *
     * @return Segundos restantes (entre 1 y 99).
     */
    int getRemainingSeconds();

    /**
     * Registra un observador para recibir actualizaciones de estado y temporizador.
     *
     * @param observer Observador a suscribir.
     */
    void attachObserver(ITrafficLightObserver observer);

    /**
     * Elimina un observador previamente registrado.
     *
     * @param observer Observador a desvincular.
     */
    void detachObserver(ITrafficLightObserver observer);

    /**
     * Notifica manualmente a todos los observadores registrados sobre el estado actual.
     */
    void notifyObservers();

    /**
     * Genera un reporte estructurado del estado actual de ambas lámparas del semáforo.
     *
     * @return Cadena formateada con el detalle del estado y las dos lámparas.
     */
    String getStatusSummary();
}
