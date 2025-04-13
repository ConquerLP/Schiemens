package ch.schiemens.frontend.core.compilationOptions;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CompilationOptions {

    private static final CompilationOptions INSTANCE = new CompilationOptions();
    private static final String FORMAT_HELP = "\t%-18s %-25s %s%n";
    private static final String VERSION = "1.0.69";

    private final List<Path> inputFiles = new ArrayList<>();
    private final List<Path> outputFiles = new ArrayList<>();

    private boolean compileSeparately;
    private boolean verifyOnly;
    private boolean printTime;

    private boolean logPrepro;
    private boolean logLexing;
    private boolean logParsing;
    private boolean logAst;

    private boolean dumpTokens;
    private boolean dumpParseTree;
    private boolean dumpAst;
    private boolean dumpIr;
    private boolean dumpBytecode;

    private String target = "svm";

    private CompilationOptions() {
    }

    public static CompilationOptions getInstance() {
        return INSTANCE;
    }

    public List<Path> getInputFiles() {
        return inputFiles;
    }

    public void setInputFiles(List<Path> paths) {
        inputFiles.clear();
        inputFiles.addAll(paths);
    }

    public List<Path> getOutputFiles() {
        return outputFiles;
    }

    public void setOutputFiles(List<Path> paths) {
        outputFiles.clear();
        outputFiles.addAll(paths);
    }

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

    public boolean isPrintTime() {
        return printTime;
    }

    public void setPrintTime(boolean printTime) {
        this.printTime = printTime;
    }

    public boolean isLogPrepro() {
        return logPrepro;
    }

    public void setLogPrepro(boolean logPrepro) {
        this.logPrepro = logPrepro;
    }

    public boolean isLogLexing() {
        return logLexing;
    }

    public void setLogLexing(boolean logLexing) {
        this.logLexing = logLexing;
    }

    public boolean isLogParsing() {
        return logParsing;
    }

    public void setLogParsing(boolean logParsing) {
        this.logParsing = logParsing;
    }

    public boolean isLogAst() {
        return logAst;
    }

    public void setLogAst(boolean logAst) {
        this.logAst = logAst;
    }

    public boolean isDumpTokens() {
        return dumpTokens;
    }

    public void setDumpTokens(boolean dumpTokens) {
        this.dumpTokens = dumpTokens;
    }

    public boolean isDumpParseTree() {
        return dumpParseTree;
    }

    public void setDumpParseTree(boolean dumpParseTree) {
        this.dumpParseTree = dumpParseTree;
    }

    public boolean isDumpAst() {
        return dumpAst;
    }

    public void setDumpAst(boolean dumpAst) {
        this.dumpAst = dumpAst;
    }

    public boolean isDumpIr() {
        return dumpIr;
    }

    public void setDumpIr(boolean dumpIr) {
        this.dumpIr = dumpIr;
    }

    public boolean isDumpBytecode() {
        return dumpBytecode;
    }

    public void setDumpBytecode(boolean dumpBytecode) {
        this.dumpBytecode = dumpBytecode;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void printVersion() {
        System.out.println("Schiemens Compiler Version: " + VERSION);
    }

    public static void printHelp() {
        System.out.println("Schiemens Compiler Options:\n");

        System.out.println("General:");
        printOption("-help", "", "Shows help screen");
        printOption("-version", "", "Prints the compiler version");
        printOption("-i", "<source-file(s)>", "Input source files (.sc)");
        printOption("-o", "<output-file>", "Output path for .pain bytecode");
        printOption("-c", "", "Compile each file separately");
        printOption("-time", "", "Prints duration of each compilation phase");

        System.out.println("\nLogging Flags:");
        printOption("-log-all", "", "Logs all frontend phases");
        printOption("-log-prepro", "", "Logs preprocessor steps");
        printOption("-log-lexing", "", "Logs tokenization");
        printOption("-log-parsing", "", "Logs parser decisions");
        printOption("-log-ast", "", "Logs AST construction");

        System.out.println("\nVerification:");
        printOption("-verify", "", "Checks only syntax and semantics (no output)");

        System.out.println("\nTarget Generation:");
        printOption("-target", "<svm|jasmin|ir>", "Target backend for output (default: svm)");

        System.out.println("\nDump Intermediate:");
        printOption("-dump-tokens", "", "Dump tokens to .tokens");
        printOption("-dump-parse", "", "Dump parse tree to .parse");
        printOption("-dump-ast", "", "Dump AST to .ast");
        printOption("-dump-ir", "", "Dump IR to .ir");
        printOption("-dump-bytecode", "", "Dump bytecode to .bytecode");
        printOption("-dump-all", "", "Dump all intermediate stages");
    }

    private static void printOption(String flag, String param, String description) {
        System.out.printf(FORMAT_HELP, flag, param, description);
    }

}
