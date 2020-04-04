package exception;
import exception.AppFailureException;

/**
 * An exception for not able to find room within the system.
 */
@SuppressWarnings("serial")
public class RoomNotFoundException extends AppFailureException{
	public RoomNotFoundException() {
		super("Room does not exist in the hotel system.");
	}
	public RoomNotFoundException(String message) {
		super(message);
	}
}
