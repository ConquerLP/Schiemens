package ch.schiemens.frontend.core.compilationOptions;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CompilationOptions {

    // === Singleton ===
    private static final CompilationOptions INSTANCE = new CompilationOptions();

    public static CompilationOptions getInstance() {
        return INSTANCE;
    }

    // === Fields ===

    // Input / Output
    private final List<Path> inputFiles = new ArrayList<>();
    private final List<Path> outputFiles = new ArrayList<>();

    // Compilation behavior
    private boolean compileSeparately = false;
    private boolean verifyOnly = false;
    private String target = "svm";

    // Logging flags
    private final Set<String> loggingFlags = new HashSet<>();

    // Dump flags
    private final Set<String> dumpFlags = new HashSet<>();

    // Miscellaneous flags
    private boolean printHelp = false;
    private boolean printVersion = false;
    private boolean printTime = false;

    // === Constructor ===
    private CompilationOptions() {
    }

    // === Input / Output ===

    public List<Path> getInputFiles() {
        return inputFiles;
    }

    public void addInputFile(Path path) {
        inputFiles.add(path);
    }

    public List<Path> getOutputFiles() {
        return outputFiles;
    }

    public void addOutputFile(Path path) {
        outputFiles.add(path);
    }

    public void setOutputFiles(List<Path> paths) {
        outputFiles.clear();
        outputFiles.addAll(paths);
    }

    public Path getOutputFileFor(int index) {
        if (index >= 0 && index < outputFiles.size()) {
            return outputFiles.get(index);
        }
        return null;
    }

    // === Compilation Options ===

    public boolean isCompileSeparately() {
        return compileSeparately;
    }

    public void setCompileSeparately(boolean compileSeparately) {
        this.compileSeparately = compileSeparately;
    }

    public boolean isVerifyOnly() {
        return verifyOnly;
    }

    public void setVerifyOnly(boolean verifyOnly) {
        this.verifyOnly = verifyOnly;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    // === Logging ===

    public void enableLogging(String phase) {
        loggingFlags.add(phase.toLowerCase());
    }

    public boolean isLoggingEnabled(String phase) {
        return loggingFlags.contains("all") || loggingFlags.contains(phase.toLowerCase());
    }

    public Set<String> getEnabledLoggingFlags() {
        return loggingFlags;
    }

    // === Dumping ===

    public void enableDump(String type) {
        dumpFlags.add(type.toLowerCase());
    }

    public boolean isDumpEnabled(String type) {
        return dumpFlags.contains("all") || dumpFlags.contains(type.toLowerCase());
    }

    public Set<String> getEnabledDumpFlags() {
        return dumpFlags;
    }

    // === General Flags ===

    public boolean isPrintHelp() {
        return printHelp;
    }

    public void setPrintHelp(boolean printHelp) {
        this.printHelp = printHelp;
    }

    public boolean isPrintVersion() {
        return printVersion;
    }

    public void setPrintVersion(boolean printVersion) {
        this.printVersion = printVersion;
    }

    public boolean isPrintTime() {
        return printTime;
    }

    public void setPrintTime(boolean printTime) {
        this.printTime = printTime;
    }

    // === Utility ===

    public void reset() {
        inputFiles.clear();
        outputFiles.clear();
        loggingFlags.clear();
        dumpFlags.clear();
        compileSeparately = false;
        verifyOnly = false;
        target = "svm";
        printHelp = false;
        printVersion = false;
        printTime = false;
    }

}
