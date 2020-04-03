package exception;
import exception.AppFailureException;

public class InvalidReservationDetailException extends AppFailureException {
	public InvalidReservationDetailException() {
		super("Invalid reservation details.");
	}
	public InvalidReservationDetailException(String message) {
		super(message);
	}
}
