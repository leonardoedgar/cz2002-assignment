package exception;
import exception.AppFailureException;

/**
 * An exception for invalid guest detail.
 */
@SuppressWarnings("serial")
public class InvalidGuestDetailException extends AppFailureException {
		public InvalidGuestDetailException() {
			super("Invalid guest details.");
		}
		public InvalidGuestDetailException (String message) {
			super(message);
		}
}
