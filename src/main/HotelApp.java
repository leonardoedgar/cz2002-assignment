package main;
import java.util.Scanner;
import resources.Hotel;
import exception.GuestDetailUpdateFailureException;
import exception.GuestNotFoundException;
import exception.AppFailureException;
import resources.Guest;


//added import
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class HotelApp {
	
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		// added date time for creating test objects
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		Date sdate= new Date();
		Date edate= new Date();
		
		try {
			 sdate=df.parse("05-10-2000");
			 edate=df.parse("07-10-2000");
		}catch(Exception e) {
			System.out.println("Unable to parse");
		}
		
		Guest guest = new Guest("Leonardo", "1", "1", "1", "1", "1", 12345, "1",sdate,edate);

		System.out.println(edate);
		System.out.println(sdate);
		
		boolean exitApp = false;
		try {
			Hotel hotel = new Hotel("src/data/roomConfig.txt");
			HotelApp.printHotelAppMenu();
			
			//added temporarily for testing, call addToGuestList whenever a new guest is added to the system
			hotel.addToGuestList(guest,"single");
			
			
			
			while (!exitApp) {
				System.out.print("Enter user input: ");
				switch(sc.next()) {
					case "a":
					case "A": HotelApp.showMenuA(hotel);break;
					case "b":
					case "B": System.out.println("B"); break;
					case "f":
					case "F": HotelApp.showMenuFG(hotel);break;
					case "q":
					case "Q": exitApp = true; break;
					default: System.out.println("Invalid input. Retry\n");
				}
			}
			System.out.println("\nThank you for using the app. Have a good day!");
		}
		catch(AppFailureException e) {
			System.out.println("App has crashed. Err: " + e.getMessage());
		}
		finally {
			sc.close();
		}
	}
	/**
	 * A function to print hotelApp Menu.
	 */
	public static void printHotelAppMenu() {
		System.out.println(""
				+ "|=================================================|\n"
				+ "|                   HOTEL APP                     |\n"
				+ "|=================================================|\n"
				+ "|(A) Create/update/search guest's detail by name  |\n" 
				+ "|(B) Create/Update/Remove/Print reservation       |\n"
				+ "|(C) Create/update room's details                 |\n" 
				+ "|(D) Enter room service order                     |\n"
				+ "|(E) Create/update/remove room service menu items |\n"
				+ "|(F) Check room availability                      |\n"
				+ "|(G) Room check-in (for walk-in or reservation)   |\n"
				+ "|(H) Room check-out and print bill invoice (with  |\n"
				+ "     breakdowns on days of stay, room service     |\n"
				+ "     order items and its total, tax and total     |\n"
				+ "     amount)                                      |\n"
				+ "|(I) Print room status statistic report           |\n"
				+ "|(Q) Quit the app                                 |\n"
				+ "|=================================================|\n");
	}
	/**
	 * A function to show menu A.
	 * @param hotel {Hotel} indicates the hotel object
	 * @throws {GuestNotFoundException} exception when searching unknown guests
	 */
	public static void showMenuA(Hotel hotel) throws GuestNotFoundException {
		
		Scanner sc = new Scanner(System.in);
		System.out.println(""
				+ "|==================================|\n"
				+ "|(A) Update guest's detail by name |\n"
				+ "|(B) Search guest's detail by name |\n"
				+ "|==================================|\n"
				+ "\nEnter user input: ");
		switch(sc.next()) {
			case "a":
			case "A": {
				System.out.println("Enter guest name, detail to update, new detail (respectively): ");
				try {
					hotel.updateGuestDetailsByName(sc.next(), sc.next(), sc.next());
				}
				catch (GuestNotFoundException | GuestDetailUpdateFailureException e) {
					System.out.println(e.getMessage());
				}
				break;
			}
			case "b":
			case "B": {
				System.out.println("Enter guest name to display his/her details: ");
				hotel.printGuestDetailsByName(sc.next());
				break;
			}
			
			default: System.out.println("Invalid input. Retry\n");
		}
		sc.close();
	}
	
	
	//added this part
	/**
	 * A function to show functional requirements f and g
	 * @param hotel
	 */
	public static void showMenuFG(Hotel hotel) {

		Scanner sc = new Scanner(System.in);
		//remove the print once you combine with the rest
		System.out.println("|(F) Check room availability	|\n"
						 + "|(G) Room check-in (for walk-in or reservation)	|\n");
		
		switch(sc.next().trim()) {
			case "f":
			case "F":{
			
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				
				System.out.println("Enter start date in the following format: dd-MM-yyyy");
				Date startDate = new Date();
				try {
			
					startDate= df.parse(sc.next().trim());
				}catch(ParseException e) {
					e.printStackTrace();
				}
				
				System.out.println("Enter end date in the following format: dd-MM-yyyy");
				int checker=1;

				Date endDate = new Date();
				while(checker==1) {
				try {
			
					endDate= df.parse(sc.next().trim());
				}catch(ParseException e) {
					e.printStackTrace();
				}
				if(endDate.compareTo(startDate)>0) {
					checker=0;
				}
				else {
					System.out.println("End date must be after startDate, enter another end date:");
				}
				}
				
				System.out.println("Enter roomType:");
				String roomType=sc.next().trim();
				
				if(hotel.checkRoomAvailability(startDate, endDate, roomType)==true) {
					System.out.println("Room type is available.");
				}
				else {
					System.out.println("Room type is not available.");
				}
				break;
			}
			case "g":
			case "G":{
				System.out.println("Enter guest identity");
				Guest guest = null;
				try {
					guest = hotel.getGuestByIdentity(sc.next().trim());
				}catch(GuestNotFoundException e) {
					System.out.println(e.getMessage());
					break;
				}
				System.out.println("Guest's selected room type is "+hotel.getGuestRoomType(guest)+" room.");
				
				//allows multiple tries for entering room number
				boolean validEntry=false;
				while(validEntry==false) {
					
					System.out.println("Enter the room number you want to assign guest to:");
					String roomNo=sc.next().trim();
					
					
					validEntry=hotel.checkIn(guest,roomNo);
				}
			}
			break;
			
			default:
				break;
			}
}
}



