package interpreter.io;

public class StandardOutput implements TextOutput {
    @Override
    public void emit(String line) {
        System.out.println(line);
    }
}
