package ch.schiemens.util;

public class PositionInFile {

    private final int line;
    private final int columnStart;
    private int columnEnd;

    public PositionInFile(int line, int columnStart) {
        this.line = line;
        this.columnStart = columnStart;
        this.columnEnd = columnStart;
    }

    public void setEnd(String value) {
        this.columnEnd = columnStart + value.length();
    }

    @Override
    public String toString() {
        return "<" + line + ":" + columnStart + "-" + columnEnd + ">";
    }

}
