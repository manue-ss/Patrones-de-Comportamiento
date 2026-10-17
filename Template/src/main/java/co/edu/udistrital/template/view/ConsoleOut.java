package co.edu.udistrital.template.view;

/**
 *
 * @author Manuel Salazar
 */
public class ConsoleOut extends AbstractOut {

    @Override
    public void show(String st) {
        System.out.println(st);
    }

    @Override
    public void showError(String st) {
        System.err.println(st);
    }

}
