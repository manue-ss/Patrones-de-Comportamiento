// Archivo: ModernTrafficLight.java | Paquete: co.edu.udistrital.state.model
package co.edu.udistrital.state.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Contexto del patrón State y Sujeto (Subject) del patrón Observer paralelo.
 * Representa un semáforo moderno de dos lámparas multicolor:
 * - Lámpara 1 (Superior): Muestra el tiempo restante en formato display de 7 segmentos en el color actual.
 * - Lámpara 2 (Inferior): Muestra la señal luminosa plena en el color actual (Rojo, Amarillo o Verde).
 */
public class ModernTrafficLight implements ITrafficLightContext {

    private final String intersectionName;
    private final List<ITrafficLightObserver> observers;
    private ITrafficLightState currentState;
    private int remainingSeconds;

    public ModernTrafficLight(String intersectionName, ITrafficLightState initialState) {
        if (intersectionName == null || intersectionName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la intersección del semáforo no puede ser nulo ni vacío.");
        }
        if (initialState == null) {
            throw new IllegalArgumentException("El estado inicial del semáforo no puede ser nulo.");
        }
        this.intersectionName = intersectionName.trim();
        this.observers = new ArrayList<>();
        this.currentState = initialState;
        this.remainingSeconds = initialState.getDurationSeconds();
    }

    @Override
    public void setState(ITrafficLightState newState) {
        if (newState == null) {
            throw new IllegalArgumentException("No se puede asignar un estado nulo al semáforo.");
        }
        this.currentState = newState;
        this.remainingSeconds = newState.getDurationSeconds();
        notifyObservers();
    }

    @Override
    public ITrafficLightState getState() {
        return currentState;
    }

    @Override
    public void tick() {
        if (remainingSeconds > 1) {
            remainingSeconds--;
            notifyObservers();
        } else {
            currentState.nextState(this);
        }
    }

    @Override
    public void requestNextState() {
        currentState.nextState(this);
    }

    @Override
    public void resetCurrentStateTimer() {
        this.remainingSeconds = currentState.getDurationSeconds();
        notifyObservers();
    }

    @Override
    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    @Override
    public String getIntersectionName() {
        return intersectionName;
    }

    @Override
    public void attachObserver(ITrafficLightObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("El observador a suscribir no puede ser nulo.");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void detachObserver(ITrafficLightObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("El observador a desvincular no puede ser nulo.");
        }
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ITrafficLightObserver observer : new ArrayList<>(observers)) {
            observer.onTrafficLightUpdate(
                    currentState.getStateName(),
                    currentState.getColor(),
                    remainingSeconds,
                    currentState.getDurationSeconds(),
                    currentState.getActionMessage()
            );
        }
    }

    @Override
    public String getStatusSummary() {
        int[] digits = SevenSegmentDecoder.splitTwoDigits(remainingSeconds);
        String formattedCountdown = String.format("%02d", remainingSeconds);
        TrafficLightColor activeColor = currentState.getColor();
        return String.format(
                "Intersección: %s | Estado: %s | [Lámpara 1 - Display 7 Segmentos: '%s' (Dígitos %d y %d) en color %s] | [Lámpara 2 - Luz Principal: %s (%s)] | Directriz: %s",
                intersectionName,
                currentState.getStateName(),
                formattedCountdown,
                digits[0],
                digits[1],
                activeColor.getDisplayName(),
                activeColor.getDisplayName(),
                activeColor.getActiveHexColor(),
                currentState.getActionMessage()
        );
    }
}
