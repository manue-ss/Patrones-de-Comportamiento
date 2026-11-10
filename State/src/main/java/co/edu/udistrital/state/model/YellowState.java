// Archivo: YellowState.java | Paquete: co.edu.udistrital.state.model
package co.edu.udistrital.state.model;

/**
 * Estado Concreto: Luz Amarilla (Precaución / Desaceleración).
 * Ambas lámparas (el display de 7 segmentos y la lámpara principal) se iluminan en color AMARILLO.
 * Al agotarse su tiempo, transiciona hacia {@link RedState}.
 */
public class YellowState extends AbstractTrafficLightState {

    public static final int DEFAULT_DURATION = 4;

    public YellowState() {
        this(DEFAULT_DURATION);
    }

    public YellowState(int durationSeconds) {
        super(
                "ESTADO AMARILLO (PRECAUCIÓN)",
                TrafficLightColor.YELLOW,
                durationSeconds,
                "PRECAUCIÓN: Reduzca la velocidad, próximo cambio a luz roja."
        );
    }

    @Override
    public void nextState(ITrafficLightContext context) {
        validateContext(context);
        context.setState(new RedState());
    }
}
