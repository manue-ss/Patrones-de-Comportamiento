// Archivo: AbstractTrafficLightState.java | Paquete: co.edu.udistrital.state.model
package co.edu.udistrital.state.model;

/**
 * Clase base abstracta para los estados concretos del semáforo.
 * Protege las invariantes de negocio (duración válida para un display de 7 segmentos de 2 dígitos
 * y referencias no nulas) cumpliendo con los principios SRP y DRY.
 */
public abstract class AbstractTrafficLightState implements ITrafficLightState {

    public static final int MIN_DURATION_SECONDS = 1;
    public static final int MAX_DURATION_SECONDS = 99;

    protected final String stateName;
    protected final TrafficLightColor color;
    protected final int durationSeconds;
    protected final String actionMessage;

    protected AbstractTrafficLightState(
            String stateName,
            TrafficLightColor color,
            int durationSeconds,
            String actionMessage
    ) {
        if (stateName == null || stateName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado del semáforo no puede ser nulo ni vacío.");
        }
        if (color == null) {
            throw new IllegalArgumentException("El color asociado al estado no puede ser nulo.");
        }
        if (durationSeconds < MIN_DURATION_SECONDS || durationSeconds > MAX_DURATION_SECONDS) {
            throw new IllegalArgumentException(
                    "La duración del estado debe estar entre " + MIN_DURATION_SECONDS
                    + " y " + MAX_DURATION_SECONDS + " segundos para el display de 7 segmentos (recibido: "
                    + durationSeconds + ")."
            );
        }
        if (actionMessage == null || actionMessage.trim().isEmpty()) {
            throw new IllegalArgumentException("El mensaje de acción vial no puede ser nulo ni vacío.");
        }
        this.stateName = stateName.trim();
        this.color = color;
        this.durationSeconds = durationSeconds;
        this.actionMessage = actionMessage.trim();
    }

    /**
     * Valida que el contexto recibido para una transición de estado no sea nulo.
     *
     * @param context Contexto a validar.
     */
    protected void validateContext(ITrafficLightContext context) {
        if (context == null) {
            throw new IllegalArgumentException("El contexto del semáforo no puede ser nulo al cambiar de estado.");
        }
    }

    @Override
    public String getStateName() {
        return stateName;
    }

    @Override
    public TrafficLightColor getColor() {
        return color;
    }

    @Override
    public int getDurationSeconds() {
        return durationSeconds;
    }

    @Override
    public String getActionMessage() {
        return actionMessage;
    }
}
