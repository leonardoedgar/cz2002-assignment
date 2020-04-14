package main;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.InputMismatchException;
import resources.Hotel;
import resources.Menu;
import resources.Room;
import exception.GuestDetailUpdateFailureException;
import exception.GuestNotFoundException;
import exception.RoomNotFoundException;
import exception.RoomTypeNotFoundException;
import resources.Reservation;
import resources.Guest;
import exception.ReservationNotFoundException;
import exception.InvalidReservationDetailException;
import exception.AppFailureException;
import exception.DuplicateReservationFoundException;
import exception.FoodNotOnMenuException;
import exception.InvalidGuestDetailException;

public class HotelApp {
	public static Scanner scanner = new Scanner(System.in);
	public static void main(String[] args) {
		boolean exitApp = false;
		try {
			Hotel hotel = new Hotel("src/data/roomConfig.txt");
			Menu menu = new Menu("src/data/menu.txt");
			while (!exitApp) {
				HotelApp.printHotelAppMenu();
				System.out.print("Enter user input: ");
				switch(HotelApp.scanner.nextLine().trim().toLowerCase()) {
					case "a": HotelApp.showMenuA(hotel); break;
					case "b": HotelApp.showMenuB(hotel);; break;
					case "c": HotelApp.showMenuC(hotel);break;
					case "d": HotelApp.showMenuD(hotel, menu);break;
					case "e": HotelApp.showMenuE(menu); break;
					case "f": HotelApp.showMenuF(hotel);break;
					case "g": HotelApp.showMenuG(hotel);break;
					case "h": HotelApp.showMenuH(hotel);break;
					case "i": HotelApp.showMenuI(hotel); break;
					case "q": exitApp = true; break;
					default: System.out.println("Invalid input. Retry\n");
				}
			}
			System.out.println("\nThank you for using the app. Have a good day!");
		}
		catch(AppFailureException e) {
			System.out.println("App has crashed. Err: " + e.getMessage());
		}
		catch (FileNotFoundException e) {
			System.out.println("Err: " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("Err: " + e.getMessage());
		}
		finally {
			scanner.close();
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
				+ "|(A) Update/search guest's detail by name         |\n" 
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
	 */
	public static void showMenuA(Hotel hotel) {
		System.out.print(""
				+ "|==================================|\n"
				+ "|(A) Update guest's detail by name |\n"
				+ "|(B) Search guest's detail by name |\n"
				+ "|==================================|\n"
				+ "\nEnter user input: ");
		switch(HotelApp.scanner.nextLine().trim().toLowerCase()) {
			case "a": {
				System.out.print("Enter guest name      : ");
				String guestName = HotelApp.scanner.nextLine().trim();
				System.out.print("Enter detail to update: ");
				String detailToUpdate = HotelApp.scanner.nextLine().trim();
				System.out.print("Enter new detail      : ");
				String newDetail = HotelApp.scanner.nextLine().trim();
				try {
					hotel.updateGuestDetailsByName(guestName, detailToUpdate, newDetail);
					System.out.println("Guest's details updated successfully!\n");
				}
				catch (GuestNotFoundException | GuestDetailUpdateFailureException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case "b": {
				System.out.println("Enter guest name to display his/her details: ");
				try {
					hotel.printGuestDetailsByName(HotelApp.scanner.nextLine().trim());
				} catch (GuestNotFoundException e) {
					System.out.println(e.getMessage());
				}
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
		System.out.print(""
				+ "|==================================|\n"
				+ "|(A) Create a reservation          |\n"
				+ "|(B) Update a reservation          |\n"
				+ "|(C) Remove a reservation          |\n"
				+ "|(D) Print all reservations        |\n"
				+ "|==================================|\n"
				+ "\nEnter user input: ");
		switch(HotelApp.scanner.nextLine().trim().toLowerCase()) {
			case "a": {
				try {
					Reservation reservation = HotelApp.createNewReservation(
							hotel.getAvailableRoomTypes(), hotel.getCurrentDate());
					hotel.getReservationSystem().addReservation(reservation, 
							hotel.getNumberOfRoomsByRoomType(reservation.getRoomType()));
					if (!hotel.checkRoomAvailability(reservation.getDateOfCheckIn(), 
							reservation.getDateOfCheckOut(), reservation.getRoomType())) {
						reservation.updateStatus("waitlist");
					}
					System.out.println("Reservation added successfully!\n");
					
				} catch (InvalidGuestDetailException | InvalidReservationDetailException | 
						DuplicateReservationFoundException | RoomTypeNotFoundException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case "b": {
				try {
					Reservation reservation = HotelApp.createNewReservation(
							hotel.getAvailableRoomTypes(), hotel.getCurrentDate());
					hotel.getReservationSystem().updateReservation(
						reservation, hotel.getNumberOfRoomsByRoomType(reservation.getRoomType()) );
					System.out.println("Reservation updated successfully!\n");
				} catch ( InvalidGuestDetailException | InvalidReservationDetailException | 
						ReservationNotFoundException | RoomTypeNotFoundException e) {
					System.out.println(e.getMessage()+ "\n");
				} 
				break;
			}
			case "c": {
				System.out.print("Enter reservation ID to remove: ");
				try {
					String reservationId = HotelApp.scanner.nextLine().trim();
					Reservation reservation = hotel.getReservationSystem().getReservation(reservationId);
					hotel.getReservationSystem().removeReservation(reservationId, 
							hotel.getNumberOfRoomsByRoomType(reservation.getRoomType()));
					System.out.println("Reservation removed successfully!\n");
				} catch (ReservationNotFoundException | RoomTypeNotFoundException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case "d": hotel.getReservationSystem().printReservation(); break;
			default: System.out.println("Invalid input."); 
		}
	}
	
	/**
	 * A function to create a new reservation. 
	 * @param roomTypes {ArrayList<String>} indicates the available room types in the hotel
	 * @return {Reservation} the reservation object.
	 * @throws {InvalidReservationDetailException} exception when reservation detail input is not valid
	 * @throws {InvalidGuestDetailException} exception when guest detail input is not valid 
	 */
	public static Reservation createNewReservation(ArrayList<String> roomTypes, Date currentDate) 
			throws InvalidReservationDetailException, InvalidGuestDetailException {
		System.out.print("Enter reservation ID                                : ");
		String reservationId = HotelApp.scanner.nextLine().trim();
		Guest guest = HotelApp.createNewGuest(true, currentDate);
		Date checkInDate=guest.getstartDate();
		Date checkOutDate = guest.getendDate();
		System.out.print("Enter room type                                     : ");
		String roomType = HotelApp.scanner.nextLine().trim();
		System.out.print("Enter number of people                              : ");
		int numOfPeople = Integer.parseInt(HotelApp.scanner.nextLine().trim());
		System.out.print("Enter payment type                                  : ");
		String paymentType = HotelApp.scanner.nextLine().trim();
		if (checkInDate.compareTo(checkOutDate) >=0 || !roomTypes.contains(roomType)) {
			throw new InvalidReservationDetailException();
		}
		return new Reservation(reservationId, guest, checkInDate, checkOutDate, 
			numOfPeople, paymentType, roomType);
	}
	
	/**
	 * A function to create a new Guest.
	 * @param isForReservation {boolean} whether the guest created wants to make a reservation
	 * @param currentDate {Date} the current date in the hotel system
	 * @return {Guest} the guest object
	 * @throws InvalidReservationDetailException  exception when check in date is later than check out date
	 * @throws {InvalidGuestDetailException} exception when guest detail input is not valid 
	 */
	@SuppressWarnings("deprecation")
	public static Guest createNewGuest(boolean isForReservation, Date currentDate) 
			throws InvalidGuestDetailException, InvalidReservationDetailException {
		System.out.print("Enter guest name                                    : ");
		String guestName = HotelApp.scanner.nextLine().trim();
		System.out.print("Enter card details                                  : ");
		String cardDetails = HotelApp.scanner.nextLine().trim();
		System.out.print("Enter address                                       : ");
		String address = HotelApp.scanner.nextLine().trim();
		System.out.print("Enter country                                       : ");
		String country = HotelApp.scanner.nextLine().trim();
		System.out.print("Enter nationality                                   : ");
		String nationality = HotelApp.scanner.nextLine().trim();
		System.out.print("Enter gender                                        : ");
		String gender = HotelApp.scanner.nextLine().trim();
		System.out.print("Enter contact                                       : ");
		String contact = HotelApp.scanner.nextLine().trim();
		System.out.print("Enter identity (driving license or passport number) : ");
		String identity = HotelApp.scanner.nextLine().trim();
		Date startDate= new Date();
		Date endDate= new Date();
		try {
			if (isForReservation) {
				System.out.print("Enter date of check-in (MM/DD/YYYY)                 : ");
				startDate=new Date(HotelApp.scanner.nextLine().trim());
				if (startDate.compareTo(currentDate) < 0) {
					throw new InvalidReservationDetailException(""
							+ "Reservation start date cannot be on a day in the past.");
				}
			}
			else {
				startDate = currentDate;
			}
			System.out.print("Enter date of check-out (MM/DD/YYYY)                : ");
			endDate=new Date(HotelApp.scanner.nextLine().trim());	
			if (startDate.compareTo(endDate) >= 0) {
				throw new InvalidReservationDetailException("Check out date "
						+ "cannot be before the check in date.");
			}
		
			return new Guest(guestName, cardDetails, address, country, 
					gender, nationality, Integer.parseInt(contact), identity,startDate,endDate);
		}
		catch (IllegalArgumentException e) {
			throw new InvalidGuestDetailException();
		}
	}
	/**
	 * Functional Requirement C.
	 * @param hotel
	 * @throws RoomNotFoundException
	 */
	public static void showMenuC(Hotel hotel) throws RoomNotFoundException {
		System.out.println("Enter your room number: ");
		String roomNum = HotelApp.scanner.nextLine();
		try {
			boolean isUserInputValid = false;
			Room room = hotel.getRoomByNo(roomNum);
			room.printRoom();
			System.out.print(""
					+ "Details to be created/updated: \n"
					+ "|===============================|\n"
					+ "|(A) Room status                |\n"
					+ "|(B) Room price                 |\n"
					+ "|(C) Bed type                   |\n"
					+ "|(D) Wifi Availability          |\n"
					+ "|(E) View of the Room           |\n"
					+ "|(F) Smoking Allowance (yes/no) |\n"
					+ "|===============================|\n"
					+ "\nEnter user input: ");
			String userInput = HotelApp.scanner.nextLine().toLowerCase();
			String detailToUpdate="";
			while (!isUserInputValid) {
				isUserInputValid = true;
				switch(userInput) {
					case "a": detailToUpdate = "status"; break;
					case "b": detailToUpdate = "price"; break;
					case "c": detailToUpdate = "bed type"; break;
					case "d": detailToUpdate = "wifi"; break;
					case "e": detailToUpdate = "view"; break;
					case "f": detailToUpdate = "smoking"; break;
					default: {
						System.out.println("Invalid choice! Please try again."); 
						isUserInputValid=false;
					}
				}
			}
			if (detailToUpdate.equals("status")) {
				System.out.print(""
						+ "New room status: \n"
						+ "|======================|\n"
						+ "|(A) Vacant            |\n"
						+ "|(B) Under Maintainance|\n"
						+ "|======================|\n"
						+ "\nEnter user input: ");
				userInput = HotelApp.scanner.nextLine().trim().toLowerCase();
				isUserInputValid = false;
				while (!isUserInputValid) {
					switch(userInput) {
						case "a": {
							hotel.updateRoomDetails(roomNum, detailToUpdate, "vacant");
							System.out.println("Room Information Updated!");
							isUserInputValid = true;
							break;
						}
						case "b": {
							hotel.updateRoomDetails(roomNum, detailToUpdate, "under maintainance");
							System.out.println("Room Information Updated!");
							isUserInputValid = true;
							break;
						}
						default: System.out.println("Invalid choice, please try again");
					}
				}
				
			}
			else{
				System.out.println("Enter the updated information: ");
				String newDetail = HotelApp.scanner.nextLine().trim();
				hotel.updateRoomDetails(roomNum, detailToUpdate, newDetail);
				System.out.println("Room Information Updated!");
			}
		}
		catch (RoomNotFoundException | RoomTypeNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * A function show hotel app menu D.
	 * @param hotel {Hotel} the hotel object
	 * @param menu {Menu} the menu object
	 * @throws RoomNotFoundException 
	 * @throws FoodNotOnMenuException 
	 */
	public static void showMenuD(Hotel hotel, Menu menu) {
			Hashtable<String, String> orderMap = new Hashtable<String, String>();
			boolean orderMore = true, orderSuccess = true;
			menu.printItems();
			System.out.print("Enter room number: ");
			String roomNo = HotelApp.scanner.nextLine().trim();
			while (orderMore) {
				System.out.print("Enter food name: ");
				String foodName = HotelApp.scanner.nextLine().trim();
				System.out.print("Enter quantity to order: ");
				try {
					String quantity = Integer.toString(HotelApp.scanner.nextInt());
					HotelApp.scanner.nextLine();
					if (orderMap.containsKey(foodName)) {
						String newQuantity = Integer.toString(
								Integer.parseInt(orderMap.get(foodName)) + Integer.parseInt(quantity));
						orderMap.replace(foodName, newQuantity);
					}
					else {
						orderMap.put(foodName, quantity);
					}
					System.out.println("current order: " + orderMap.toString());
					System.out.print("Order more (yes/no): ");
					orderMore = HotelApp.scanner.nextLine().trim().equalsIgnoreCase("yes");
				}
				catch(InputMismatchException e) {
					System.out.println("Invalid input. Re-entry the last order to reorder.");
					orderSuccess = false;
				}
			}
			if (orderSuccess) {
				try {
					hotel.makeRoomServiceOrder(roomNo, menu, orderMap);
					System.out.println("Order successful!");
				} catch (FoodNotOnMenuException | RoomNotFoundException | GuestNotFoundException e ) {
					System.out.println(e.getMessage());
				} 
			}
	}
	public static void showMenuE(Menu menu) {
		String foodname;
		double price;
		System.out.println("Here is the current menu.");
		menu.printItems();
		System.out.println("Would you like to:\n"
				+ "|=================================|\n"
				+ "|(A) Add Items in the Menu        |\n"
				+ "|(B) Remove Items from the Menu   |\n"
				+ "|(C) Update Items from the Menu   |\n"
				+ "|(D) Print the Menu               |\n"
				+ "|=================================|"
				+ "\nYour choice: ");
		switch(HotelApp.scanner.nextLine().trim().toLowerCase()) {
		case "a": {
			System.out.println("Enter the food name:");
			foodname = HotelApp.scanner.nextLine().trim();
			System.out.println("Enter the price of the food:");
			price = HotelApp.scanner.nextDouble();
			menu.addItems(foodname, price);
			break;
			}
		case "b": {
			System.out.println("Enter the food name:");
			foodname = HotelApp.scanner.nextLine().trim();
			menu.removeItems(foodname);
			break;
			}
		case "c": {
			System.out.println("Enter the food name:");
			foodname = HotelApp.scanner.nextLine().trim();
			System.out.println("Enter the price of the food:");
			price = HotelApp.scanner.nextDouble();
			menu.updateItems(foodname, price);
			break;
			}
		case "d": {
			menu.printItems();
			break;
			}
		default: System.out.println("Invalid input.");
		}
	}
	/**
	 * A function to show the functional requirements f
	 * @param hotel
	 */
	@SuppressWarnings("deprecation")
	public static void showMenuF(Hotel hotel) {
		Date startDate = new Date();
		Date endDate = new Date();
		System.out.print("Enter start date in the following format (MM/DD/YYYY): ");
		try {
			startDate= new Date(HotelApp.scanner.nextLine().trim());
			System.out.println("Enter end date in the following format (MM/DD/YYYY): ");
			boolean isUserInputValid = false;
			while(isUserInputValid) {
				endDate= new Date(HotelApp.scanner.nextLine().trim());
				if(endDate.compareTo(startDate)>0) {
					isUserInputValid = true;
				}
				else {
					System.out.println("End date must be after start date, enter another end date:");
				}
			}
			System.out.println("Enter room type: ");
			String roomType = HotelApp.scanner.nextLine().trim();
			boolean roomTypeExists = false;
			while(!roomTypeExists) {
				roomTypeExists = hotel.doesRoomTypeExists(roomType);
				if(!roomTypeExists) {
					System.out.println("Invalid room type. Try again: ");
					roomType = HotelApp.scanner.nextLine().trim();
				}
			}
				
			if(hotel.checkRoomAvailability(startDate, endDate, roomType)) {
				System.out.println("Room type is available.");
			}
			else {
				System.out.println("Room type is not available.");
			}		
		} catch(RoomTypeNotFoundException e) {
			System.out.println(e.getMessage());
		}
}
	
	/**
	 * A function to show the functional requirements of G
	 * @param hotel
	 */
	public static void showMenuG(Hotel hotel){
		//does guest have a reservation
		boolean hasReservation = false;
		boolean inputValid = false;
		//input checking, only allows user to enter y or n
		while(!inputValid) {
			System.out.print("Does the guest have a reservation (yes/no)? ");
			switch(HotelApp.scanner.nextLine().trim().toLowerCase()) {
				case "yes": hasReservation = inputValid = true; break;
				case "no": hasReservation = false; inputValid = true; break;
				default: {
					System.out.println("Invalid input. Retry");
				}
			}
		}
		if (hasReservation) {
			//check guest details from reservation system and assign to room
			System.out.print("Enter reservation id: ");
			String reservationId = HotelApp.scanner.nextLine().trim();
			Reservation tempreservation = hotel.getReservationSystem().getReservation(reservationId);
			if(tempreservation != null) {
				String roomType = tempreservation.getRoomType();
				Guest tempGuest = tempreservation.getGuest();
				boolean success = false;
				System.out.print("Enter the room number guest will be assigned to: ");
				String roomNo = HotelApp.scanner.nextLine().trim();
				try{
					success=hotel.checkIn(tempGuest, roomNo, roomType,tempreservation);
					if(success) {
						System.out.println("Check-in successful!");
					}
					else {
						System.out.println("Room is currently unavailable. Choose another room.");
					}
				} catch(RoomNotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
			else {
				System.out.println("Reservation Id is invalid.");
			}
		}
		else {
			//create new guest object and assign to room
			try {
				Guest newGuest = HotelApp.createNewGuest(false, hotel.getCurrentDate());				
				System.out.print("Enter guest's preferred room type                   : ");
				String roomType = HotelApp.scanner.nextLine().trim();
				boolean roomTypeExists = false;
				while(!roomTypeExists) {
					roomTypeExists = hotel.doesRoomTypeExists(roomType);
					if(!roomTypeExists) {
						System.out.println("Invalid room type. Try again:");
						roomType = HotelApp.scanner.nextLine().trim();
					}
				}
				boolean roomNoAvailable = false;
				if(hotel.checkRoomAvailability(newGuest.getstartDate(), newGuest.getendDate(), 
						roomType)) {
					System.out.print("Enter the room number to assign to                  : ");
					String roomNo = HotelApp.scanner.nextLine().trim();
					try{
						roomNoAvailable = hotel.checkIn(newGuest, roomNo, roomType);
						if(roomNoAvailable) {
							System.out.println("Check-in successful!");
						}
						else {
							System.out.println("Room is currently unavailable. Choose another room.");
						}
					} catch (RoomNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}
				else {
					System.out.println("Room type is currently fully booked. "
							+ "Guest is not assigned to a room and the details will be deleted.");
				}
				
			} catch(InvalidGuestDetailException |RoomTypeNotFoundException | 
					InvalidReservationDetailException e) {
					System.out.println(e.getMessage());
			}
		}
	}

	public static void showMenuH(Hotel hotel) {
		System.out.print("Enter room number to check out: ");
		String roomNo = HotelApp.scanner.nextLine().trim();
		System.out.print("Enter guest full name for verification: ");
		String guestName = HotelApp.scanner.nextLine().trim();
		try {
			boolean success = hotel.checkOut(roomNo, guestName);
			if (!success) {
				System.out.println("Check out failed. Identity is not verified.");
			}
		} catch (RoomNotFoundException | GuestNotFoundException e) {
			System.out.println(e.getMessage());
		} 
	}

	/**
	 * Show the functional i
	 * @param hotel
	 */
	public static void showMenuI(Hotel hotel) {
		System.out.print("Print room statistics by:\n"+
						   "|============================|\n"+
						   "|(A) Room type occupancy rate|\n"+
						   "|(B) Room status             |\n"+
						   "|============================|\n"+
						   "Enter your choice: ");
		try {
			switch(HotelApp.scanner.nextLine().trim().toLowerCase()) {
				case "a": {
					ArrayList<String> roomTypeList = hotel.getAvailableRoomTypes();
					for (String roomType: roomTypeList) {
						ArrayList<String> vacantRoomNoList = 
								hotel.getRoomTypeToVacantRoomNoListTable(true).get(roomType);
						System.out.println(Character.toUpperCase(roomType.charAt(0)) + 
								roomType.substring(1) + ": ");
						System.out.println("\tNumber: " + 
								vacantRoomNoList.size() + " out of " 
								+ hotel.getNumberOfRoomsByRoomType(roomType));
						String vacantRoomListString = vacantRoomNoList.toString();
						System.out.println("\tRooms: " + vacantRoomListString.substring(1, 
								vacantRoomListString.length()-1));
						}
					break;
				}
				case "b": {
					Hashtable<String, ArrayList<String>> roomStatusToRoomNoListTable = 
							hotel.getRoomStatusToRoomNoListTable(true);
					for (String roomStatus: roomStatusToRoomNoListTable.keySet()) {
						System.out.println(Character.toUpperCase(roomStatus.charAt(0)) + 
								roomStatus.substring(1) + " :");
						String roomListString = roomStatusToRoomNoListTable.get(roomStatus).toString();
						System.out.println(roomListString.substring(1, roomListString.length()-1));
					}
					break;
				}
				default: System.out.println("Inavlid input.");
			}
		} catch(RoomTypeNotFoundException e){
			System.out.println(e.getMessage());
		}
	}
}
