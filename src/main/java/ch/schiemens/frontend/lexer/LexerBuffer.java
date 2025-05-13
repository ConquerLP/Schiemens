package ch.schiemens.frontend.lexer;

import ch.schiemens.util.PositionInFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class LexerBuffer {

    private final BufferedReader reader;
    private int current = -2;
    private static final int TAB_SIZE = 4;

    private int line = 1;
    private int column = 1;

    private int lastChar = -2;
    private int lastLine = 1;
    private int lastColumn = 1;
    private boolean canGoBack = false;

    public LexerBuffer(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public int consume() throws IOException {
        int result;
        if (current != -2) {
            result = current;
            current = -2;
        } else {
            result = reader.read();
        }
        lastChar = result;
        lastLine = line;
        lastColumn = column;
        canGoBack = true;
        switch (result) {
            case '\n', '\f' -> {
                line++;
                column = 1;
            }
            case '\r' -> {
                int next = reader.read();
                if (next != '\n') {
                    current = next;
                }
                line++;
                column = 1;
            }
            case '\t' -> column += TAB_SIZE - ((column - 1) % TAB_SIZE);
            default -> column++;
        }
        return result;
    }

    public void goBack() {
        if (!canGoBack || lastChar == -1) {
            throw new IllegalStateException("Cannot go back more than one step or after EOF");
        }
        current = lastChar;
        line = lastLine;
        column = lastColumn;
        canGoBack = false;
    }

    public static boolean isEOF(int current) {
        return current == -1;
    }

    public PositionInFile makePositionInFile() {
        return new PositionInFile(line, column);
    }

    public PositionInFile makePositionInFile(String value) {
        return new PositionInFile(line, column, value);
    }

}
