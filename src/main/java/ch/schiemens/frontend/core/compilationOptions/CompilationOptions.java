package ch.schiemens.frontend.core.compilationOptions;

public class CompilationOptions {

    private static final CompilationOptions INSTANCE = new CompilationOptions();



    private CompilationOptions() {
    }

    public static CompilationOptions getInstance() {
        return INSTANCE;
    }

    public void printHelp() {

    }

}
