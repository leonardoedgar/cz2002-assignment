package exception;
import exception.AppFailureException;

/**
 * An exception for failure in updating room's detail.
 */
@SuppressWarnings("serial")
public class RoomDetailUpdateFailureException extends AppFailureException{
	public RoomDetailUpdateFailureException() {
		super("Failed to update room's details.");
	}
	public RoomDetailUpdateFailureException(String message) {
		super(message);
	}
}
