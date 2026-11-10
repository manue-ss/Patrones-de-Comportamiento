package co.edu.udistrital.strategy.view;

public class ConsoleOut implements IOut {

    @Override
    public void printLn(String msg) {
        System.out.println(msg);
    }

    @Override
    public void printError(String msg) {
        System.err.println("[ERROR] " + msg);
    }
}