package exception;
import exception.AppFailureException;

/**
 * An exception for not able to find room within the system.
 */
@SuppressWarnings("serial")
public class RoomTypeNotFoundException extends AppFailureException{
	public RoomTypeNotFoundException() {
		super("Room type does not exist in the hotel system.");
	}
	public RoomTypeNotFoundException(String message) {
		super(message);
	}
}
