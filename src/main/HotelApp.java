package main;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.lang.NumberFormatException;
import resources.Hotel;
import resources.Reservation;
import resources.Guest;
import exception.GuestDetailUpdateFailureException;
import exception.GuestNotFoundException;
import exception.ReservationNotFoundException;
import exception.InvalidReservationDetailException;
import exception.AppFailureException;
import exception.DuplicateReservationFoundException;
import exception.InvalidGuestDetailException;

public class HotelApp {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean exitApp = false;
		try {
			Hotel hotel = new Hotel("src/data/roomConfig.txt");
			while (!exitApp) {
				HotelApp.printHotelAppMenu();
				System.out.print("Enter user input: ");
				switch(sc.next()) {
					case "a":
					case "A": HotelApp.showMenuA(hotel); break;
					case "b":
					case "B": HotelApp.showMenuB(hotel); break;
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
		System.out.print(""
				+ "|==================================|\n"
				+ "|(A) Update guest's detail by name |\n"
				+ "|(B) Search guest's detail by name |\n"
				+ "|==================================|\n"
				+ "\nEnter user input: ");
		switch(sc.nextLine().trim()) {
			case "a":
			case "A": {
				System.out.print("Enter guest name      : ");
				String guestName = sc.nextLine().trim();
				System.out.print("Enter detail to update: ");
				String detailToUpdate = sc.nextLine().trim();
				System.out.print("Enter new detail      : ");
				String newDetail = sc.nextLine().trim();
				try {
					hotel.updateGuestDetailsByName(guestName, detailToUpdate, newDetail);
					System.out.println("Guest's details updated successfully!\n");
				}
				catch (GuestNotFoundException | GuestDetailUpdateFailureException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case "b":
			case "B": {
				System.out.println("Enter guest name to display his/her details: ");
				hotel.printGuestDetailsByName(sc.nextLine().trim());
				break;
			}
			default: System.out.println("Invalid input. Retry\n"); 
		}
	}
	
	/**
	 * A function to show menu B.
	 * @param hotel {Hotel} indicates the hotel object
	 */
	public static void showMenuB(Hotel hotel) {
		Scanner sc = new Scanner(System.in);
		System.out.print(""
				+ "|==================================|\n"
				+ "|(A) Create a reservation          |\n"
				+ "|(B) Update a reservation          |\n"
				+ "|(C) Remove a reservation          |\n"
				+ "|(D) Print all reservations        |\n"
				+ "|==================================|\n"
				+ "\nEnter user input: ");
		switch(sc.nextLine().trim()) {
			case "a":
			case "A": {
				try {
					hotel.getReservationSystem().addReservation(
							HotelApp.createNewReservation(hotel.getAvailableRoomTypes()));
					System.out.println("Reservation added successfully!\n");
				} catch (InvalidGuestDetailException | InvalidReservationDetailException | 
						DuplicateReservationFoundException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case "b":
			case "B": {
				try {
					hotel.getReservationSystem().updateReservation(
						HotelApp.createNewReservation(hotel.getAvailableRoomTypes()));
					System.out.println("Reservation updated successfully!\n");
				} catch ( InvalidGuestDetailException | InvalidReservationDetailException | 
						ReservationNotFoundException e) {
					System.out.println(e.getMessage()+ "\n");
				} 
				break;
			}
			case "c":
			case "C": {
				System.out.print("Enter reservation ID to remove: ");
				try {
					hotel.getReservationSystem().removeReservation(sc.nextLine().trim());
					System.out.println("Reservation removed successfully!\n");
				} catch (ReservationNotFoundException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case "d":
			case "D": hotel.getReservationSystem().printReservation(); break;
			default: System.out.println("Invalid input. Retry\n"); 
		}
	}
	
	/**
	 * A function to create a new reservation. 
	 * @param roomTypes {ArrayList<String>} indicates the available room types in the hotel
	 * @return {Reservation} the reservation object.
	 * @throws {InvalidReservationDetailException} exception when reservation detail input is not valid
	 * @throws {InvalidGuestDetailException} exception when guest detail input is not valid 
	 */
	@SuppressWarnings("deprecation")
	public static Reservation createNewReservation(ArrayList<String> roomTypes) throws InvalidReservationDetailException, InvalidGuestDetailException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter reservation ID                 : ");
		String reservationId = sc.nextLine().trim();
		Guest guest = HotelApp.createNewGuest();
		System.out.print("Enter date of check in (MM/DD/YYYY)  : ");
		Date checkInDate = new Date(sc.nextLine().trim());
		System.out.print("Enter date of check out (MM/DD/YYYY) : ");
		Date checkOutDate = new Date(sc.nextLine().trim());
		System.out.print("Enter room type                      : ");
		String roomType = sc.nextLine().trim();
		System.out.print("Enter number of people               : ");
		int numOfPeople = Integer.parseInt(sc.nextLine().trim());
		System.out.print("Enter payment type                   : ");
		String paymentType = sc.nextLine().trim();
		if (checkInDate.compareTo(checkOutDate) >=0 || !roomTypes.contains(roomType)) {
			throw new InvalidReservationDetailException();
		}
		return new Reservation(reservationId, guest, checkInDate, checkOutDate, 
			numOfPeople, paymentType, roomType);
	}
	
	/**
	 * A function to create a new Guest.
	 * @return {Guest} the guest object
	 * @throws {InvalidGuestDetailException} exception when guest detail input is not valid 
	 */
	public static Guest createNewGuest() throws InvalidGuestDetailException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter guest name                     : ");
		String guestName = sc.nextLine().trim();
		System.out.print("Enter card details                   : ");
		String cardDetails = sc.nextLine().trim();
		System.out.print("Enter address                        : ");
		String address = sc.nextLine().trim();
		System.out.print("Enter country                        : ");
		String country = sc.nextLine().trim();
		System.out.print("Enter nationality                    : ");
		String nationality = sc.nextLine().trim();
		System.out.print("Enter gender                         : ");
		String gender = sc.nextLine().trim();
		System.out.print("Enter contact                        : ");
		String contact = sc.nextLine().trim();
		System.out.print("Enter identity                       : ");
		String identity = sc.nextLine().trim();
		try {
			return new Guest(guestName, cardDetails, address, country, 
					gender, nationality, Integer.parseInt(contact), identity);
		}
		catch (NumberFormatException e) {
			throw new InvalidGuestDetailException();
		}
		
	}
}
