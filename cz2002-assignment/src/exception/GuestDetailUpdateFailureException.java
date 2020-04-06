package exception;
import exception.AppFailureException;

/**
 * An exception for failure in updating guest's detail.
 */
public class GuestDetailUpdateFailureException extends AppFailureException{
	public GuestDetailUpdateFailureException() {
		super("Failed to update guest's details.");
	}
	public GuestDetailUpdateFailureException(String message) {
		super(message);
	}
}
