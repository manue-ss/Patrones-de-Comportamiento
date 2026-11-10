// package co.edu.udistrital.model; | Archivo: IApproverHandler.java
package co.edu.udistrital.model;

/**
 * Interfaz del patrón Chain of Responsibility que define el contrato para los manejadores.
 */
public interface IApproverHandler {

    /**
     * Asigna el siguiente manejador en la cadena de responsabilidad.
     *
     * @param nextHandler Siguiente eslabón de la cadena.
     * @return Referencia al manejador asignado para facilitar el encadenamiento fluido.
     */
    IApproverHandler setNextHandler(IApproverHandler nextHandler);

    /**
     * Procesa la solicitud de crédito o la delega al siguiente manejador si supera su rango.
     *
     * @param request Solicitud de crédito a evaluar.
     * @return Resultado de la evaluación crediticia.
     */
    IApprovalResult handleRequest(ICreditRequest request);
}