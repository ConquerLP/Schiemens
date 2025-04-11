package ch.codegen.cli;

import ch.codegen.util.LoggerCli;

import java.io.File;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CompileOptions {

    private static final CompileOptions INSTANCE = new CompileOptions();

    private static boolean ast = false;
    private static boolean log = false;
    private static boolean time = false;
    private static boolean verify = false;
    private static boolean singleCompilation = false;
    private static String target = "sm";
    private static final Set<File> inputFiles = new LinkedHashSet<>();

    private CompileOptions() {
    }

    public static CompileOptions getInstance() {
        return INSTANCE;
    }

    public static void parseArguments(Map<String, String> options) {
        ast = options.containsKey("-ast");
        log = options.containsKey("-log");
        time = options.containsKey("-time");
        verify = options.containsKey("-verify");
        singleCompilation = options.containsKey("-c");
        if (options.containsKey("-target")) {
            String t = options.get("-target");
            if (t != null && !t.isEmpty()) {
                target = t;
            }
        }
        if (options.containsKey("-i")) {
            String input = options.get("-i");
            if (input != null && !input.isEmpty()) {
                String[] patterns = input.split("[,\\s]+");
                for (String pattern : patterns) {
                    List<File> matches = resolvePattern(pattern);
                    if (matches.isEmpty()) {
                        LoggerCli.warn("No valid files found for: " + pattern);
                    }
                    inputFiles.addAll(matches);
                }
            }
        }
        if (inputFiles.isEmpty()) {
            LoggerCli.error("No valid .sc files.");
        }
    }

    private static List<File> resolvePattern(String pattern) {
        try {
            Path baseDir = Paths.get(".").toAbsolutePath().normalize();
            if (!pattern.contains("*") && !pattern.contains("?")) {
                File f = new File(pattern);
                return (f.exists() && f.getName().endsWith(".sc")) ? List.of(f.getCanonicalFile()) : List.of();
            }
            String regex = patternToRegex(pattern);
            Pattern compiled = Pattern.compile(regex);
            try (var stream = Files.walk(baseDir)) {
                return stream
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .filter(f -> f.getName().endsWith(".sc"))
                        .filter(f -> compiled.matcher(baseDir.relativize(f.toPath()).toString().replace("\\", "/")).matches())
                        .map(f -> {
                            try {
                                return f.getCanonicalFile();
                            } catch (Exception e) {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            LoggerCli.error("Error whilst matching the pattern: " + pattern + " – " + e.getMessage());
            return List.of();
        }
    }

    private static String patternToRegex(String glob) {
        return "^" + glob
                .replace(".", "\\.")
                .replace("**", "§§")
                .replace("*", "[^/]*")
                .replace("?", ".")
                .replace("§§", ".*")
                + "$";
    }

    public static boolean isAst() {
        return ast;
    }

    public static boolean isLog() {
        return log;
    }

    public static boolean isTime() {
        return time;
    }

    public static boolean isVerify() {
        return verify;
    }

    public static String getTarget() {
        return target;
    }

    public static boolean isSingleCompilation() {
        return singleCompilation;
    }

    public static List<File> getInputFiles() {
        return new ArrayList<>(inputFiles);
    }

}
