package hr.fer.zemris.java.tecaj.hw5.db.parser;

/**
 * Exception that is thrown if an unexpected problem occurs during the parsing.
 *
 * @author Mario Bobic
 */
public class QueryParserException extends RuntimeException {
    /** Serialization UID. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an {@code QueryParserException} with no
     * detail message and no cause.
     */
    public QueryParserException() {
        super();
    }

    /**
     * Constructs an {@code QueryParserException} with the
     * specified detail message.
     *
     * @param message the detail message.
     */
    public QueryParserException(String message) {
        super(message);
    }

    /**
     * Constructs an {@code QueryParserException} with the
     * specified cause.
     *
     * @param cause the cause
     */
    public QueryParserException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructs an {@code QueryParserException} with the
     * specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause
     */
    public QueryParserException(String message, Throwable cause) {
        super(message, cause);
    }

}
