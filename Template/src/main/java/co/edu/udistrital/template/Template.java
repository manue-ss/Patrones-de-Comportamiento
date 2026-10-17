package co.edu.udistrital.template;

import co.edu.udistrital.template.controller.Client;
import co.edu.udistrital.template.model.*;
import co.edu.udistrital.template.view.AbstractIn;
import co.edu.udistrital.template.view.AbstractOut;
import co.edu.udistrital.template.view.ConsoleIn;
import co.edu.udistrital.template.view.ConsoleOut;

/**
 *
 * @author 11man
 */
public class Template {

    public static void main(String[] args) {
        // Verifica si se pasó un argumento al ejecutar el programa; de lo contrario, usa "es" por defecto.
        String idioma = (args.length > 0) ? args[0] : "es";

        AbstractOut out = new ConsoleOut();
        AbstractIn in = new ConsoleIn();

        Client client = new Client();

        // Se pasa el parámetro 'idioma' a los nuevos constructores de cada operación
        OperationTemplate op = new Addition(in, out, idioma);
        client.execute(op);

        op = new Substraction(in, out, idioma);
        client.execute(op);

        op = new Multiplication(in, out, idioma);
        client.execute(op);

        op = new Division(in, out, idioma);
        client.execute(op);
    }
}
