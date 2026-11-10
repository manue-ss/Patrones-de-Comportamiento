// Archivo: RedState.java | Paquete: co.edu.udistrital.state.model
package co.edu.udistrital.state.model;

/**
 * Estado Concreto: Luz Roja (Detención total).
 * Ambas lámparas (el display de 7 segmentos y la lámpara principal) se iluminan en color ROJO.
 * Al agotarse su tiempo, transiciona hacia {@link GreenState}.
 */
public class RedState extends AbstractTrafficLightState {

    public static final int DEFAULT_DURATION = 10;

    public RedState() {
        this(DEFAULT_DURATION);
    }

    public RedState(int durationSeconds) {
        super(
                "ESTADO ROJO (DETENERSE)",
                TrafficLightColor.RED,
                durationSeconds,
                "ALTO TOTAL: Vehículos detenidos. Paso peatonal habilitado."
        );
    }

    @Override
    public void nextState(ITrafficLightContext context) {
        validateContext(context);
        context.setState(new GreenState());
    }
}
