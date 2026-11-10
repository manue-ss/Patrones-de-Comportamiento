// package co.edu.udistrital.chainofresponsability; | Archivo: TestMainInvalidAndEdgeCases.java
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
 * Prueba atómica 3: Evalúa casos borde, entradas malformadas, montos negativos o cero,
 * y nombres vacíos ejecutando el bucle principal y el control de excepciones en el main
 * mediante llamados a los métodos delegados del Cliente.
 */
public class TestMainInvalidAndEdgeCases {

    /**
     * Punto de entrada para la prueba atómica de excepciones y casos extremos.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        // 1. Configuración de la cadena utilizando la única clase Handler
        IApproverHandler accountExecutive = new ApproverHandler("Ejecutivo de Cuenta", 0.0, 10000.0);
        IApproverHandler immediateLeader = new ApproverHandler("Líder Inmediato", 10000.0, 50000.0);
        IApproverHandler manager = new ApproverHandler("Gerente", 50000.0, 100000.0);
        IApproverHandler director = new ApproverHandler("Director", 100000.0, Double.POSITIVE_INFINITY);

        accountExecutive
            .setNextHandler(immediateLeader)
            .setNextHandler(manager)
            .setNextHandler(director);

        // 2. Entradas con errores de formato, valores negativos/cero y nombres en blanco
        String edgeCaseInputs = String.join("\n",
            "",                             // Error: Nombre vacío
            "Pedro Pérez", "abc_no_numero", // Error: Formato numérico inválido
            "María López", "0",             // Caso borde: Monto igual a $0 (Rechazado por regla)
            "Jorge Díaz", "-15000",         // Caso borde: Monto negativo (Rechazado por regla)
            "Sofía Castro", "12000",        // Recuperación exitosa tras errores -> Líder Inmediato
            "salir"
        );

        // 3. Inyección de dependencias en el Cliente
        IIn inputReader = new ConsoleIn(edgeCaseInputs);
        IOut outputPrinter = new ConsoleOut();
        Client bankClient = new Client(accountExecutive, inputReader, outputPrinter);

        // 4. Bucle principal controlado en el main mediante llamados a métodos del Cliente
        bankClient.printLine("==================================================================");
        bankClient.printLine("   SISTEMA BANCARIO DE APROBACIÓN DE CRÉDITOS (CHAIN OF RESP.)    ");
        bankClient.printLine("==================================================================");

        boolean running = true;

        while (running && bankClient.hasNextLine()) {
            try {
                bankClient.printLine("\n------------------------------------------------------------------");
                bankClient.printLine("Ingrese el nombre del solicitante (o escriba 'salir' para terminar): ");
                String applicantInput = bankClient.readLine().trim();

                if (applicantInput.equalsIgnoreCase("salir")) {
                    bankClient.printLine("Finalizando sesión en el sistema bancario...");
                    running = false;
                    continue;
                }

                String validName = bankClient.validateApplicantName(applicantInput);

                bankClient.printLine("Ingrese el monto del crédito en dólares (ej. 25000): $");
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