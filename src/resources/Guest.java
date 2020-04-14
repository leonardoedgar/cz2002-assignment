package resources;
import java.util.Date;
import java.util.Set;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.ArrayList;

import exception.FoodNotOnMenuException;
import exception.GuestDetailUpdateFailureException;
import resources.Payment;

/**
 * A class to represent a guest in a hotel.
 */
public class Guest {
	private String name;
	private String cardDetails;
	private String address;
	private String country;
	private String gender;
	private String nationality;
	private int contact;
	private String identity;
	private String paymentType;
	private ArrayList<RoomService> roomServiceList;
	private Date startDateOfStay;
	private Date endDateOfStay;
	
	/**
	 * A class constructor to create a guest.
	 * @param name {String} the name of the guest
	 * @param cardDetails {String} the card detail
	 * @param address {String} the address
	 * @param country {String} the country the guest from
	 * @param gender {String} the gender
	 * @param nationality {String} the nationality
	 * @param contact {int} the contact number
	 * @param identity {String} the identity card number of the guest
	 * @param startDateOfStay {Date} start date of guest's stay
	 * @param endDateOfStay {Date} end date of guest's stay
	 */
	public Guest(String name, String cardDetails, String address, String country, String gender, 
			String nationality, int contact, String identity,Date startDateOfStay,Date endDateOfStay,
			String paymentType) {
		this.name = name;
		this.cardDetails = cardDetails;
		this.address = address;
		this.country = country;
		this.gender = gender;
		this.nationality = nationality;
		this.contact = contact;
		this.identity=identity; 
		this.roomServiceList = new ArrayList<RoomService>();
		this.startDateOfStay=startDateOfStay;
		this.endDateOfStay=endDateOfStay;
		this.paymentType=paymentType;
	}
	
	/**
	 * A function to get the name of the guest.
	 * @return {String} the name of the guest
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * A function to get the card detail.
	 * @return {String} the card detail
	 */
	public String getCardDetails() {
		return cardDetails;
	}
	
	/**
	 * A function to print the guest detail.
	 */
	public void printDetails() {
		System.out.println(""
				+ "Name          : " + this.name
				+ "\nCard Details  : " + this.cardDetails 
				+ "\nAddress       : " + this.address
				+ "\nCountry       : " + this.country
				+ "\nGender        : " + this.gender
				+ "\nNationality   : " + this.nationality
				+ "\nContact Number: " + Integer.toString(this.contact)
				+ "\nIdentity      : " + this.identity);
	}
	
	/**
	 * A function to update the guest detail.
	 * @param detailToUpdate {String} guest's detail to update
	 * @param newData {String} the new data to update to
	 * @throws {GuestDetailUpdateFailureException} exception when updating unknown details
	 */
	public void updateDetails(String detailToUpdate, String newData) throws GuestDetailUpdateFailureException {
		Set<String> attrNames = this.getAttributeNames();
		if (attrNames.contains(detailToUpdate)) {
			Field field;
			try {
				field = this.getClass().getDeclaredField(detailToUpdate);
				field.setAccessible(true);
				try {
					field.set(this, newData);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					throw new GuestDetailUpdateFailureException();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw new GuestDetailUpdateFailureException();
				}
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
				throw new GuestDetailUpdateFailureException();
			} catch (SecurityException e) {
				e.printStackTrace();
				throw new GuestDetailUpdateFailureException();
			}
		}
	}
	
	/**
	 * A function to get attribute names of the guest class.
	 * @return {Set<String>} the attribute of the class
	 */
	private Set<String> getAttributeNames() {
		Set<String> fields = new HashSet<String>();
        for (Field field : this.getClass().getDeclaredFields()) {
            fields.add(field.getName().toLowerCase());
        }
        return fields;
	}

	/**
	 * A function to get date when guest will check in
	 * @return {Date} the Date object
	 */
	public Date getstartDate() {
		return startDateOfStay;
	}
	
	/**
	 * A function to get date when guest will check out
	 * @return {Date} the Date object
	 */
	public Date getendDate() {
		return endDateOfStay;
	}
	/**
	 * A function to get identity number of the guest
	 * @return {String} String of identity
	 */
	public String getIdentity() {
		return identity;
	}
	
	/**
	 * A function for guest to make order.
	 * @param menu {Menu} menu of food in the hotel
	 * @param orderMap {Hashtable<String, String>} the order and quantity from the guest 
	 * @throws {FoodNotOnMenuException} when food ordered not on menu
	 */
	public void makeOrder(Menu menu, Hashtable<String, String> orderMap) throws FoodNotOnMenuException {
		RoomService roomService = new RoomService(menu);
		roomService.makeOrder(orderMap);
		this.roomServiceList.add(roomService);
	}
	
	/**
	 * A function for guest to make payment.
	 * @param roomType {String} the room type the guest stayed at
	 * @param roomCost {Double} the room cost the guest stayed at
	 */
	public void makePayment(String roomType, double roomCost) {
		int numberOfDays = (int) ((this.endDateOfStay.getTime()-this.startDateOfStay.getTime())/(1000*60*60*24));
		Payment payment = new Payment(this.paymentType, roomType, roomCost*numberOfDays, 
				this.roomServiceList);
		payment.printReceipt();
	}
	
	/**
	 * A function to get the guest payment type.
	 * @return {String} the payment type
	 */
	public String getPaymentType() {
		return this.paymentType;
	}
}
