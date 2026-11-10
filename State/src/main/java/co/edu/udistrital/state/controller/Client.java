// Archivo: Client.java | Paquete: co.edu.udistrital.state.controller
package co.edu.udistrital.state.controller;

import co.edu.udistrital.state.model.GreenState;
import co.edu.udistrital.state.model.ITrafficLightContext;
import co.edu.udistrital.state.model.ITrafficLightObserver;
import co.edu.udistrital.state.model.ITrafficLightState;
import co.edu.udistrital.state.model.RedState;
import co.edu.udistrital.state.model.SevenSegmentDecoder;
import co.edu.udistrital.state.model.TrafficLightColor;
import co.edu.udistrital.state.model.YellowState;
import co.edu.udistrital.state.view.IIn;
import co.edu.udistrital.state.view.IOut;

/**
 * Controlador (Cliente Orquestador / Caja Negra) que coordina el contexto del patrón State
 * ({@link ITrafficLightContext}), la suscripción de observadores ({@link ITrafficLightObserver})
 * y las abstracciones de Entrada/Salida ({@link IIn}, {@link IOut}).
 */
public class Client implements ITrafficLightObserver {

    private final ITrafficLightContext trafficLightContext;
    private final IIn in;
    private final IOut out;

    public Client(ITrafficLightContext trafficLightContext, IIn in, IOut out) {
        if (trafficLightContext == null || in == null || out == null) {
            throw new IllegalArgumentException("Las dependencias inyectadas en Client no pueden ser nulas.");
        }
        this.trafficLightContext = trafficLightContext;
        this.in = in;
        this.out = out;
        this.trafficLightContext.attachObserver(this);
    }

    /**
     * Muestra un encabezado formateado a través de la abstracción de salida.
     *
     * @param title Título del bloque de pruebas.
     */
    public void showHeader(String title) {
        out.show("\n==============================================================================================");
        out.show("  " + title);
        out.show("==============================================================================================");
    }

    /**
     * Muestra el resumen completo del estado actual del semáforo de 2 lámparas.
     */
    public void displayCurrentStatus() {
        out.show("[ESTADO ACTUAL] " + trafficLightContext.getStatusSummary());
    }

    /**
     * Avanza un segundo en el temporizador del semáforo delegando en el modelo.
     */
    public void advanceOneSecond() {
        try {
            trafficLightContext.tick();
        } catch (IllegalArgumentException e) {
            out.showErr("Fallo al avanzar el reloj del semáforo: " + e.getMessage());
        }
    }

    /**
     * Simula una secuencia continua de segundos para demostrar el conteo regresivo
     * en la lámpara de 7 segmentos y las transiciones automáticas de estado.
     *
     * @param seconds Cantidad de segundos a simular.
     */
    public void simulateSeconds(int seconds) {
        if (seconds <= 0) {
            out.showErr("La cantidad de segundos a simular debe ser mayor a cero (recibido: " + seconds + ").");
            return;
        }
        out.show("\n>>> Simulando avance de " + seconds + " segundo(s) en el reloj del semáforo...");
        for (int i = 0; i < seconds; i++) {
            trafficLightContext.tick();
        }
    }

    /**
     * Fuerza inmediatamente el paso al siguiente estado del ciclo (Rojo -> Verde -> Amarillo -> Rojo).
     */
    public void forceStateTransition() {
        try {
            String previousState = trafficLightContext.getState().getStateName();
            trafficLightContext.requestNextState();
            out.show("[TRANSICIÓN FORZADA] Cambio manual desde '" + previousState
                    + "' hacia '" + trafficLightContext.getState().getStateName() + "'.");
        } catch (IllegalArgumentException e) {
            out.showErr("Fallo al forzar transición de estado: " + e.getMessage());
        }
    }

    /**
     * Reinicia el contador regresivo del estado actual a su duración configurada.
     */
    public void resetStateTimer() {
        trafficLightContext.resetCurrentStateTimer();
        out.show("[REINICIO DE TEMPORIZADOR] Display de 7 segmentos restablecido a "
                + String.format("%02d", trafficLightContext.getRemainingSeconds()) + " segundos.");
    }

    /**
     * Cambia el estado del semáforo a un estado concreto suministrado, protegiendo invariantes.
     *
     * @param newState Nuevo estado concreto a aplicar.
     */
    public void changeState(ITrafficLightState newState) {
        try {
            trafficLightContext.setState(newState);
        } catch (IllegalArgumentException e) {
            out.showErr("Protección de invariante al cambiar estado: " + e.getMessage());
        }
    }

