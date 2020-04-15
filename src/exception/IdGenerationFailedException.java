package exception;
import exception.AppFailureException;

/**
* An exception for not being able to generate a unique id after a certain amount of attempts.
*/
@SuppressWarnings("serial")
public class IdGenerationFailedException extends AppFailureException {
	public IdGenerationFailedException() {
		super("Failed to generate unique id.");
	}
	public IdGenerationFailedException (String message) {
		super(message);
	}
}
