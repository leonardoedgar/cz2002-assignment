package resources;
import java.util.Date;
import java.util.Set;
import java.lang.reflect.Field;
import java.util.HashSet;
import exception.GuestDetailUpdateFailureException;

public class Guest {
	String name;
	String cardDetails;
	String address;
	String country;
	String gender;
	String nationality;
	int contact;
	String identity;
	Payment payment;
	RoomService roomService;
	Date startDateOfStay;
	Date endDateOfStay;

	public Guest(String name, String cardDetails, String address, String country, String gender, String nationality, int contact, String identity) {
		this.name = name;
		this.cardDetails = cardDetails;
		this.address = address;
		this.country = country;
		this.gender = gender;
		this.nationality = nationality;
		this.contact = contact;
		this.identity=identity; 
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCardDetails() {
		return cardDetails;
	}
	public void makeOrder(Menu menu) {
		//
	}
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
	private Set<String> getAttributeNames() {
		Set<String> fields = new HashSet<String>();
        for (Field field : this.getClass().getDeclaredFields()) {
            fields.add(field.getName().toLowerCase());
        }
        return fields;
	}
	
}
