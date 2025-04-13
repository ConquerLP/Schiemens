package ch.schiemens.util;

import java.nio.file.Path;

public class PositionInFile {

    private int line;
    private int columnStart;
    private int columnEnd;
    private Path filePath;

    public PositionInFile(int line, int columnStart, String value) {
        this.line = line;
        this.columnStart = columnStart;
        this.columnEnd = columnStart + value.length();
    }

    @Override
    public String toString() {
        return "<" + line + ":" + columnStart + "-" + columnEnd + ">";
    }

}
