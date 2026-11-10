// package co.edu.udistrital.model; | Archivo: IApprovalResult.java
package co.edu.udistrital.model;

/**
 * Abstracción que representa el resultado del procesamiento de un crédito en la cadena.
 */
public interface IApprovalResult {

    /**
     * Indica si la solicitud de crédito fue aprobada.
     *
     * @return true si fue aprobada, false si fue rechazada.
     */
    boolean isApproved();

    /**
     * Obtiene el cargo o rol responsable que tomó la decisión.
     *
     * @return Nombre del rol aprobador.
     */
    String getApproverRole();

    /**
     * Obtiene el detalle descriptivo de la resolución del crédito.
     *
     * @return Mensaje con el dictamen final.
     */
    String getSummaryMessage();
}