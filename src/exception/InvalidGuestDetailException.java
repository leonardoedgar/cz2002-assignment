package exception;
import exception.AppFailureException;

public class InvalidGuestDetailException extends AppFailureException {
		public InvalidGuestDetailException() {
			super("Invalid guest details.");
		}
		public InvalidGuestDetailException (String message) {
			super(message);
		}
}
