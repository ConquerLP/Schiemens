package ch.schiemens.util;

public class PositionInFile {

    private final int line;
    private final int columnStart;
    private final int columnEnd;

    public PositionInFile(int line, int columnEnd, String value) {
        this.line = line;
        this.columnStart = columnEnd - value.length();
        this.columnEnd = columnEnd;
    }

    public PositionInFile(int line, int columnEnd) {
        this.line = line;
        this.columnStart = columnEnd;
        this.columnEnd = columnEnd;
    }

    @Override
    public String toString() {
        return "<" + line + ":" + columnStart + ":" + columnEnd + ">";
    }

}
