package resources;
import java.util.Date;

/**
 * A class to represent a reservation in a hotel.
 */
public class Reservation {
	private String reservationId;
	private Guest guestDetails;
	private Date dateOfCheckIn;
	private Date dateOfCheckOut;
	private int noOfPeople;
	private String paymentType;
	private String status;
	private String roomType;
	
	/**
	 * A class constructor for reservation object.
	 * @param reservationId {String} the reservation id
	 * @param guestDetails {Guest} the guest object
	 * @param dateOfCheckIn {Date} date of check in
	 * @param dateOfCheckOut {Date} date of check out
	 * @param noOfPeople {int} number of people in 1 room
	 * @param paymentType {String} the payment type
	 * @param roomType {String} the room type
	 */
	public Reservation(String reservationId,Guest guestDetails, Date dateOfCheckIn, Date dateOfCheckOut,
			int noOfPeople,String paymentType,String roomType){
		this.reservationId=reservationId;
		this.guestDetails=guestDetails;
		this.dateOfCheckIn=dateOfCheckIn;
		this.dateOfCheckOut=dateOfCheckOut;
		this.noOfPeople=noOfPeople;
		this.paymentType=paymentType;
		this.status="not-received";
		this.roomType=roomType;
	}
	
	/**
	 * A function to get the reservation id.
	 * @return {String} the reservation id
	 */
	public String getReservationId() {
		return this.reservationId;
	}
	
	/**
	 * A function to get the guest detail.
	 * @return {Guest} the guest object
	 */
	public Guest getGuest() {
		return this.guestDetails;
	}
	
	/**
	 * A function to get the payment type.
	 * @return {String} the payment type
	 */
	public String getPaymentType() {
		return this.paymentType;
	}
	
	/**
	 * A function to get the number of people in 1 room.
	 * @return {int} the number of people in 1 room
	 */
	public int getNoOfPeople() {
		return this.noOfPeople;
	}
	
	/**
	 * A function to get the reservation status.
	 * @return {String} the status of the reservation
	 */
	public String getStatus() {
		return this.status;
	}
	
	/**
	 * A function to expire a reservation.
	 */
	public void expire() {
		this.updateStatus("expired");
	}
	
	/**
	 * A function to update the status of a reservation.
	 * @param reservationStatus {String} the status to update to
	 */
	public void updateStatus(String reservationStatus) {
		this.status=reservationStatus;
	}
	
	/**
	 * A function to print the reservation receipt.
	 */
	public void printReceipt() {
		int lengthOfSymbol = this.dateOfCheckIn.toString().length()+ 18;
		String header = "RECEIPT";
		int lengthOfSymbolWithoutHeader = lengthOfSymbol - header.length();
		String symbol = "=";
		for(int i=0; i<2; i++) {
			for(int j=0; j<lengthOfSymbolWithoutHeader/2 ; j++) {
				System.out.print(symbol);
			}
			if (i==0) {
				System.out.print(header);
			}
			else if (i==1) {
				System.out.print(symbol);
				System.out.println();
			}
		}
		this.printDetails();
		for (int i=0; i<lengthOfSymbol; i++) {
			System.out.print(symbol);
		}
		System.out.println();
	}
	
	/**
	 * A function to get the date of check in.
	 * @return {Date} the check in date
	 */
	public Date getDateOfCheckIn() {
		return this.dateOfCheckIn;
	}
	
	/**
	 * A function to get the date of check out.
	 * @return {Date} the check out date
	 */
	public Date getDateOfCheckOut() {
		return this.dateOfCheckOut;
	}
	
	/**
	 * A function to get the room type.
	 * @return {String} the room type
	 */
	public String getRoomType() {
		return this.roomType;
	}
	
	/**
	 * A function to print the detail of a reservation.
	 */
	public void printDetails() {
		System.out.println("Reservation id  : " + this.reservationId);
		System.out.println("Guest name      : " + this.guestDetails.getName());
		System.out.println("Date of checkin : " + this.dateOfCheckIn);
		System.out.println("Date of checkout: " + this.dateOfCheckOut);
		System.out.println("Number of people: " + this.noOfPeople);
		System.out.println("Payment type    : " + this.paymentType);
		System.out.println("Status          : " + this.status);
		System.out.println("Room type       : " + this.roomType);
	}
	
	/**
	 * A function to copy a reservation.
	 * @param reservation {Reservation} the reservation object to copy.
	 * @return {Reservation} a new copied reservation object
	 */
	public static Reservation copy(Reservation reservation) {
		return new Reservation(
			reservation.getReservationId(),
			reservation.getGuest(),
			reservation.getDateOfCheckIn(),
			reservation.getDateOfCheckOut(),
			reservation.getNoOfPeople(),
			reservation.getPaymentType(),
			reservation.getRoomType()
		);
	}
}
