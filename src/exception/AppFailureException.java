package exception;
import java.lang.Exception;

/**
 * An exception for application failure.
 */
@SuppressWarnings("serial")
public class AppFailureException extends Exception {
	public AppFailureException() {
		super("App exception.");
	}
	public AppFailureException(String message) {
		super(message);
	}
}
