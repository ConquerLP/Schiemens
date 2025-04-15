package ch.schiemens.frontend.lexer;

import java.io.IOException;
import java.io.Reader;

public class LexerBuffer {

    private final Reader reader;
    private int current = -2;
    private int line = 1;
    private int column = 0;

    public LexerBuffer(Reader reader) {
        this.reader = reader;
    }

    public int peek() throws IOException {
        if (current == -2) current = reader.read();
        return current;
    }

    public int advance() throws IOException {
        int result = peek();
        current = -2;
        if (result == '\n') {
            line++;
            column = 0;
        } else {
            column++;
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
