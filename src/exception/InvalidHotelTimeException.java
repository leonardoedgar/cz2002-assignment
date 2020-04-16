package exception;
import exception.AppFailureException;

/**
 * An exception for invalid hotel time.
 */
@SuppressWarnings("serial")
public class InvalidHotelTimeException extends AppFailureException {
	public InvalidHotelTimeException() {
		super("Invalid hotel time.");
	}
	public InvalidHotelTimeException(String message) {
		super(message);
	}
}
