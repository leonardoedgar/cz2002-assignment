package main;
import java.util.Scanner;
import resources.Hotel;
import exception.GuestDetailUpdateFailureException;
import exception.GuestNotFoundException;
import exception.AppFailureException;
import resources.Guest;

public class HotelApp {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
//		Guest guest = new Guest("Leonardo", "1", "1", "1", "1", "1", 12345, "1");
		boolean exitApp = false;
		try {
			Hotel hotel = new Hotel("src/data/roomConfig.txt");
			HotelApp.printHotelAppMenu();
			while (!exitApp) {
				System.out.print("Enter user input: ");
				switch(sc.next()) {
					case "a":
					case "A": HotelApp.showMenuA(hotel);break;
					case "b":
					case "B": System.out.println("B"); break;
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
}

