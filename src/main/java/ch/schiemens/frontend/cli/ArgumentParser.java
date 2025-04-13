package ch.schiemens.frontend.cli;

import ch.schiemens.frontend.core.compilationOptions.CompilationOptions;

import java.nio.file.Path;
import java.util.*;

public class ArgumentParser {

    public static void parseArguments(String[] args, CompilationOptions options) {
        Map<String, String> argsMap = parseArgs(args);
        for (Map.Entry<String, String> entry : argsMap.entrySet()) {
            String flag = entry.getKey();
            String value = entry.getValue();
            switch (flag) {
                case "-i" -> {
                    List<Path> inputFiles = parsePaths(value);
                    options.setInputFiles(inputFiles);
                }
                case "-o" -> {
                    List<Path> outputFiles = parsePaths(value);
                    options.setOutputFiles(outputFiles);
                }
                case "-target" -> options.setTarget(value.isEmpty() ? "svm" : value);
                case "-c" -> options.setCompileSeparately(true);
                case "-verify" -> options.setVerifyOnly(true);
                case "-time" -> options.setPrintTime(true);
                case "-log-all" -> {
                    options.setLogPrepro(true);
                    options.setLogLexing(true);
                    options.setLogParsing(true);
                    options.setLogAst(true);
                }
                case "-log-prepro" -> options.setLogPrepro(true);
                case "-log-lexing" -> options.setLogLexing(true);
                case "-log-parsing" -> options.setLogParsing(true);
                case "-log-ast" -> options.setLogAst(true);
                case "-dump-tokens" -> options.setDumpTokens(true);
                case "-dump-parse" -> options.setDumpParseTree(true);
                case "-dump-ast" -> options.setDumpAst(true);
                case "-dump-ir" -> options.setDumpIr(true);
                case "-dump-bytecode" -> options.setDumpBytecode(true);
                case "-dump-all" -> {
                    options.setDumpTokens(true);
                    options.setDumpParseTree(true);
                    options.setDumpAst(true);
                    options.setDumpIr(true);
                    options.setDumpBytecode(true);
                }
                default -> System.err.println("Unknown compiler option: " + flag);
            }
        }
        validateOptions(options);
    }

    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < args.length; i++) {
            String current = args[i];
            if (current.startsWith("-")) {
                String value = (i + 1 < args.length && !args[i + 1].startsWith("-")) ? args[i + 1] : "";
                map.put(current, value);
                if (!value.isEmpty()) i++;
            }
        }
        return map;
    }

    private static List<Path> parsePaths(String value) {
        if (value == null || value.trim().isEmpty()) return Collections.emptyList();
        String[] parts = value.trim().split(",");
        List<Path> result = new ArrayList<>();
        for (String part : parts) {
            result.add(Path.of(part.trim()));
        }
        return result;
    }

    private static void validateOptions(CompilationOptions options) {
        List<Path> input = options.getInputFiles();
        List<Path> output = options.getOutputFiles();

        if (input.isEmpty()) {
            System.err.println("[ERROR] No input files specified using -i");
            System.exit(1);
        }

        if (options.isCompileSeparately()) {
            if (output.isEmpty()) {
                System.err.println("[ERROR] Output files must be specified with -o when using -c");
                System.exit(1);
            }
            if (input.size() != output.size()) {
                System.err.printf("[ERROR] In -c mode, input count (%d) must match output count (%d)%n",
                        input.size(), output.size());
                System.exit(1);
            }
        } else {
            if (input.size() > 1) {
                System.err.println("[ERROR] Multiple input files require -c flag (compile separately)");
                System.exit(1);
            }
            if (output.size() > 1) {
                System.err.println("[ERROR] Only one output file allowed without -c");
                System.exit(1);
            }
            if (output.isEmpty() && !options.isVerifyOnly()) {
                System.out.println("[WARN] No output specified. Compilation will proceed without writing .pain");
            }
        }
    }

}
