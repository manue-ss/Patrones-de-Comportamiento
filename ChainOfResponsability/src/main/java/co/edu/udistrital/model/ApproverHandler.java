// package co.edu.udistrital.model; | Archivo: ApproverHandler.java
package co.edu.udistrital.model;

/**
 * Única clase manejadora concreta del patrón Chain of Responsibility.
 * Se parametriza dinámicamente por instancia para representar los distintos cargos bancarios
 * (Ejecutivo de Cuenta, Líder Inmediato, Gerente y Director) sin requerir múltiples subclases.
 */
public class ApproverHandler implements IApproverHandler {

    private final String roleName;
    private final double minAmountExclusive;
    private final double maxAmountInclusive;
    private IApproverHandler nextHandler;

    /**
     * Construye un eslabón aprobador con sus respectivos límites de autoridad financiera.
     *
     * @param roleName           Nombre del cargo bancario en español.
     * @param minAmountExclusive Monto mínimo exclusivo que puede aprobar este cargo.
     * @param maxAmountInclusive Monto máximo inclusivo que puede aprobar este cargo.
     */
    public ApproverHandler(String roleName, double minAmountExclusive, double maxAmountInclusive) {
        this.roleName = roleName;
        this.minAmountExclusive = minAmountExclusive;
        this.maxAmountInclusive = maxAmountInclusive;
    }

    @Override
    public IApproverHandler setNextHandler(IApproverHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    @Override
    public IApprovalResult handleRequest(ICreditRequest request) {
        if (request == null) {
            return new ApprovalResult(
                false,
                "Sistema de Validación",
                "Solicitud nula o inválida: no fue posible procesar el crédito."
            );
        }

        double requestedAmount = request.getAmount();

        if (requestedAmount <= 0) {
            return new ApprovalResult(
                false,
                "Regla de Negocio Bancaria",
                String.format(
                    "Crédito RECHAZADO para %s: El monto solicitado ($%.2f) debe ser estrictamente mayor a $0.00.",
                    request.getApplicantName(),
                    requestedAmount
                )
            );
        }

        if (canApprove(requestedAmount)) {
            String formattedMessage = String.format(
                "Crédito APROBADO para %s por un monto de $%.2f. Autorizado por: %s.",
                request.getApplicantName(),
                requestedAmount,
                this.roleName
            );
            return new ApprovalResult(true, this.roleName, formattedMessage);
        }

        if (this.nextHandler != null) {
            return this.nextHandler.handleRequest(request);
        }

        return new ApprovalResult(
            false,
            "Sin Cobertura en Cadena",
            String.format(
                "Crédito NO PROCESADO para %s: Ningún nivel jerárquico pudo autorizar el monto de $%.2f.",
                request.getApplicantName(),
                requestedAmount
            )
        );
    }

    /**
     * Evalúa si el monto se encuentra dentro de las atribuciones del cargo actual.
     *
     * @param amount Monto a verificar.
     * @return true si el monto está dentro del rango permitido, false en caso contrario.
     */
    protected boolean canApprove(double amount) {
        return amount > this.minAmountExclusive && amount <= this.maxAmountInclusive;
    }

    /**
     * Obtiene el nombre del rol configurado en este manejador.
     *
     * @return Nombre del cargo bancario.
     */
    public String getRoleName() {
        return this.roleName;
    }
}