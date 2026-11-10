// Archivo: Client.java | Paquete: co.edu.udistrital.strategy.controller
package co.edu.udistrital.strategy.controller;

import co.edu.udistrital.strategy.model.Algorithm;
import co.edu.udistrital.strategy.model.ISortContext;
import co.edu.udistrital.strategy.view.IIn;
import co.edu.udistrital.strategy.view.IOut;
import java.util.Arrays;

public class Client {

    private final ISortContext context;
    private final IIn in;
    private final IOut out;

    public Client(ISortContext context, IIn in, IOut out) {
        this.context = context;
        this.in = in;
        this.out = out;
    }

    public boolean executeSort(Algorithm algorithm, Object[] data) {
        try {
            String strategyName = (algorithm != null)
                    ? algorithm.getClass().getSimpleName() : "EstrategiaNula";
            
            this.out.printLn("\n[" + strategyName + "] Arreglo original:  "
                    + Arrays.toString(data));

            this.context.setSortAlgorithm(algorithm);
            boolean success = this.context.sort(data);

            if (success) {
                this.out.printLn("[" + strategyName + "] Ordenamiento exitoso: "
                        + Arrays.toString(data));
                return true;
            } else {
                this.out.printError("Fallo controlado al ordenar el arreglo "
                        + "(datos nulos, vacios, estrategia nula o tipos no "
                        + "comparables).");
                return false;
            }
        }
        catch (Exception e) {
            this.out.printError("Excepcion inesperada en la ejecucion: "
                    + e.getMessage());
            return false;
        }
    }

    public boolean executeFromRawInput(Algorithm algorithm, String rawInput) {
        try {
            this.out.printLn("\nEntrada cruda recibida: \"" + rawInput + "\"");
            
            if (rawInput == null || rawInput.trim().isEmpty()) {
                this.out.printError("La cadena de entrada esta vacia o es nula.");
                return false;
            }

            String[] tokens = rawInput.split(",");
            Integer[] parsedArray = new Integer[tokens.length];

            for (int i = 0; i < tokens.length; i++) {
                parsedArray[i] = Integer.parseInt(tokens[i].trim());
            }

            return this.executeSort(algorithm, parsedArray);
        }
        catch (NumberFormatException e) {
            this.out.printError("Formato numerico invalido en la entrada '"
                    + rawInput + "'. Detalle: " + e.getMessage());
            return false;
        }
        catch (Exception e) {
            this.out.printError("Error al procesar la entrada: "
                    + e.getMessage());
            return false;
        }
    }

    public void runInteractiveLoop(Algorithm[] strategies) {
        if (strategies == null || strategies.length == 0) {
            this.out.printError("No hay estrategias configuradas para el modo"
                    + " interactivo.");
            return;
        }

        boolean running = true;
        while (running) {
            try {
                this.out.printLn("\n--- MENU DE ORDENAMIENTO (STRATEGY) ---");
                
                for (int i = 0; i < strategies.length; i++) {
                    this.out.printLn((i + 1) + ". "
                            + strategies[i].getClass().getSimpleName());
                }
                
                this.out.printLn("0. Salir");
                this.out.printLn("Seleccione una opcion:");

                String optionStr = this.in.readLine();
                int option = Integer.parseInt(optionStr.trim());

                if (option == 0) {
                    running = false;
                    this.out.printLn("Finalizando sesion interactiva.");
                } else if (option > 0 && option <= strategies.length) {
                    this.out.printLn("Ingrese los numeros enteros separados "
                            + "por coma (ej. 45, 12, 89, 1):");
                    String rawNumbers = this.in.readLine();
                    this.executeFromRawInput(strategies[option - 1], rawNumbers);
                } else {
                    this.out.printError("Opcion fuera de rango.");
                }
            }
            catch (NumberFormatException e) {
                this.out.printError("Debe ingresar un numero entero valido "
                        + "para seleccionar la opcion.");
            }
            catch (Exception e) {
                this.out.printError("Error en el ciclo de aplicacion: "
                        + e.getMessage());
                running = false;
            }
        }
    }
}
