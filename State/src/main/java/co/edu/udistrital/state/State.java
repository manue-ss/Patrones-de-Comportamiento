// Archivo: State.java | Paquete: co.edu.udistrital.state
package co.edu.udistrital.state;

import co.edu.udistrital.state.controller.Client;
import co.edu.udistrital.state.model.GreenState;
import co.edu.udistrital.state.model.ITrafficLightContext;
import co.edu.udistrital.state.model.ITrafficLightState;
import co.edu.udistrital.state.model.ModernTrafficLight;
import co.edu.udistrital.state.model.RedState;
import co.edu.udistrital.state.model.TrafficLightColor;
import co.edu.udistrital.state.model.YellowState;
import co.edu.udistrital.state.view.ConsoleIn;
import co.edu.udistrital.state.view.ConsoleOut;
import co.edu.udistrital.state.view.IIn;
import co.edu.udistrital.state.view.IOut;
import co.edu.udistrital.state.view.TrafficLightGuiApp;

/**
 * Composition Root que ensambla las abstracciones del Modelo, la Vista y el Controlador
 * bajo el principio de Inversión de Dependencias (DIP).
 * Ejecuta la progresión de pruebas deterministas y de validación de los patrones State y Observer,
 * y lanza la interfaz gráfica JavaFX del semáforo moderno de 2 lámparas.
 */
public class State {

    public static void main(String[] args) {
        // 1. Instanciación de las abstracciones de Entrada/Salida (Vista)
        IIn in = new ConsoleIn();
        IOut out = new ConsoleOut();

        // 2. Instanciación del Estado Inicial y del Contexto (Modelo - Patrón State + Observer)
        ITrafficLightState initialState = new RedState(3);
        ITrafficLightContext trafficLightContext = new ModernTrafficLight(
                "Av. Caracas con Calle 40 - Universidad Distrital",
                initialState
        );

        // 3. Ensamblaje del Cliente Orquestador (Controlador)
        Client client = new Client(trafficLightContext, in, out);

        // =========================================================================================
        // FASE 1: PRUEBAS ALFA / DETERMINISTAS (HAPPY PATHS - CICLO STATE Y NOTIFICACIÓN OBSERVER)
        // =========================================================================================
        client.showHeader("FASE 1: PRUEBAS ALFA - PATRÓN STATE + OBSERVER EN SEMÁFORO DE 2 LÁMPARAS");
        client.displayCurrentStatus();

        // Verificación del decodificador de 7 segmentos para la Lámpara 1
        client.verifySevenSegmentEncoding(3);
        client.verifySevenSegmentEncoding(8);
        client.verifySevenSegmentEncoding(0);

        // Simulación determinista: el estado Rojo inicial dura 3 segundos; al 3er tick transiciona a Verde
        client.simulateSeconds(3);
        client.displayCurrentStatus();

        // Configuramos un estado Verde corto (3 segundos) para demostrar la transición automática Verde -> Amarillo
        client.applyCustomDurationState(TrafficLightColor.GREEN, 3);
        client.simulateSeconds(3);
        client.displayCurrentStatus();

        // Configuramos un estado Amarillo corto (2 segundos) para demostrar la transición Amarillo -> Rojo
        client.applyCustomDurationState(TrafficLightColor.YELLOW, 2);
        client.simulateSeconds(2);
        client.displayCurrentStatus();

        // Demostración de transiciones manuales polimórficas (nextState)
        client.showHeader("DEMOSTRACIÓN DE CONMUTACIÓN POLIMÓRFICA INMEDIATA (nextState)");
        client.forceStateTransition(); // Rojo -> Verde
        client.forceStateTransition(); // Verde -> Amarillo
        client.forceStateTransition(); // Amarillo -> Rojo

        // =========================================================================================
        // FASE 2: PRUEBAS DE VALIDACIÓN, PROTECCIÓN DE INVARIANTES Y CASOS LÍMITE
        // =========================================================================================
        client.showHeader("FASE 2: PRUEBAS DE VALIDACIÓN Y PROTECCIÓN DE INVARIANTES DE DOMINIO");

        // 2.1 Intento de inyectar un estado nulo en el contexto
        client.changeState(null);

        // 2.2 Intento de crear un estado con duración 0 o negativa (inválida para el temporizador)
        client.applyCustomDurationState(TrafficLightColor.RED, 0);
        client.applyCustomDurationState(TrafficLightColor.GREEN, -5);

        // 2.3 Intento de crear un estado con duración mayor a 99 segundos (excede los 2 dígitos de 7 segmentos)
        client.applyCustomDurationState(TrafficLightColor.YELLOW, 120);

        // 2.4 Intento de decodificar dígitos fuera del rango [0-9] en el display de 7 segmentos
        client.verifySevenSegmentEncoding(-1);
        client.verifySevenSegmentEncoding(10);

        // 2.5 Intento de suscribir y desvincular un observador nulo
        client.registerObserver(null);
        client.unregisterObserver(null);

        // 2.6 Intento de simular una cantidad de segundos no positiva
        client.simulateSeconds(0);

        // 2.7 Validación de invariantes en constructores de Estados y Contexto
        try {
            new ModernTrafficLight("   ", new RedState());
        } catch (IllegalArgumentException e) {
            out.showErr("Protección de invariante en constructor de Contexto: " + e.getMessage());
        }

        try {
            new RedState().nextState(null);
        } catch (IllegalArgumentException e) {
            out.showErr("Protección de invariante en transición con Contexto nulo: " + e.getMessage());
        }

        try {
            new GreenState();
            new YellowState();
        } catch (IllegalArgumentException e) {
            out.showErr("Error inesperado: " + e.getMessage());
        }

        // Restablecemos el Estado Rojo estándar (10 s) antes de abrir la interfaz gráfica JavaFX
        client.showHeader("LANZANDO INTERFAZ GRÁFICA JAVAFX (SEMÁFORO MODERNO DE 2 LÁMPARAS)");
        client.changeToRedState();
        client.displayCurrentStatus();

        // 4. Inicio de la GUI en JavaFX (suscrita en paralelo mediante el patrón Observer)
        if (args != null && args.length > 0 && "--headless".equalsIgnoreCase(args[0])) {
            out.show("[MODO HEADLESS] Ejecución por consola finalizada sin abrir ventana JavaFX.");
        } else {
            TrafficLightGuiApp.launchGui(client, args);
        }
    }
}
