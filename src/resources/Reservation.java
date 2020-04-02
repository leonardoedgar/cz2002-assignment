package resources;

import java.util.Date;
public class Reservation {
	private double reservationNumber;
	private Guest guestDetails;
	private Date dateOfCheckIn;
	private Date dateOfCheckOut;
	private int noOfPeople;
	private String paymentType;
	private String status;
	
	/**
	 * Creates an instance of Reservation
	 * @param reservationNumber
	 * @param guestDetails
	 * @param dateOfCheckIn
	 * @param dateOfCheckOut
	 * @param noOfPeople
	 * @param paymentType
	 * @param status
	 */
	public Reservation(double reservationNumber,Guest guestDetails,Date dateOfCheckIn,Date dateOfCheckOut,int noOfPeople,String paymentType,String status){
		this.reservationNumber=reservationNumber;
		this.guestDetails=guestDetails;
		this.dateOfCheckIn=dateOfCheckIn;
		this.dateOfCheckOut=dateOfCheckOut;
		this.noOfPeople=noOfPeople;
		this.paymentType=paymentType;
		this.status=status;
		
	}
	
	public double getReservationNumber() {
		return reservationNumber;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void expire() {
		status="Expired";
	}
	
	public void updateStatus(String reservationStatus) {
		status=reservationStatus;
	}
	
	public void printReceipt() {
		System.out.print("Reservation Details:");
		System.out.print("Guest Name:"+guestDetails.getName);
		System.out.print("Date of checkin Details:"+dateOfCheckIn);
		System.out.print("Date of checkout Details:"+dateOfCheckOut);
		System.out.print("Number of people Details:"+noOfPeople);
		System.out.print("Payment type:"+paymentType);
		System.out.print("Status:"+status);
	}
}

