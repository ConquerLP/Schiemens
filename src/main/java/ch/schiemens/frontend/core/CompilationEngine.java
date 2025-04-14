package ch.schiemens.frontend.core;

import ch.schiemens.frontend.core.compilationOptions.CompilationOptions;

public class CompilationEngine {

    private static final CompilationEngine INSTANCE = new CompilationEngine();
    private CompilationOptions compilationOptions;

    private CompilationEngine() {
    }

    public static CompilationEngine getInstance() {
        return INSTANCE;
    }

    public void setCompilationOptions(CompilationOptions compilationOptions) {
        this.compilationOptions = compilationOptions;
    }

    public void compile() {

        
    }

}
