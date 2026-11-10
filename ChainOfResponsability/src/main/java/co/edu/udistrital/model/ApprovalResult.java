// package co.edu.udistrital.model; | Archivo: ApprovalResult.java
package co.edu.udistrital.model;

/**
 * Entidad inmutable que contiene el dictamen de evaluación de la cadena de responsabilidad.
 */
public class ApprovalResult implements IApprovalResult {

    private final boolean approved;
    private final String approverRole;
    private final String summaryMessage;

    /**
     * Construye el resultado de la operación de evaluación crediticia.
     *
     * @param approved       Estado de aprobación.
     * @param approverRole   Cargo encargado de la decisión.
     * @param summaryMessage Descripción detallada del dictamen.
     */
    public ApprovalResult(boolean approved, String approverRole, String summaryMessage) {
        this.approved = approved;
        this.approverRole = approverRole;
        this.summaryMessage = summaryMessage;
    }

    @Override
    public boolean isApproved() {
        return this.approved;
    }

    @Override
    public String getApproverRole() {
        return this.approverRole;
    }

    @Override
    public String getSummaryMessage() {
        return this.summaryMessage;
    }
}