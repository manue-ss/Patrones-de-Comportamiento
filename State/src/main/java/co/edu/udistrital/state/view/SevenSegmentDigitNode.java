// Archivo: SevenSegmentDigitNode.java | Paquete: co.edu.udistrital.state.view
package co.edu.udistrital.state.view;

import co.edu.udistrital.state.model.SevenSegmentDecoder;
import co.edu.udistrital.state.model.TrafficLightColor;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Componente visual personalizado en JavaFX que dibuja un dígito electrónico de 7 segmentos
 * (segmentos A, B, C, D, E, F, G) con capacidad de alternar entre los 3 colores del semáforo
 * (Rojo, Amarillo y Verde).
 */
public class SevenSegmentDigitNode extends Pane {

    private static final double DIGIT_WIDTH = 52.0;
    private static final double DIGIT_HEIGHT = 92.0;
    private static final double THICKNESS = 9.0;
    private static final double SEGMENT_LENGTH = 32.0;
    private static final double CORNER_ARC = 6.0;

    private final Rectangle[] segments;
    private final DropShadow activeGlow;

    public SevenSegmentDigitNode() {
        setPrefSize(DIGIT_WIDTH, DIGIT_HEIGHT);
        setMinSize(DIGIT_WIDTH, DIGIT_HEIGHT);
        setMaxSize(DIGIT_WIDTH, DIGIT_HEIGHT);

        this.segments = new Rectangle[7];
        this.activeGlow = new DropShadow();
        this.activeGlow.setRadius(10.0);
        this.activeGlow.setSpread(0.35);

        initializeSegments();
    }

    private void initializeSegments() {
        // Segmento A (Superior horizontal)
        segments[0] = createHorizontalSegment(10.0, 2.0);
        // Segmento B (Superior derecho vertical)
        segments[1] = createVerticalSegment(41.0, 11.0);
        // Segmento C (Inferior derecho vertical)
        segments[2] = createVerticalSegment(41.0, 49.0);
        // Segmento D (Inferior horizontal)
        segments[3] = createHorizontalSegment(10.0, 81.0);
        // Segmento E (Inferior izquierdo vertical)
        segments[4] = createVerticalSegment(1.0, 49.0);
        // Segmento F (Superior izquierdo vertical)
        segments[5] = createVerticalSegment(1.0, 11.0);
        // Segmento G (Central horizontal)
        segments[6] = createHorizontalSegment(10.0, 41.5);

        getChildren().addAll(segments);
    }

    private Rectangle createHorizontalSegment(double x, double y) {
        Rectangle rect = new Rectangle(x, y, SEGMENT_LENGTH, THICKNESS);
        rect.setArcWidth(CORNER_ARC);
        rect.setArcHeight(CORNER_ARC);
        return rect;
    }

    private Rectangle createVerticalSegment(double x, double y) {
        Rectangle rect = new Rectangle(x, y, THICKNESS, SEGMENT_LENGTH);
        rect.setArcWidth(CORNER_ARC);
        rect.setArcHeight(CORNER_ARC);
        return rect;
    }

    /**
     * Actualiza los segmentos iluminados del dígito y su color activo.
     *
     * @param digit       Dígito entero a renderizar (0 a 9).
     * @param activeColor Color actual del estado del semáforo.
     */
    public void updateDigit(int digit, TrafficLightColor activeColor) {
        boolean[] pattern = SevenSegmentDecoder.encodeDigit(digit);
        Color litColor = Color.web(activeColor.getActiveHexColor());
        Color unlitColor = Color.web(activeColor.getDimHexColor());
        activeGlow.setColor(litColor);

        for (int i = 0; i < segments.length; i++) {
            if (pattern[i]) {
                segments[i].setFill(litColor);
                segments[i].setEffect(activeGlow);
            } else {
                segments[i].setFill(unlitColor);
                segments[i].setEffect(null);
            }
        }
    }
}
