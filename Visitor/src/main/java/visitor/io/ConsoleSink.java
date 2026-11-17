package visitor.io;

public class ConsoleSink implements OutputSink {
    @Override
    public void writeLine(String text) {
        System.out.println(text);
    }
}
