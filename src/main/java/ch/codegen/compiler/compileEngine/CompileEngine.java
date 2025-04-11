package ch.codegen.compiler.compileEngine;

import ch.codegen.cli.CompileOptions;
import ch.codegen.util.LoggerCli;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CompileEngine {

    private final CompileOptions options = CompileOptions.getInstance();
    private final Preprocessor preprocessor = new Preprocessor();
    private final Compiler compiler = new Compiler();

    public void run() {
        List<File> sources = options.getInputFiles();

        if (sources.isEmpty()) {
            LoggerCli.error("No input files provided.");
            return;
        }

        if (options.isCompileSeparately()) {
            compileEachIndividually(sources);
        } else {
            compileAsSingleUnit(sources);
        }
    }

    private void compileAsSingleUnit(List<File> sources) {
        File entry = options.getEntryFile() != null ? options.getEntryFile() : sources.get(0);
        LoggerCli.info("Compiling as single unit, entry file: " + entry.getName());

        try {
            String code = preprocessor.process(entry);
            File out = options.getOutputFile() != null ? options.getOutputFile() : new File(entry.getName() + ".out");
            compiler.compile(code, out);
        } catch (IOException e) {
            LoggerCli.error("Failed to compile entry file: " + e.getMessage());
        }
    }

    private void compileEachIndividually(List<File> sources) {
        LoggerCli.info("Compiling each file separately...");

        for (File file : sources) {
            try {
                String code = preprocessor.process(file);
                File out = options.getOutputFile() != null
                        ? new File(options.getOutputFile(), file.getName() + ".out")
                        : new File(file.getName() + ".out");

                compiler.compile(code, out);
            } catch (IOException e) {
                LoggerCli.warn("Failed to compile " + file.getName() + ": " + e.getMessage());
            }
        }
    }

}

