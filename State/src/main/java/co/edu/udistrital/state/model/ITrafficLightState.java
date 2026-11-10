// Archivo: ITrafficLightState.java | Paquete: co.edu.udistrital.state.model
package co.edu.udistrital.state.model;

/**
 * Interfaz State (GoF) que encapsula el comportamiento específico asociado a un estado
 * particular del semáforo moderno de dos lámparas.
 */
public interface ITrafficLightState {

    /**
     * Ejecuta la transición hacia el siguiente estado lógico del ciclo del semáforo.
     *
     * @param context Contexto del semáforo cuyo estado interno será modificado.
     */
    void nextState(ITrafficLightContext context);

    /**
     * Obtiene el nombre del estado concreto.
     *
     * @return Nombre del estado en español.
     */
    String getStateName();

    /**
     * Obtiene el color que deben adoptar tanto la lámpara de color actual como la lámpara
     * del temporizador de 7 segmentos durante este estado.
     *
     * @return Color asignado al estado.
     */
    TrafficLightColor getColor();

    /**
     * Obtiene la duración en segundos que permanece activo este estado antes de conmutar.
     *
     * @return Duración en segundos (mayor a cero).
     */
    int getDurationSeconds();
}
