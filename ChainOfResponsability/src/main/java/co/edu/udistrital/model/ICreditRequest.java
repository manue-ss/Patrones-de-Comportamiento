// package co.edu.udistrital.model; | Archivo: ICreditRequest.java
package co.edu.udistrital.model;

/**
 * Abstracción que representa una solicitud de crédito bancario.
 */
public interface ICreditRequest {

    /**
     * Obtiene el nombre del solicitante del crédito.
     *
     * @return Nombre del cliente solicitante.
     */
    String getApplicantName();

    /**
     * Obtiene el monto monetario solicitado para el préstamo.
     *
     * @return Monto del crédito en dólares.
     */
    double getAmount();
}