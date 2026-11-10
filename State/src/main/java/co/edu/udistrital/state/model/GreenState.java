// Archivo: GreenState.java | Paquete: co.edu.udistrital.state.model
package co.edu.udistrital.state.model;

/**
 * Estado Concreto: Luz Verde (Vía libre).
 * Ambas lámparas (el display de 7 segmentos y la lámpara principal) se iluminan en color VERDE.
 * Al agotarse su tiempo, transiciona hacia {@link YellowState}.
 */
public class GreenState extends AbstractTrafficLightState {

    public static final int DEFAULT_DURATION = 12;

    public GreenState() {
        this(DEFAULT_DURATION);
    }

    public GreenState(int durationSeconds) {
        super(
                "ESTADO VERDE (AVANZAR)",
                TrafficLightColor.GREEN,
                durationSeconds,
                "VÍA LIBRE: Circulación vehicular activa con normalidad."
        );
    }

    @Override
    public void nextState(ITrafficLightContext context) {
        validateContext(context);
        context.setState(new YellowState());
    }
}
