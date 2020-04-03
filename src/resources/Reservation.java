package resources;
import java.util.Date;

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
	 * Creates an instance of Reservation
	 * @param reservationId
	 * @param guestDetails
	 * @param dateOfCheckIn
	 * @param dateOfCheckOut
	 * @param noOfPeople
	 * @param paymentType
	 * @param status
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
	
	public String getReservationId() {
		return this.reservationId;
	}
	
	public Guest getGuest() {
		return this.guestDetails;
	}
	
	public String getPaymentType() {
		return this.paymentType;
	}
	
	public int getNoOfPeople() {
		return this.noOfPeople;
	}
	
	public String getStatus() {
		return this.status;
	}
	
	public void expire() {
		this.status="Expired";
	}
	
	public void updateStatus(String reservationStatus) {
		this.status=reservationStatus;
	}
	
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
	
	public Date getDateOfCheckIn() {
		return this.dateOfCheckIn;
	}
	
	public Date getDateOfCheckOut() {
		return this.dateOfCheckOut;
	}
	public String getRoomType() {
		return this.roomType;
	}
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
