package exception;
import exception.AppFailureException;

/**
 * An exception for reservation not found.
 */
@SuppressWarnings("serial")
public class ReservationNotFoundException extends AppFailureException {
	public ReservationNotFoundException() {
		super("Reservation is not found.");
	}
	public ReservationNotFoundException(String message) {
		super(message);
	}
}
