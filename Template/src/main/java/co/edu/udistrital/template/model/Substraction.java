package co.edu.udistrital.template.model;

import co.edu.udistrital.template.view.AbstractOut;
import co.edu.udistrital.template.view.AbstractIn;

/**
 *
 * @author Manuel Salazar
 * @version 1
 */
public class Substraction extends OperationTemplate {

    public Substraction(AbstractIn in, AbstractOut out, String idioma) {
        super(in, out, idioma);
    }


    @Override
    protected double operar(double numA, double numB) {
        return numA - numB;
    }

}
