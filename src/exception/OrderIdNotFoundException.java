package exception;
/**
 * Exception handling if orderid not found
 * @param message
 */
public class OrderIdNotFoundException extends AppFailureException {
	public OrderIdNotFoundException() {
		super("Invalid Order ID");
	}
	public OrderIdNotFoundException (String message) {
		super(message);
	}
}