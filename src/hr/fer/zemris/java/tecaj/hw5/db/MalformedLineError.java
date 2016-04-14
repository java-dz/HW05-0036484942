package hr.fer.zemris.java.tecaj.hw5.db;

/**
 * An instance of class MalformedLineError should be thrown in case of detecting
 * a malformed line in files.
 *
 * @author Mario Bobic
 */
public class MalformedLineError extends Error {
	/** Serialization UID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new error with {@code null} as its detail message. The cause
	 * is not initialized, and may subsequently be initialized by a call to
	 * {@link #initCause}.
	 */
	public MalformedLineError() {
		super();
	}

	/**
	 * Constructs a new error with the specified detail message. The cause is
	 * not initialized, and may subsequently be initialized by a call to
	 * {@link #initCause}.
	 *
	 * @param message
	 *            the detail message. The detail message is saved for later
	 *            retrieval by the {@link #getMessage()} method.
	 */
	public MalformedLineError(String message) {
		super(message);
	}

	/**
	 * Constructs a new error with the specified detail message and cause.
	 * <p>
	 * Note that the detail message associated with {@code cause} is <i>not</i>
	 * automatically incorporated in this error's detail message.
	 *
	 * @param message
	 *            the detail message (which is saved for later retrieval by the
	 *            {@link #getMessage()} method).
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A {@code null} value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public MalformedLineError(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new error with the specified cause and a detail message of
	 * {@code (cause==null ? null : cause.toString())} (which typically contains
	 * the class and detail message of {@code cause}). This constructor is
	 * useful for errors that are little more than wrappers for other
	 * throwables.
	 *
	 * @param cause
	 *            the cause (which is saved for later retrieval by the
	 *            {@link #getCause()} method). (A {@code null} value is
	 *            permitted, and indicates that the cause is nonexistent or
	 *            unknown.)
	 */
	public MalformedLineError(Throwable cause) {
		super(cause);
	}

}
