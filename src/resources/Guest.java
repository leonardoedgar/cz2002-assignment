package resources;
import java.util.Date;

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
	Date start_date_of_stay;
	Date end_date_of_stay;

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
		return name;
	}
	
	public String getCardDetails() {
		return cardDetails;
	}
	public void makeOrder(Menu menu) {
		//
	}
}
