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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.BorderPane;
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
 * para representar el semáforo moderno de 2 lámparas multicolor:
 * - Lámpara 1 (Superior): Display de 7 segmentos de 2 dígitos que alterna entre Rojo, Amarillo y Verde.
 * - Lámpara 2 (Inferior): Lámpara luminosa circular que alterna entre Rojo, Amarillo y Verde.
 */
public class TrafficLightGuiApp extends Application implements ITrafficLightObserver {

    private static Client sharedClient;

    private SevenSegmentDigitNode tensDigitNode;
    private SevenSegmentDigitNode unitsDigitNode;
    private Circle timerLampHousing;
    private Circle mainSignalLamp;
    private DropShadow mainLampGlow;

    private Label intersectionLabel;
    private Label stateBadgeLabel;
    private Label directiveLabel;
    private Label lampDetailLabel;
    private ProgressBar countdownProgressBar;

    private Timeline autoCycleTimeline;
    private boolean isAutoRunning = true;

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
        // Suscribir la vista JavaFX como observador del modelo a través del controlador
        sharedClient.registerObserver(this);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #0f172a, #1e293b);");

        // Panel superior: Encabezado e información del patrón State + Observer
        VBox headerBox = createHeaderSection();
        root.setTop(headerBox);

        // Panel central: Cuerpo del semáforo moderno de 2 lámparas + Panel de telemetría
        HBox centerContent = new HBox(35);
        centerContent.setAlignment(Pos.CENTER);
        centerContent.setPadding(new Insets(20, 10, 20, 10));

        VBox trafficLightHousing = createModernTwoLampTrafficLight();
        VBox telemetryAndControlsPanel = createTelemetryAndControlsPanel();

        centerContent.getChildren().addAll(trafficLightHousing, telemetryAndControlsPanel);
        root.setCenter(centerContent);

        // Configurar el reloj automático de 1 segundo (Timeline JavaFX)
        setupTimeline();

        // Sincronizar la interfaz con el estado actual del modelo inmediatamente
        sharedClient.refreshObservers();

        Scene scene = new Scene(root, 860, 620);
        primaryStage.setTitle("Patrón State & Observer - Semáforo Inteligente de 2 Lámparas (JavaFX)");
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

    private VBox createHeaderSection() {
        Label titleLabel = new Label("SEMÁFORO MODERNO DE 2 LÁMPARAS MULTICOLOR");
        titleLabel.setStyle("-fx-text-fill: #f8fafc; -fx-font-size: 21px; -fx-font-weight: bold;");

        intersectionLabel = new Label("Intersección: " + sharedClient.getIntersectionName());
        intersectionLabel.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 14px;");

        VBox header = new VBox(5, titleLabel, intersectionLabel);
        header.setAlignment(Pos.CENTER);
        return header;
    }

    private VBox createModernTwoLampTrafficLight() {
        // =====================================================================
        // LÁMPARA 1 (SUPERIOR): Display de 7 Segmentos (Alterna en los 3 colores)
        // =====================================================================
        timerLampHousing = new Circle(92);
        timerLampHousing.setFill(Color.web("#090d16"));
        timerLampHousing.setStroke(Color.web("#334155"));
        timerLampHousing.setStrokeWidth(6);

        InnerShadow housingDepth = new InnerShadow();
        housingDepth.setRadius(18);
        housingDepth.setColor(Color.BLACK);
        timerLampHousing.setEffect(housingDepth);

        tensDigitNode = new SevenSegmentDigitNode();
        unitsDigitNode = new SevenSegmentDigitNode();

        HBox digitsContainer = new HBox(12, tensDigitNode, unitsDigitNode);
        digitsContainer.setAlignment(Pos.CENTER);

        StackPane upperLampStack = new StackPane(timerLampHousing, digitsContainer);

        Label lampOneCaption = new Label("LÁMPARA 1: DISPLAY 7 SEGMENTOS");
        lampOneCaption.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 11px; -fx-font-weight: bold;");

        // =====================================================================
        // LÁMPARA 2 (INFERIOR): Luz Principal (Alterna en los 3 colores)
        // =====================================================================
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

        Label lampTwoCaption = new Label("LÁMPARA 2: COLOR ACTUAL");
        lampTwoCaption.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 11px; -fx-font-weight: bold;");

        // Gabinete negro/antracita del semáforo de 2 cavidades
        VBox cabinet = new VBox(14, lampOneCaption, upperLampStack, lampTwoCaption, lowerLampStack);
        cabinet.setAlignment(Pos.CENTER);
        cabinet.setPadding(new Insets(22, 32, 22, 32));
        cabinet.setStyle(
                "-fx-background-color: linear-gradient(to right, #1e293b, #0f172a, #1e293b);"
                + "-fx-background-radius: 38;"
                + "-fx-border-color: #475569;"
                + "-fx-border-width: 4;"
                + "-fx-border-radius: 38;"
                + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 24, 0.2, 0, 8);"
        );

