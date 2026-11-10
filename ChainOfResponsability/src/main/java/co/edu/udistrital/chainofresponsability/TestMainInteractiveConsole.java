// package co.edu.udistrital.chainofresponsability; | Archivo: TestMainInteractiveConsole.java
package co.edu.udistrital.chainofresponsability;

import co.edu.udistrital.controller.Client;
import co.edu.udistrital.model.ApproverHandler;
import co.edu.udistrital.model.IApprovalResult;
import co.edu.udistrital.model.IApproverHandler;
import co.edu.udistrital.model.ICreditRequest;
import co.edu.udistrital.view.ConsoleIn;
import co.edu.udistrital.view.ConsoleOut;
import co.edu.udistrital.view.IIn;
import co.edu.udistrital.view.IOut;

/**
 * Prueba atómica 4: Ensamblaje para ejecución interactiva en vivo desde el teclado (System.in)
 * con el bucle principal ejecutado directamente en el main mediante métodos del Cliente.
 */
public class TestMainInteractiveConsole {

    /**
     * Punto de entrada para la ejecución interactiva con el usuario por consola.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        // 1. Instanciación de los eslabones usando la única clase Handler
        IApproverHandler accountExecutive = new ApproverHandler("Ejecutivo de Cuenta", 0.0, 10000.0);
        IApproverHandler immediateLeader = new ApproverHandler("Líder Inmediato", 10000.0, 50000.0);
        IApproverHandler manager = new ApproverHandler("Gerente", 50000.0, 100000.0);
        IApproverHandler director = new ApproverHandler("Director", 100000.0, Double.POSITIVE_INFINITY);

        // 2. Construcción de la cadena de responsabilidad
        accountExecutive
            .setNextHandler(immediateLeader)
            .setNextHandler(manager)
            .setNextHandler(director);

        // 3. Creación de componentes de E/S enlazados a la consola real e inyección en el Cliente
        IIn liveInputReader = new ConsoleIn();
        IOut liveOutputPrinter = new ConsoleOut();
        Client bankClient = new Client(accountExecutive, liveInputReader, liveOutputPrinter);

        // 4. Bucle interactivo controlado en el main mediante llamados a métodos del Cliente
        bankClient.printLine("==================================================================");
        bankClient.printLine("   SISTEMA BANCARIO DE APROBACIÓN DE CRÉDITOS (CHAIN OF RESP.)    ");
        bankClient.printLine("==================================================================");

        boolean running = true;

        while (running && bankClient.hasNextLine()) {
            try {
                bankClient.printLine("\n------------------------------------------------------------------");
                bankClient.print("Ingrese el nombre del solicitante (o escriba 'salir' para terminar): ");
                String applicantInput = bankClient.readLine().trim();

                if (applicantInput.equalsIgnoreCase("salir")) {
                    bankClient.printLine("Finalizando sesión en el sistema bancario...");
                    running = false;
                    continue;
                }

                String validName = bankClient.validateApplicantName(applicantInput);

                bankClient.print("Ingrese el monto del crédito en dólares (ej. 25000): $");
                String amountInput = bankClient.readLine();
                double parsedAmount = bankClient.parseAndValidateAmount(amountInput);

                ICreditRequest creditRequest = bankClient.createRequest(validName, parsedAmount);
                IApprovalResult result = bankClient.processCredit(creditRequest);

                bankClient.printLine(">> RESULTADO: " + result.getSummaryMessage());
            } catch (NumberFormatException exception) {
                bankClient.printLine(">> ERROR DE FORMATO: El valor ingresado no es un número válido. Intente nuevamente.");
            } catch (IllegalArgumentException exception) {
                bankClient.printLine(">> ERROR DE VALIDACIÓN: " + exception.getMessage());
            } catch (Exception exception) {
                bankClient.printLine(">> ERROR INESPERADO: Ocurrió un fallo al procesar la solicitud: " + exception.getMessage());
            }
        }

        bankClient.printLine("==================================================================");
        bankClient.printLine("                  EJECUCIÓN FINALIZADA CORRECTAMENTE              ");
        bankClient.printLine("==================================================================");
        bankClient.closeInput();
    }
}