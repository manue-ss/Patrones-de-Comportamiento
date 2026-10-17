package co.edu.udistrital.template.model;

import co.edu.udistrital.template.view.AbstractOut;
import co.edu.udistrital.template.view.AbstractIn;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Manuel Salazar
 * @version 2
 */
public abstract class OperationTemplate {

    private AbstractIn in;
    protected AbstractOut out;
    protected String[] textos;

    // Diccionario estático para la internacionalización
    private static final Map<String, String[]> DICCIONARIO = new HashMap<>();

    static {
        // Posiciones: 0 = Petición de número, 1 = Error de formato, 2 = Prefijo de resultado
        DICCIONARIO.put("es", new String[]{
            "Ingrese un valor: ",
            ">>Ingrese un numero valido.",
            "Resultado: "
        });
        DICCIONARIO.put("en", new String[]{
            "Enter a value: ",
            ">>Enter a valid number.",
            "Result: "
        });
    }

    public OperationTemplate(AbstractIn in, AbstractOut out, String idioma) {
        this.in = in;
        this.out = out;
        // Se asigna el idioma pasado por parámetro o español por defecto
        this.textos = DICCIONARIO.getOrDefault(idioma, DICCIONARIO.get("es"));
    }

    public final void execute() {
        double numA = this.getNum();
        double numB;
        do {
            numB = this.getNum();
        } while (this.validate(numB));

        double resoult = this.operar(numA, numB);

        this.showResoult(resoult);
    }

    protected abstract double operar(double numA, double numB);

    protected double getNum() {
        do {
            try {
                // Se reemplaza el string vacío por la petición del diccionario
                out.show(textos[0]);
                String dato = in.nextString();

                return Double.parseDouble(dato);
            }
            catch (NumberFormatException e) {
                // Se reemplaza el mensaje de error quemado
                out.showError(textos[1]);
            }
        } while (true);

    }

    protected void showResoult(double resoult) {
        // Se añade el texto descriptivo antes del resultado
        out.show(textos[2] + resoult);
    }

    protected boolean validate(double num) {
        return false;
    }
}
