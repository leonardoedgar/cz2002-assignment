package resources;
import java.util.ArrayList;

/**
 * A class that represents payment in a hotel.
 */
public class Payment {
	private String paymentType;
	private String roomType;
	private double totalRoomCost;
	private ArrayList<RoomService> roomServiceList;
	private double taxRate;
	private double discountRate;
	
	/**
	 * A class constructor to create payment object
	 * @param paymentType {String} indicates the payment type
	 * @param roomType {String} the room type the person stayed at the hotel
	 * @param totalRoomCost {Double} the total room cost the person stayed at the hotel
	 * @param roomServiceList {ArrayList<RoomService>} the room services that are made by the guest
	 * @param taxRate {Double} tax rate for the payment
	 * @param discountRate {Double} discount for the payment
	 */
	public Payment(String paymentType, String roomType, double totalRoomCost, 
			ArrayList<RoomService> roomServiceList, double taxRate, double discountRate) {
		this.paymentType = paymentType;
		this.roomType = roomType;
		this.totalRoomCost = totalRoomCost;
		this.roomServiceList = roomServiceList;
		this.taxRate = taxRate;
		this.discountRate = discountRate;
	}
	
	public Payment(String paymentType, String roomType, double totalRoomCost, 
			ArrayList<RoomService> roomServiceList) {
		this(paymentType, roomType, totalRoomCost, roomServiceList, 0.07, 0);
	}
	
	public Payment(String paymentType, String roomType, double totalRoomCost, 
			ArrayList<RoomService> roomServiceList, double taxRate) {
		this(paymentType, roomType, totalRoomCost, roomServiceList, taxRate, 0);
	}
	
	/**
	 * A function to print the payment receipt.
	 */
	public void printReceipt() {
		double totalCost = this.totalRoomCost;
		System.out.println("           HOTEL CHECKOUT");
		System.out.println("===================================");
		System.out.println("Room type\t: " + this.roomType);
		System.out.println("Total room cost\t: SGD" + String.format("%.2f", this.totalRoomCost));
		System.out.println("Room order history: ");
		for (RoomService roomService: roomServiceList) {
			roomService.printOrderHistory();
			totalCost += roomService.getTotalCost();
		}
		System.out.println("===================================");
		System.out.println("Total cost before tax\t: SGD" + 
			String.format("%.2f", totalCost));
		double discountAmount = this.discountRate*totalCost;
		totalCost -= discountAmount;
		System.out.println("Discount rate(" + String.format("%.2f", this.discountRate*100) + "%)\t: SGD" + 
			String.format("%.2f", discountAmount));
		double taxAmount = this.taxRate*totalCost;
		totalCost += taxAmount;
		System.out.println("Tax rate("      + String.format("%.2f", this.taxRate*100) + "%)\t\t: SGD" + 
			String.format("%.2f", taxAmount));
		System.out.println("Total cost after tax\t: SGD" + 
			String.format("%.2f", totalCost));
		System.out.println("Payment made by\t: " + this.paymentType);
		System.out.println("===================================");
		System.out.println("  THANK YOU FOR STAYING WITH US!");
		System.out.println("           SEE YOU AGAIN!");
	}
}