        return cabinet;
    }

    private VBox createTelemetryAndControlsPanel() {
        Label panelTitle = new Label("TELEMETRÍA DE ESTADO (STATE + OBSERVER)");
        panelTitle.setStyle("-fx-text-fill: #38bdf8; -fx-font-size: 15px; -fx-font-weight: bold;");

        stateBadgeLabel = new Label();
        stateBadgeLabel.setAlignment(Pos.CENTER);
        stateBadgeLabel.setMaxWidth(Double.MAX_VALUE);
        stateBadgeLabel.setPadding(new Insets(10));
        stateBadgeLabel.setStyle(
                "-fx-background-color: #1e293b; -fx-text-fill: white; "
                + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 8;"
        );

        directiveLabel = new Label();
        directiveLabel.setWrapText(true);
        directiveLabel.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 13px;");

        lampDetailLabel = new Label();
        lampDetailLabel.setWrapText(true);
        lampDetailLabel.setStyle("-fx-text-fill: #94a3b8; -fx-font-size: 12px;");

        countdownProgressBar = new ProgressBar(1.0);
        countdownProgressBar.setMaxWidth(Double.MAX_VALUE);
        countdownProgressBar.setPrefHeight(16);

        // Controles de interacción del patrón State
        Label controlsTitle = new Label("CONTROLES DE TRANSICIÓN Y RELOJ");
        controlsTitle.setStyle("-fx-text-fill: #38bdf8; -fx-font-size: 14px; -fx-font-weight: bold;");

        Button pauseResumeBtn = createStyledButton("Pausar Reloj Automático", "#0284c7");
        pauseResumeBtn.setOnAction(e -> {
            if (isAutoRunning) {
                autoCycleTimeline.pause();
                isAutoRunning = false;
                pauseResumeBtn.setText("Reanudar Reloj Automático");
                pauseResumeBtn.setStyle(getButtonStyle("#16a34a"));
            } else {
                autoCycleTimeline.play();
                isAutoRunning = true;
                pauseResumeBtn.setText("Pausar Reloj Automático");
                pauseResumeBtn.setStyle(getButtonStyle("#0284c7"));
            }
        });

        Button manualTickBtn = createStyledButton("Avanzar +1 Segundo (Tick Manual)", "#4f46e5");
        manualTickBtn.setOnAction(e -> sharedClient.advanceOneSecond());

        Button forceTransitionBtn = createStyledButton("Forzar Cambio de Estado (nextState)", "#7c3aed");
        forceTransitionBtn.setOnAction(e -> sharedClient.forceStateTransition());

        Button resetTimerBtn = createStyledButton("Reiniciar Contador del Estado Actual", "#475569");
        resetTimerBtn.setOnAction(e -> sharedClient.resetStateTimer());

        // Botones para inyectar un estado específico directamente
        Label directStateTitle = new Label("CONMUTACIÓN DIRECTA DE ESTADO:");
        directStateTitle.setStyle("-fx-text-fill: #cbd5e1; -fx-font-size: 12px; -fx-font-weight: bold;");

        Button forceRedBtn = createStyledButton("Rojo", "#dc2626");
        forceRedBtn.setOnAction(e -> sharedClient.changeToRedState());

        Button forceGreenBtn = createStyledButton("Verde", "#16a34a");
        forceGreenBtn.setOnAction(e -> sharedClient.changeToGreenState());

        Button forceYellowBtn = createStyledButton("Amarillo", "#ca8a04");
        forceYellowBtn.setOnAction(e -> sharedClient.changeToYellowState());

        HBox directStateButtons = new HBox(10, forceRedBtn, forceGreenBtn, forceYellowBtn);
        directStateButtons.setAlignment(Pos.CENTER);

        VBox controlsContainer = new VBox(
                12,
                panelTitle,
                stateBadgeLabel,
                directiveLabel,
                lampDetailLabel,
                countdownProgressBar,
                controlsTitle,
                pauseResumeBtn,
                manualTickBtn,
                forceTransitionBtn,
                resetTimerBtn,
                directStateTitle,
                directStateButtons
        );
        controlsContainer.setPrefWidth(430);
        controlsContainer.setPadding(new Insets(22));
        controlsContainer.setStyle(
                "-fx-background-color: rgba(30, 41, 59, 0.85);"
                + "-fx-background-radius: 20;"
                + "-fx-border-color: #334155;"
                + "-fx-border-width: 2;"
                + "-fx-border-radius: 20;"
        );

        return controlsContainer;
    }

    private Button createStyledButton(String text, String hexColor) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle(getButtonStyle(hexColor));
        return button;
    }

    private String getButtonStyle(String hexColor) {
        return "-fx-background-color: " + hexColor + "; "
                + "-fx-text-fill: white; "
                + "-fx-font-weight: bold; "
                + "-fx-font-size: 12px; "
                + "-fx-padding: 9 14 9 14; "
                + "-fx-background-radius: 8; "
                + "-fx-cursor: hand;";
    }

    private void setupTimeline() {
        autoCycleTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> sharedClient.advanceOneSecond())
        );
        autoCycleTimeline.setCycleCount(Timeline.INDEFINITE);
        autoCycleTimeline.play();
    }

    @Override
    public void onTrafficLightUpdate(
            String stateName,
            TrafficLightColor activeColor,
            int remainingSeconds,
            int totalDuration,
            String actionMessage
    ) {
        Runnable uiUpdate = () -> {
            int[] digits = SevenSegmentDecoder.splitTwoDigits(remainingSeconds);

            // 1. Actualizar Lámpara 1 (Display de 7 segmentos en el color del estado actual)
            tensDigitNode.updateDigit(digits[0], activeColor);
            unitsDigitNode.updateDigit(digits[1], activeColor);
            timerLampHousing.setStroke(Color.web(activeColor.getActiveHexColor(), 0.55));

            // 2. Actualizar Lámpara 2 (Luz principal en el color del estado actual con gradiente radial)
            Color primaryColor = Color.web(activeColor.getActiveHexColor());
            RadialGradient lampGradient = new RadialGradient(
                    0, 0, 0.5, 0.5, 0.55, true, CycleMethod.NO_CYCLE,
                    new Stop(0.0, primaryColor.brighter()),
                    new Stop(0.75, primaryColor),
                    new Stop(1.0, primaryColor.darker())
            );
            mainSignalLamp.setFill(lampGradient);
            mainLampGlow.setColor(primaryColor);

            // 3. Actualizar etiquetas de telemetría y barra de progreso
            stateBadgeLabel.setText(stateName + "  [" + String.format("%02d", remainingSeconds) + " s]");
            stateBadgeLabel.setStyle(
                    "-fx-background-color: " + activeColor.getDimHexColor() + "; "
                    + "-fx-border-color: " + activeColor.getActiveHexColor() + "; "
                    + "-fx-border-width: 2; -fx-border-radius: 8; -fx-background-radius: 8; "
                    + "-fx-text-fill: " + activeColor.getActiveHexColor() + "; "
                    + "-fx-font-size: 15px; -fx-font-weight: bold;"
            );

            directiveLabel.setText("Directriz: " + actionMessage);
            lampDetailLabel.setText(
                    "• Lámpara 1 (7 Segmentos): Mostrando '" + String.format("%02d", remainingSeconds)
                    + "' en color " + activeColor.getDisplayName() + "\n"
                    + "• Lámpara 2 (Luz Principal): Iluminada en color " + activeColor.getDisplayName()
                    + " (" + activeColor.getActiveHexColor() + ")"
            );

            double progress = totalDuration > 0 ? (double) remainingSeconds / totalDuration : 0.0;
            countdownProgressBar.setProgress(progress);
            countdownProgressBar.setStyle("-fx-accent: " + activeColor.getActiveHexColor() + ";");
        };

        if (Platform.isFxApplicationThread()) {
            uiUpdate.run();
        } else {
            Platform.runLater(uiUpdate);
        }
    }
}
