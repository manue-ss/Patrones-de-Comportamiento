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
     * @param activeColor      Color actual que exhiben ambas lámparas (7 segmentos y lámpara plena).
     * @param remainingSeconds Segundos restantes para el cambio de estado mostrados en la lámpara de 7 segmentos.
     */
    void onTrafficLightUpdate(TrafficLightColor activeColor, int remainingSeconds);
}
