// Archivo: ITrafficLightObserver.java | Paquete: co.edu.udistrital.state.model
package co.edu.udistrital.state.model;

/**
 * Interfaz del patrón Observer (Suscriptor) implementada paralelamente al patrón State.
 * Permite notificar a la vista (GUI JavaFX y Consola) cada vez que el semáforo cambia de estado
 * o actualiza el contador regresivo de su display de 7 segmentos.
 */
public interface ITrafficLightObserver {

    /**
     * Se invoca automáticamente cuando el semáforo avanza un segundo o realiza una transición de estado.
     *
     * @param stateName        Nombre descriptivo del estado actual.
     * @param activeColor      Color actual que exhiben ambas lámparas (7 segmentos y lámpara plena).
     * @param remainingSeconds Segundos restantes para el cambio de estado mostrados en la lámpara de 7 segmentos.
     * @param totalDuration    Duración total configurada para el estado actual.
     * @param actionMessage    Instrucción vial asociada al estado actual.
     */
    void onTrafficLightUpdate(
            String stateName,
            TrafficLightColor activeColor,
            int remainingSeconds,
            int totalDuration,
            String actionMessage
    );
}
