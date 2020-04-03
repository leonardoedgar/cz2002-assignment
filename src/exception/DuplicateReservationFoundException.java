package exception;
import exception.AppFailureException;

public class DuplicateReservationFoundException extends AppFailureException {
	public DuplicateReservationFoundException() {
		super("Duplicate reservation id found.");
	}
	public DuplicateReservationFoundException(String message) {
		super(message);
	}
}
