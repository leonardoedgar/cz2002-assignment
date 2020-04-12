package exception;
import exception.AppFailureException;

/**
 * An exception for food not on menu.
 */
@SuppressWarnings("serial")
public class FoodNotOnMenuException extends AppFailureException {
	public FoodNotOnMenuException() {
		super("Food not on menu.");
	}
	public FoodNotOnMenuException(String message) {
		super(message);
	}
}
