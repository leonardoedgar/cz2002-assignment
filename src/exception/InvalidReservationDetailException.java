package exception;
import exception.AppFailureException;
/**
 * An exception for invalid reservation detail.
 */
@SuppressWarnings("serial")
public class InvalidReservationDetailException extends AppFailureException {
	public InvalidReservationDetailException() {
		super("Invalid reservation details.");
	}
	public InvalidReservationDetailException(String message) {
		super(message);
	}
}
