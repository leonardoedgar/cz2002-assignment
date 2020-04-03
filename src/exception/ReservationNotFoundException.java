package exception;
import exception.AppFailureException;

public class ReservationNotFoundException extends AppFailureException {
	public ReservationNotFoundException() {
		super("Reservation is not found.");
	}
	public ReservationNotFoundException(String message) {
		super(message);
	}
}
