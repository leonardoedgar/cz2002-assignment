package exception;
import exception.AppFailureException;

/**
 * An exception for not able to find guests within the system.
 */
@SuppressWarnings({ "serial" })
public class GuestNotFoundException extends AppFailureException{
	public GuestNotFoundException() {
		super("Guest does not exist in the hotel system.");
	}
	public GuestNotFoundException(String message) {
		super(message);
	}
}
