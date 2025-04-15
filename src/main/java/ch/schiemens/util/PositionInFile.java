package ch.schiemens.util;

public class PositionInFile {

    private final int line;
    private final int columnStart;
    private final int columnEnd;

    public PositionInFile(int line, int columnStart, int columnEnd) {
        this.line = line;
        this.columnStart = columnStart;
        this.columnEnd = columnEnd;
    }


    @Override
    public String toString() {
        return "<" + line + ":" + columnStart + ":" + columnEnd + ">";
    }

}
