package resources;
import java.util.Date;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.ArrayList;
import exception.FoodNotOnMenuException;
import exception.GuestDetailUpdateFailureException;
import exception.GuestNotFoundException;
import exception.OrderIdNotFoundException;
import resources.Payment;

/**
 * A class to represent a guest in a hotel.
 */
public class Guest {
	private String name;
	private String cardDetails;
	private String address;
	private String country;
	private String nationality;
	private String gender;
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
			String nationality, int contact, String identity, String paymentType, 
			Date startDateOfStay, Date endDateOfStay) {
		this.name = name;
		this.cardDetails = cardDetails;
		this.address = address;
		this.country = country;
		this.gender = gender;
		this.nationality = nationality;
		this.contact = contact;
		this.identity = identity; 
		this.roomServiceList = new ArrayList<RoomService>();
		this.paymentType = paymentType;
		this.startDateOfStay = startDateOfStay;
		this.endDateOfStay = endDateOfStay;
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
		return this.cardDetails;
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
		Hashtable<String, String> attrNames = this.getAttributeNames();
		detailToUpdate = detailToUpdate.toLowerCase().replaceAll(" ", "");
		if (attrNames.containsKey(detailToUpdate)) {
			Field field;
			try {
				field = this.getClass().getDeclaredField(attrNames.get(detailToUpdate));
				field.setAccessible(true);
				try {
					if (field.getType() == int.class) {
						field.set(this, Integer.parseInt(newData));
					}
					else {
						field.set(this, newData);
					}
				} catch (IllegalArgumentException e) {
					throw new GuestDetailUpdateFailureException();
				} catch (IllegalAccessException e) {
					throw new GuestDetailUpdateFailureException();
				}
			} catch (NoSuchFieldException e) {
				throw new GuestDetailUpdateFailureException();
			} catch (SecurityException e) {
				throw new GuestDetailUpdateFailureException();
			}
		}
		else {
			throw new GuestDetailUpdateFailureException("Detail to update is not found.");
		}
	}
	
	/**
	 * A function to get attribute names of the guest class.
	 * @return {Hashtable<String, String>} the attribute of the class hashtable
	 */
	private Hashtable<String, String> getAttributeNames() {
		Hashtable<String, String> fields= new Hashtable<String, String>();
        for (Field field : this.getClass().getDeclaredFields()) {
        	if (field.getName().equals("contact")) {
        		fields.put("contactnumber", field.getName());
        	}
        	else {
        		fields.put(field.getName().toLowerCase(), field.getName());
        	}
        }
        return fields;
	}

	/**
	 * A function to get date when guest will check in
	 * @return {Date} the Date object
	 */
	public Date getStartDateOfStay() {
		return this.startDateOfStay;
	}
	
	/**
	 * A function to get date when guest will check out
	 * @return {Date} the Date object
	 */
	public Date getEndDateOfStay() {
		return this.endDateOfStay;
	}
	/**
	 * A function to get identity number of the guest
	 * @return {String} String of identity
	 */
	public String getIdentity() {
		return this.identity;
	}
	
	/**
	 * A function for guest to make order.
	 * @param menu {Menu} menu of food in the hotel
	 * @param orderMap {Hashtable<String, String>} the order and quantity from the guest 
	 * @throws {FoodNotOnMenuException} when food ordered not on menu
	 */
	public void makeOrder(Menu menu, Hashtable<String, String> orderMap, int orderId) throws FoodNotOnMenuException {
		RoomService roomService = new RoomService(menu, orderId);
		roomService.makeOrder(orderMap);
		this.roomServiceList.add(roomService);
	}
	
	/**
	 * A function for guest to make payment.
	 * @param roomType {String} the room type the guest stayed at
	 * @param roomCost {Double} the room cost the guest stayed at
	 */
	public void makePayment(String roomType, double roomCost) {
		@SuppressWarnings("deprecation")
		int numberOfDays = (int) ((new Date(this.endDateOfStay.toLocaleString().split(",")[0]).getTime() 
				- new Date(this.startDateOfStay.toLocaleString().split(",")[0]).getTime())/(1000*60*60*24));
		Payment payment = new Payment(this.paymentType, roomType, roomCost*numberOfDays, 
				this.roomServiceList);
		payment.printReceipt();
	}

	/**
	 * A function to get the address of the guest.
	 * @return {String} the address of the guest
	 */
	public String getAddress() {
		return this.address;
	}
	
	/**
	 * A function to get the country where the guest comes from.
	 * @return {String} the country
	 */
	public String getCountry() {
		return this.country;
	}
	
	/**
	 * A function to get the nationality of the guest.
	 * @return {String} the nationality
	 */
	public String getNationality() {
		return this.nationality;
	}
	
	/**
	 * A function to get the gender of the guest.
	 * @return {String} the gender
	 */
	public String getGender() {
		return this.gender;
	}
	
	/**
	 * A function to get the contact number of the guest.
	 * @return {int} the contact number
	 */
	public int getContact() {
		return this.contact;
	}
	
	/**
	 * A function to get the payment type of the guest.
	 * @return {String} the payment type
	 */
	public String getPaymentType() {
		return this.paymentType;
	}

	/**
	 * get the room service object by orderId
	 * @param orderId
	 * @return
	 * @throws OrderIdNotFoundException
	 */
	public RoomService getRoomServiceByOrderId(int orderId) throws OrderIdNotFoundException {
		for(RoomService rs: this.roomServiceList) {
			if (rs.getOrderId() == orderId) {
				return rs;
			}
		}
		throw new OrderIdNotFoundException();
	}
	
	/**
	 * To print the roomservicelist per guest
	 */
	public void printRoomServiceList() throws GuestNotFoundException {
		try {
			System.out.println("The list of orders are as follows:");
			for(RoomService rs: this.roomServiceList) {
				System.out.println("-------------------------------------");
				System.out.println("Order ID: "+rs.getOrderId() +"\n"+
							"Order Status: "+ rs.getStatus()+"\n"+ 
							"List of Orders: "+ rs.getOrderMap());
				System.out.println("-------------------------------------");
			}
		}
		catch(NullPointerException e){
			throw new GuestNotFoundException();
		}
	}
	
	/**
	 * A function to check if 2 guests object are identical.
	 * @param firstGuest {Guest} the first guest object
	 * @param secondGuest {Guest} the second guest object
	 * @return {boolean} whether the two guests objects are identical
	 */
	public static boolean isIdentical(Guest firstGuest, Guest secondGuest) {
		return (firstGuest.getName().equals(secondGuest.getName()) && 
				firstGuest.getCardDetails().equals(secondGuest.getCardDetails()) &&
				firstGuest.getAddress().equals(secondGuest.getAddress()) && 
				firstGuest.getCountry().equals(secondGuest.getCountry()) &&
				firstGuest.getNationality().equals(secondGuest.getNationality()) &&
				firstGuest.getGender().equals(secondGuest.getGender()) &&
				firstGuest.getContact() == secondGuest.getContact() &&
				firstGuest.getIdentity().equals(secondGuest.getIdentity()) &&
				firstGuest.getPaymentType().equals(secondGuest.getPaymentType()) &&
				firstGuest.getStartDateOfStay().compareTo(secondGuest.getStartDateOfStay()) == 0 && 
				firstGuest.getEndDateOfStay().compareTo(secondGuest.getEndDateOfStay()) == 0);
	}
}
