package exception;
import exception.AppFailureException;

/**
 * An exception for duplicate reservation found.
 */
@SuppressWarnings("serial")
public class DuplicateReservationFoundException extends AppFailureException {
	public DuplicateReservationFoundException() {
		super("Duplicate reservation id found.");
	}
	public DuplicateReservationFoundException(String message) {
		super(message);
	}
}
