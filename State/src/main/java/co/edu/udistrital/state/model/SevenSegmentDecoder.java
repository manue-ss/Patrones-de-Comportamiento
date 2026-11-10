// Archivo: SevenSegmentDecoder.java | Paquete: co.edu.udistrital.state.model
package co.edu.udistrital.state.model;

/**
 * Servicio de dominio que codifica números enteros (0-9) en señales lógicas para un display
 * electrónico de 7 segmentos (segmentos A, B, C, D, E, F, G).
 * Permite tanto a la interfaz gráfica JavaFX como al reporte textual conocer qué segmentos encender.
 */
public final class SevenSegmentDecoder {

    /**
     * Matriz de activación de los 7 segmentos [a, b, c, d, e, f, g] para cada dígito del 0 al 9.
     * Orden estándar:
     *   --A--
     *  |     |
     *  F     B
     *  |     |
     *   --G--
     *  |     |
     *  E     C
     *  |     |
     *   --D--
     */
    private static final boolean[][] DIGIT_SEGMENTS = {
        // A,     B,     C,     D,     E,     F,     G
        {true,  true,  true,  true,  true,  true,  false}, // 0
        {false, true,  true,  false, false, false, false}, // 1
        {true,  true,  false, true,  true,  false, true},  // 2
        {true,  true,  true,  true,  false, false, true},  // 3
        {false, true,  true,  false, false, true,  true},  // 4
        {true,  false, true,  true,  false, true,  true},  // 5
        {true,  false, true,  true,  true,  true,  true},  // 6
        {true,  true,  true,  false, false, false, false}, // 7
        {true,  true,  true,  true,  true,  true,  true},  // 8
        {true,  true,  true,  true,  false, true,  true}   // 9
    };

    private SevenSegmentDecoder() {
        // Clase utilitaria de dominio sin estado
    }

    /**
     * Retorna un arreglo inmutable de 7 valores booleanos indicando cuáles segmentos (A..G)
     * deben iluminarse para representar el dígito indicado.
     *
     * @param digit Dígito entre 0 y 9.
     * @return Arreglo de 7 posiciones booleanas [A, B, C, D, E, F, G].
     */
    public static boolean[] encodeDigit(int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException(
                    "El dígito para el decodificador de 7 segmentos debe estar entre 0 y 9 (recibido: " + digit + ")."
            );
        }
        return DIGIT_SEGMENTS[digit].clone();
    }

    /**
     * Separa un número de dos dígitos (0 a 99) en su dígito de decenas y su dígito de unidades.
     *
     * @param number Número entre 0 y 99.
     * @return Arreglo de 2 enteros: [decenas, unidades].
     */
    public static int[] splitTwoDigits(int number) {
        if (number < 0 || number > 99) {
            throw new IllegalArgumentException(
                    "El valor para el display de 2 dígitos debe estar entre 0 y 99 (recibido: " + number + ")."
            );
        }
        return new int[]{number / 10, number % 10};
    }
}
