package co.edu.udistrital.template.model;

import co.edu.udistrital.template.view.AbstractOut;
import co.edu.udistrital.template.view.AbstractIn;

/**
 *
 * @author Manuel Salazar
 * @version 1
 */
public class Division extends OperationTemplate {
    
    public Division(AbstractIn in, AbstractOut out, String idioma) {
        super(in, out, idioma);
    }
    
    @Override
    protected boolean validate(double num) {
        boolean val = num == 0;
        if (val) {
            super.out.showError(super.textos[1]);
        }
        
        return val;
    }
    
    @Override
    protected double operar(double numA, double numB) {
        return numA / numB;
    }
    
}
