package util.exceptions;

public class InvalidInputException extends RuntimeException {

	private static final long serialVersionUID = 1758587393641179176L;

	public InvalidInputException() {
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
