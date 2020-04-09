package exception;
import exception.AppFailureException;

/**
 * An exception for hotel setup failure.
 */
@SuppressWarnings("serial")
public class HotelSetupFailureException extends AppFailureException {
	public HotelSetupFailureException() {
		super("Failure in setting up hotel.");
	}
	public HotelSetupFailureException(String message) {
		super(message);
	}
}
