package ch.schiemens.frontend.lexer;

import ch.schiemens.util.PositionInFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class LexerBuffer {

    private final BufferedReader reader;
    private int current = -2;
    private final static int TAB_SIZE = 4;
    private int line = 1;
    private int column = 1;

    public LexerBuffer(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public int peek() throws IOException {
        if (current == -2) {
            reader.mark(1);
            current = reader.read();
            reader.reset();
        }
        return current;
    }

    public int consume() throws IOException {
        int result;
        if (current != -2) {
            result = current;
            current = -2;
        } else {
            result = reader.read();
        }
        switch (result) {
            case '\n', '\f' -> {
                line++;
                column = 1;
            }
            case '\r' -> {
                reader.mark(1);
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

    public boolean isEOF() throws IOException {
        return peek() == -1;
    }

    public PositionInFile makePositionInFile() {
        return new PositionInFile(line, column);
    }

    public PositionInFile makePositionInFile(String value) {
        return new PositionInFile(line, column, value);
    }

}
