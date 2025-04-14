package ch.schiemens.util;

public class PositionInFile {

    private int line;
    private int columnStart;
    private int columnEnd;

    public PositionInFile(int line, int columnStart, String value) {
        this.line = line;
        this.columnStart = columnStart;
        this.columnEnd = columnStart + value.length();
    }

    public PositionInFile(int line, int columnStart) {
        this.line = line;
        this.columnStart = columnStart;
        this.columnEnd = columnStart;
    }

    @Override
    public String toString() {
        return "<" + line + ":" + columnStart + "-" + columnEnd + ">";
    }

}
