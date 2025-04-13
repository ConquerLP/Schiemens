package ch.schiemens.exception;

public class CLIException extends RuntimeException {

    public CLIException(String message) {
        super(message);
    }

    public CLIException(String message, Throwable cause) {
        super(message, cause);
    }

}
