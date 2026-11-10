// package co.edu.udistrital.controller; | Archivo: Client.java
package co.edu.udistrital.controller;

import co.edu.udistrital.model.CreditRequest;
import co.edu.udistrital.model.IApprovalResult;
import co.edu.udistrital.model.IApproverHandler;
import co.edu.udistrital.model.ICreditRequest;
import co.edu.udistrital.view.IIn;
import co.edu.udistrital.view.IOut;

/**
 * Controlador tipo "caja negra" que orquesta la comunicación entre la Vista y el Modelo.
 * Recibe sus dependencias por constructor y expone métodos delegados para ser invocados
 * desde el bucle principal en los archivos de ejecución (Main).
 */
public class Client {

    private final IApproverHandler approvalChainHead;
    private final IIn inputReader;
    private final IOut outputPrinter;

    /**
     * Construye el cliente orquestador inyectando las abstracciones del modelo y la vista.
     *
     * @param approvalChainHead Primer manejador de la cadena de responsabilidad.
     * @param inputReader       Abstracción para lectura de datos.
     * @param outputPrinter     Abstracción para impresión de mensajes.
     */
    public Client(
        IApproverHandler approvalChainHead,
        IIn inputReader,
        IOut outputPrinter
    ) {
        this.approvalChainHead = approvalChainHead;
        this.inputReader = inputReader;
        this.outputPrinter = outputPrinter;
    }

    /**
     * Método delegado para mostrar una línea de texto en la vista.
     *
     * @param msg Mensaje en español a visualizar.
     */
    public void printLine(String msg) {
        this.outputPrinter.printLine(msg);
    }

    /**
     * Método delegado para mostrar un aviso de captura de datos en la vista.
     *
     * @param msg Aviso en español a visualizar.
     */
    public void print(String msg) {
        this.outputPrinter.print(msg);
    }

    /**
     * Método delegado para leer una cadena de entrada desde la vista.
     *
     * @return Cadena capturada.
     */
    public String readLine() {
        return this.inputReader.readLine();
    }

    /**
     * Método delegado para verificar disponibilidad de datos en la entrada.
     *
     * @return true si existen más líneas por leer, false en caso contrario.
     */
    public boolean hasNextLine() {
        return this.inputReader.hasNextLine();
    }

    /**
     * Método delegado para cerrar los recursos de entrada de la vista.
     */
    public void closeInput() {
        this.inputReader.close();
    }

    /**
     * Valida que el nombre del solicitante no sea nulo ni esté en blanco.
     *
     * @param rawName Nombre capturado desde la entrada.
     * @return Nombre limpio de espacios extremos.
     * @throws IllegalArgumentException Si el nombre está vacío o es nulo.
     */
    public String validateApplicantName(String rawName) {
        if (rawName == null || rawName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del solicitante no puede estar vacío.");
        }
        return rawName.trim();
    }

    /**
     * Valida el formato numérico de la cadena ingresada y la convierte a tipo double.
     *
     * @param rawAmount Cadena con el monto ingresado.
     * @return Valor numérico convertido.
     * @throws NumberFormatException    Si el texto no representa un número real válido.
     * @throws IllegalArgumentException Si el valor excede los límites numéricos permitidos.
     */
    public double parseAndValidateAmount(String rawAmount) {
        if (rawAmount == null || rawAmount.trim().isEmpty()) {
            throw new NumberFormatException("Monto vacío.");
        }
        String normalizedAmount = rawAmount.trim().replace(",", ".");
        double value = Double.parseDouble(normalizedAmount);
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new IllegalArgumentException("El monto especificado excede los límites numéricos permitidos.");
        }
        return value;
    }

    /**
     * Método delegado para construir una solicitud del modelo.
     *
     * @param applicantName Nombre del solicitante.
     * @param amount        Monto solicitado.
     * @return Instancia de la solicitud bajo su interfaz.
     */
    public ICreditRequest createRequest(String applicantName, double amount) {
        return new CreditRequest(applicantName, amount);
    }

    /**
     * Método delegado para enviar una solicitud a través de la cadena de responsabilidad.
     *
     * @param request Solicitud de crédito a evaluar.
     * @return Resultado devuelto por el modelo.
     */
    public IApprovalResult processCredit(ICreditRequest request) {
        return this.approvalChainHead.handleRequest(request);
    }
}