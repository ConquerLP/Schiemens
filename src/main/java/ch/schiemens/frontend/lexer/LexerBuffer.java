package ch.schiemens.frontend.lexer;

import java.io.IOException;
import java.io.Reader;

public class LexerBuffer {

    private final Reader reader;
    private int current = -2;
    private final static int tabSize = 4;
    private int line = 1;
    private int column = 1;

    public LexerBuffer(Reader reader) {
        this.reader = reader.markSupported() ? reader : new java.io.BufferedReader(reader);
    }

    public int peek() throws IOException {
        if (current == -2) {
            current = reader.read();
        }
        return current;
    }

    public int advance() throws IOException {
        int result = peek();
        current = -2;
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
            case '\t' -> column += tabSize - ((column - 1) % tabSize);
            default -> column++;
        }
        return result;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public boolean isEOF() throws IOException {
        return peek() == -1;
    }

}
