// Archivo: TrafficLightGuiApp.java | Paquete: co.edu.udistrital.state.view
package co.edu.udistrital.state.view;

import co.edu.udistrital.state.controller.Client;
import co.edu.udistrital.state.model.ITrafficLightObserver;
import co.edu.udistrital.state.model.SevenSegmentDecoder;
import co.edu.udistrital.state.model.TrafficLightColor;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Vista Gráfica en JavaFX que implementa {@link ITrafficLightObserver} (Patrón Observer)
 * para mostrar exclusivamente el semáforo moderno de 2 lámparas multicolor:
 * - Lámpara 1 (Superior): Display de 7 segmentos de 2 dígitos que alterna en los 3 colores.
 * - Lámpara 2 (Inferior): Lámpara luminosa circular que alterna en los 3 colores.
 */
public class TrafficLightGuiApp extends Application implements ITrafficLightObserver {

    private static Client sharedClient;

    private SevenSegmentDigitNode tensDigitNode;
    private SevenSegmentDigitNode unitsDigitNode;
    private Circle timerLampHousing;
    private Circle mainSignalLamp;
    private DropShadow mainLampGlow;

    private Timeline autoCycleTimeline;

    /**
     * Lanza la interfaz gráfica de JavaFX inyectando el controlador/orquestador previamente ensamblado.
     *
     * @param client Instancia del controlador Client configurado desde el Main.
     * @param args   Argumentos de línea de comandos.
     */
    public static void launchGui(Client client, String[] args) {
        if (client == null) {
            throw new IllegalArgumentException("El controlador Client no puede ser nulo para iniciar la GUI.");
        }
        sharedClient = client;
        Application.launch(TrafficLightGuiApp.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
        sharedClient.registerObserver(this);

        VBox trafficLightHousing = createModernTwoLampTrafficLight();

        StackPane root = new StackPane(trafficLightHousing);
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: #0f172a;");

        setupTimeline();
        sharedClient.refreshObservers();

        Scene scene = new Scene(root, 310, 520);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> {
            if (autoCycleTimeline != null) {
                autoCycleTimeline.stop();
            }
            sharedClient.unregisterObserver(this);
        });
        primaryStage.show();
    }

    private VBox createModernTwoLampTrafficLight() {
        InnerShadow housingDepth = new InnerShadow();
        housingDepth.setRadius(18);
        housingDepth.setColor(Color.BLACK);

        // Lámpara 1 (Superior): Display de 7 Segmentos (Alterna en los 3 colores)
        timerLampHousing = new Circle(92);
        timerLampHousing.setFill(Color.web("#090d16"));
        timerLampHousing.setStroke(Color.web("#334155"));
        timerLampHousing.setStrokeWidth(6);
        timerLampHousing.setEffect(housingDepth);

        tensDigitNode = new SevenSegmentDigitNode();
        unitsDigitNode = new SevenSegmentDigitNode();

        HBox digitsContainer = new HBox(12, tensDigitNode, unitsDigitNode);
        digitsContainer.setAlignment(Pos.CENTER);

        StackPane upperLampStack = new StackPane(timerLampHousing, digitsContainer);

        // Lámpara 2 (Inferior): Luz Principal (Alterna en los 3 colores)
        Circle lowerLampRim = new Circle(92);
        lowerLampRim.setFill(Color.web("#090d16"));
        lowerLampRim.setStroke(Color.web("#334155"));
        lowerLampRim.setStrokeWidth(6);
        lowerLampRim.setEffect(housingDepth);

        mainSignalLamp = new Circle(76);
        mainLampGlow = new DropShadow();
        mainLampGlow.setRadius(38);
        mainLampGlow.setSpread(0.45);
        mainSignalLamp.setEffect(mainLampGlow);

        StackPane lowerLampStack = new StackPane(lowerLampRim, mainSignalLamp);

        // Gabinete del semáforo de 2 lámparas
        VBox cabinet = new VBox(24, upperLampStack, lowerLampStack);
        cabinet.setAlignment(Pos.CENTER);
        cabinet.setPadding(new Insets(26, 30, 26, 30));
        cabinet.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
        cabinet.setStyle(
                "-fx-background-color: linear-gradient(to right, #1e293b, #0f172a, #1e293b);"
                + "-fx-background-radius: 42;"
                + "-fx-border-color: #475569;"
                + "-fx-border-width: 4;"
                + "-fx-border-radius: 42;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 24, 0.25, 0, 8);"
        );

        return cabinet;
    }

    private void setupTimeline() {
        autoCycleTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> sharedClient.advanceOneSecond())
        );
        autoCycleTimeline.setCycleCount(Timeline.INDEFINITE);
        autoCycleTimeline.play();
    }

    @Override
    public void onTrafficLightUpdate(TrafficLightColor activeColor, int remainingSeconds) {
        Runnable uiUpdate = () -> {
            int[] digits = SevenSegmentDecoder.splitTwoDigits(remainingSeconds);

            // Actualizar Lámpara 1 (Display de 7 segmentos en el color actual)
            tensDigitNode.updateDigit(digits[0], activeColor);
            unitsDigitNode.updateDigit(digits[1], activeColor);
            timerLampHousing.setStroke(Color.web(activeColor.getActiveHexColor(), 0.55));

            // Actualizar Lámpara 2 (Luz principal en el color actual)
            Color primaryColor = Color.web(activeColor.getActiveHexColor());
            RadialGradient lampGradient = new RadialGradient(
                    0, 0, 0.5, 0.5, 0.55, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0, primaryColor.brighter()),
                    new Stop(0.75, primaryColor),
                    new Stop(1.0, primaryColor.darker())
            );
            mainSignalLamp.setFill(lampGradient);
            mainLampGlow.setColor(primaryColor);
        };

        if (Platform.isFxApplicationThread()) {
            uiUpdate.run();
        } else {
            Platform.runLater(uiUpdate);
        }
    }
}
