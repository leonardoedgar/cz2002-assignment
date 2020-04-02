package project;

public class Guest {
	String name;
	String cardDetails;
	String address;
	String country;
	String gender;
	String nationality;
	int contact;
	String identity;
	double total_cost;
	
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
	
	
}
