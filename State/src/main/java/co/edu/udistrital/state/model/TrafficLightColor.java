// Archivo: TrafficLightColor.java | Paquete: co.edu.udistrital.state.model
package co.edu.udistrital.state.model;

/**
 * Enumeración que define los colores posibles en las dos lámparas del semáforo moderno.
 * Cada lámpara tiene la capacidad de alternar entre los 3 colores (Rojo, Amarillo y Verde).
 */
public enum TrafficLightColor {

    RED("ROJO", "#FF1E27", "#2B080A"),
    YELLOW("AMARILLO", "#FFC700", "#2B2205"),
    GREEN("VERDE", "#00E65B", "#052612");

    private final String displayName;
    private final String activeHexColor;
    private final String dimHexColor;

    TrafficLightColor(String displayName, String activeHexColor, String dimHexColor) {
        this.displayName = displayName;
        this.activeHexColor = activeHexColor;
        this.dimHexColor = dimHexColor;
    }

    /**
     * Obtiene el nombre en español del color de la lámpara.
     *
     * @return Nombre legible del color.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Obtiene el código hexadecimal brillante para renderizar los LEDs encendidos.
     *
     * @return Cadena hexadecimal de color activo.
     */
    public String getActiveHexColor() {
        return activeHexColor;
    }

    /**
     * Obtiene el código hexadecimal atenuado para los segmentos o lámparas en reposo.
     *
     * @return Cadena hexadecimal de color atenuado.
     */
    public String getDimHexColor() {
        return dimHexColor;
    }
}