    /**
     * Conmuta directamente al Estado Rojo con su duración estándar.
     */
    public void changeToRedState() {
        changeState(new RedState());
    }

    /**
     * Conmuta directamente al Estado Verde con su duración estándar.
     */
    public void changeToGreenState() {
        changeState(new GreenState());
    }

    /**
     * Conmuta directamente al Estado Amarillo con su duración estándar.
     */
    public void changeToYellowState() {
        changeState(new YellowState());
    }

    /**
     * Prueba la creación de un estado personalizado validando los rangos de duración permitidos
     * para el display de 7 segmentos de 2 dígitos.
     *
     * @param colorType       Tipo de color del estado a probar.
     * @param customDuration  Duración en segundos a validar.
     */
    public void applyCustomDurationState(TrafficLightColor colorType, int customDuration) {
        try {
            if (colorType == null) {
                throw new IllegalArgumentException("El color para configurar el estado no puede ser nulo.");
            }
            ITrafficLightState customState = switch (colorType) {
                case RED -> new RedState(customDuration);
                case GREEN -> new GreenState(customDuration);
                case YELLOW -> new YellowState(customDuration);
            };
            trafficLightContext.setState(customState);
            out.show("[CONFIGURACIÓN EXITOSA] Estado " + customState.getStateName()
                    + " configurado con duración de " + customDuration + " s.");
        } catch (IllegalArgumentException e) {
            out.showErr("Protección de invariante en duración de estado: " + e.getMessage());
        }
    }

    /**
     * Verifica la codificación de un dígito para el display de 7 segmentos controlando errores de rango.
     *
     * @param digit Dígito a evaluar.
     */
    public void verifySevenSegmentEncoding(int digit) {
        try {
            boolean[] segments = SevenSegmentDecoder.encodeDigit(digit);
            StringBuilder activeLabels = new StringBuilder();
            char[] names = {'A', 'B', 'C', 'D', 'E', 'F', 'G'};
            for (int i = 0; i < segments.length; i++) {
                if (segments[i]) {
                    if (activeLabels.length() > 0) {
                        activeLabels.append(", ");
                    }
                    activeLabels.append(names[i]);
                }
            }
            out.show("[DECODIFICADOR 7 SEGMENTOS] Dígito " + digit + " -> Segmentos encendidos: [" + activeLabels + "]");
        } catch (IllegalArgumentException e) {
            out.showErr("Protección de invariante en decodificador 7 segmentos: " + e.getMessage());
        }
    }

    /**
     * Registra un observador adicional (como la ventana JavaFX) en el modelo.
     *
     * @param observer Instancia de {@link ITrafficLightObserver} a suscribir.
     */
    public void registerObserver(ITrafficLightObserver observer) {
        try {
            trafficLightContext.attachObserver(observer);
        } catch (IllegalArgumentException e) {
            out.showErr("Protección de invariante al suscribir observador: " + e.getMessage());
        }
    }

    /**
     * Desvincula un observador del modelo.
     *
     * @param observer Instancia de {@link ITrafficLightObserver} a remover.
     */
    public void unregisterObserver(ITrafficLightObserver observer) {
        try {
            trafficLightContext.detachObserver(observer);
        } catch (IllegalArgumentException e) {
            out.showErr("Protección de invariante al desvincular observador: " + e.getMessage());
        }
    }

    /**
     * Solicita al contexto notificar el estado actual a todos los observadores activos.
     */
    public void refreshObservers() {
        trafficLightContext.notifyObservers();
    }

    /**
     * Obtiene el nombre de la intersección gestionada por el semáforo.
     *
     * @return Nombre de la intersección.
     */
    public String getIntersectionName() {
        return trafficLightContext.getIntersectionName();
    }

    /**
     * Obtiene la abstracción de entrada inyectada.
     *
     * @return Instancia de {@link IIn}.
     */
    public IIn getInput() {
        return in;
    }

    @Override
    public void onTrafficLightUpdate(
            String stateName,
            TrafficLightColor activeColor,
            int remainingSeconds,
            int totalDuration,
            String actionMessage
    ) {
        out.show(String.format(
                "  -> [OBSERVER NOTIFICACIÓN] %-28s | Lámpara 1 (7-Seg): [%02d s - %s] | Lámpara 2 (Color): [%-8s] | %s",
                stateName,
                remainingSeconds,
                activeColor.getDisplayName(),
                activeColor.getDisplayName(),
                actionMessage
        ));
    }
}
