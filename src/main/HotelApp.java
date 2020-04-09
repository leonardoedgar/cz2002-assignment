package main;
import java.util.Scanner;
import resources.Hotel;
import resources.Room;
import exception.GuestDetailUpdateFailureException;
import exception.GuestNotFoundException;
import exception.RoomDetailUpdateFailureException;
import exception.RoomNotFoundException;
import exception.AppFailureException;
import resources.Guest;

public class HotelApp {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
//		Guest guest = new Guest("Leonardo", "1", "1", "1", "1", "1", 12345, "1");
		boolean exitApp = false;
		try {
			Hotel hotel = new Hotel("src/data/roomConfig.txt");
			while (!exitApp) {
				HotelApp.printHotelAppMenu();
				System.out.print("Enter user input: ");
				switch(sc.nextLine()) {
					case "a":
					case "A": HotelApp.showMenuA(hotel);break;
					case "b":
					case "B": System.out.println("B"); break;
					case "c":
					case "C": HotelApp.showMenuC(hotel);break;
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
		switch(sc.nextLine()) {
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
	/**
	 * Functional Requirement C.
	 * @param hotel
	 * @throws RoomNotFoundException
	 */
	public static void showMenuC(Hotel hotel) throws RoomNotFoundException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter your room number: ");
		String room_num = sc.nextLine();
		try {
		Room room = hotel.getRoomByNo(room_num);
		room.printRoom();
		System.out.print(""
				+ "Details to be created/updated: \n"
				+ "|=====================|\n"
				+ "|(A) Room status      |\n"
				+ "|(B) Room price       |\n"
				+ "|(C) Bed type         |\n"
				+ "|(D) Wifi Availability|\n"
				+ "|(E) View of the Room |\n"
				+ "|(F) Smoking Allowance|\n"
				+ "|=====================|\n"
				+ "\nEnter user input: ");
		String choice = sc.nextLine();
		System.out.println("choice = "+choice);
		System.out.println("Enter the updated information: ");
		String new_data = sc.nextLine();
		hotel.updateRoomDetails(room_num, choice.charAt(0), new_data);
		System.out.println("Room Information Updated!");
		room.printRoom();}
		catch (RoomNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		
	}
}

