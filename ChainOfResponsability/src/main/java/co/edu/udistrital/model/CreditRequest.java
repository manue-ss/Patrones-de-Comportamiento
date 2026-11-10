// package co.edu.udistrital.model; | Archivo: CreditRequest.java
package co.edu.udistrital.model;

/**
 * Entidad inmutable que encapsula los datos de una solicitud de crédito.
 */
public class CreditRequest implements ICreditRequest {

    private final String applicantName;
    private final double amount;

    /**
     * Construye una nueva solicitud de crédito.
     *
     * @param applicantName Nombre del titular que solicita el préstamo.
     * @param amount        Monto solicitado en dólares.
     */
    public CreditRequest(String applicantName, double amount) {
        this.applicantName = applicantName;
        this.amount = amount;
    }

    @Override
    public String getApplicantName() {
        return this.applicantName;
    }

    @Override
    public double getAmount() {
        return this.amount;
    }
}