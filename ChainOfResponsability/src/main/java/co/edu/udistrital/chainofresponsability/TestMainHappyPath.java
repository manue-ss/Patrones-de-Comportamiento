// package co.edu.udistrital.chainofresponsability; | Archivo: TestMainHappyPath.java
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
 * Prueba atómica 1: Evalúa el camino feliz (Happy Path) a través de los cuatro niveles
 * jerárquicos del banco ejecutando el bucle principal directamente en el main mediante
 * llamados a los métodos delegados del Cliente.
 */
public class TestMainHappyPath {

    /**
     * Punto de entrada para la prueba atómica de flujos estándar de aprobación.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        // 1. Instanciación de la única clase Handler parametrizada para cada rol de negocio
        IApproverHandler accountExecutive = new ApproverHandler("Ejecutivo de Cuenta", 0.0, 10000.0);
        IApproverHandler immediateLeader = new ApproverHandler("Líder Inmediato", 10000.0, 50000.0);
        IApproverHandler manager = new ApproverHandler("Gerente", 50000.0, 100000.0);
        IApproverHandler director = new ApproverHandler("Director", 100000.0, Double.POSITIVE_INFINITY);

        // 2. Ensamblaje de la cadena de responsabilidad
        accountExecutive
            .setNextHandler(immediateLeader)
            .setNextHandler(manager)
            .setNextHandler(director);

        // 3. Simulación de entradas válidas para cada uno de los 4 niveles jerárquicos
        String simulatedInputs = String.join("\n",
            "Carlos Ramírez", "5000",       // Nivel 1: Ejecutivo de Cuenta (<= $10,000)
            "Ana Gómez", "25000",           // Nivel 2: Líder Inmediato ($10,000 - $50,000)
            "Luis Martínez", "75000",       // Nivel 3: Gerente ($50,000 - $100,000)
            "Corporación Alfa", "250000",   // Nivel 4: Director (> $100,000)
            "salir"
        );

        // 4. Instanciación de abstracciones de Vista e Inyección de Dependencias en el Cliente
        IIn inputReader = new ConsoleIn(simulatedInputs);
        IOut outputPrinter = new ConsoleOut();
        Client bankClient = new Client(accountExecutive, inputReader, outputPrinter);

        // 5. Bucle principal controlado en el main mediante llamados a métodos del Cliente
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