package ch.schiemens.frontend.core.compilationOptions;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class OutputFileResolver {

    public static List<Path> autoGenerateOutputs(List<Path> inputFiles, Path baseDirectory, String extension) {
        List<Path> result = new ArrayList<>();
        for (Path input : inputFiles) {
            String name = stripExtension(input.getFileName().toString());
            Path output = baseDirectory.resolve(name + "." + extension);
            result.add(output);
        }
        return result;
    }

    private static String stripExtension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot > 0) {
            return filename.substring(0, dot);
        }
        return filename;
    }

}
