package main;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.InputMismatchException;
import resources.Hotel;
import resources.Menu;
import resources.Room;
import resources.RoomService;
import exception.GuestDetailUpdateFailureException;
import exception.GuestNotFoundException;
import exception.IdGenerationFailedException;
import exception.RoomNotFoundException;
import exception.RoomTypeNotFoundException;
import resources.Reservation;
import resources.Guest;
import exception.ReservationNotFoundException;
import exception.InvalidReservationDetailException;
import exception.OrderIdNotFoundException;
import exception.AppFailureException;
import exception.DuplicateReservationFoundException;
import exception.FoodNotOnMenuException;
import exception.InvalidGuestDetailException;
import exception.InvalidHotelTimeException;

public class HotelApp {
	public static Scanner scanner = new Scanner(System.in);
	public static Date currentTime = new Date();
	public static double setupTimeDelayInSeconds = 1;
	public static Date prevTime = new Date();
	public static boolean isSaveState = true;
	
	public static void main(String[] args) {
		boolean exitApp = false;
		try {
			Hotel hotel = HotelApp.loadStateHotel();
			Menu menu = HotelApp.loadStateMenu();
			if(hotel == null|| menu == null) {
				 hotel = new Hotel("src/data/roomConfig.txt", HotelApp.setupTimeDelayInSeconds);
				 menu = new Menu("src/data/menu.txt");
			}
			while (!exitApp) {
				HotelApp.currentTime = new Date(HotelApp.currentTime.getTime()+
						new Date().getTime()- HotelApp.prevTime.getTime());
				HotelApp.prevTime = new Date();
				hotel.updateReservationStatusByDate(HotelApp.currentTime);
				hotel.kickOutGuestWhoPastCheckOutTime();
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
					case "j": HotelApp.showMenuJ(); break;
					case "q": exitApp = true; break;
					default: System.out.println("Invalid input. Retry\n");
				}
			}
			HotelApp.saveState(hotel, menu);
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
				+ "|    breakdowns on days of stay, room service     |\n"
				+ "|    order items and its total, tax and total     |\n"
				+ "|    amount)                                      |\n"
				+ "|(I) Print room status statistic report           |\n"
				+ "|(J) Fast forward hotel time                      |\n"
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
				String detailToUpdate = "";
				while(detailToUpdate.equals("")) {
					System.out.print(	""
							+ "|====================================|\n"
							+ "|(A) Update guest's name             |\n"
							+ "|(B) Update guest's card details     |\n"
							+ "|(C) Update guest's address          |\n"
							+ "|(D) Update guest's country          |\n"
							+ "|(E) Update guest's nationality      |\n"
							+ "|(F) Update guest's gender           |\n"
							+ "|(G) Update guest's contact number   |\n"
							+ "|(H) Update guest's identity         |\n"
							+ "|(I) Update guest's payment type     |\n"
							+ "|====================================|\n"
							+"Enter detail to update: ");
					switch(HotelApp.scanner.nextLine().trim().toLowerCase()) {
						case "a": detailToUpdate = "name"; break;
						case "b": detailToUpdate = "card details"; break;
						case "c": detailToUpdate = "address"; break;
						case "d": detailToUpdate = "country"; break;
						case "e": detailToUpdate = "nationality"; break;
						case "f": detailToUpdate = "gender"; break;
						case "g": detailToUpdate = "contact number"; break;
						case "h": detailToUpdate = "identity"; break;
						case "i": detailToUpdate = "payment type"; break;
						default: System.out.println("Invalid entry, try again");
					}
				}
				System.out.print("Enter new " + detailToUpdate + "      : ");
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
				System.out.print("Enter guest name to display his/her details: ");
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
							hotel.getReservationSystem().generateNewId(), 
							hotel.getAvailableRoomTypes(), hotel.getCheckInDate(),
							hotel.getCheckOutTimeInMilliSeconds());
					if (!hotel.checkRoomAvailability(reservation.getDateOfCheckIn(), 
							reservation.getDateOfCheckOut(), reservation.getRoomType())) {
						reservation.updateStatus("waitlist");
					}
					hotel.getReservationSystem().addReservation(reservation, 
							hotel.getNumberOfRoomsByRoomType(reservation.getRoomType()));
					System.out.println("Reservation added successfully!\n");
					System.out.println("Reservation details are as follows:");
					reservation.printDetails();
					
				} catch (InvalidGuestDetailException | InvalidReservationDetailException | 
						DuplicateReservationFoundException | RoomTypeNotFoundException | IdGenerationFailedException e) {
					System.out.println(e.getMessage() + "\n");
				}
				break;
			}
			case "b": {
				try {
					Reservation reservation = HotelApp.createNewReservation(
							hotel.getReservationSystem().generateNewId(), 
							hotel.getAvailableRoomTypes(), hotel.getCurrentDate(),
							hotel.getCheckOutTimeInMilliSeconds());
					hotel.getReservationSystem().updateReservation(
						reservation, hotel.getNumberOfRoomsByRoomType(reservation.getRoomType()) );
					System.out.println("Reservation updated successfully!\n");
					System.out.println("Reservation details are as follows:");
					reservation.printDetails();
				} catch ( InvalidGuestDetailException | InvalidReservationDetailException | 
						ReservationNotFoundException | RoomTypeNotFoundException | IdGenerationFailedException e) {
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
	public static Reservation createNewReservation(String reservationId, 
			ArrayList<String> roomTypes, Date currentDate, int checkOutTime) 
			throws InvalidReservationDetailException, InvalidGuestDetailException {
    try {
		Guest guest = HotelApp.createNewGuest(true, currentDate, checkOutTime);
		Date checkInDate = guest.getStartDateOfStay();
		Date checkOutDate = guest.getEndDateOfStay();
		System.out.print("Enter room type                                     : ");
		String roomType = HotelApp.scanner.nextLine().trim();
		System.out.print("Enter number of people                              : ");
		int numOfPeople;
		try {
			numOfPeople = HotelApp.scanner.nextInt();
			HotelApp.scanner.nextLine();
			if (numOfPeople < 1) {
				throw new InvalidReservationDetailException();
			}
		}
		catch (InputMismatchException e) {
			HotelApp.scanner.nextLine();
			throw new InvalidReservationDetailException();
		}
		String paymentType = guest.getPaymentType();
		if (checkInDate.compareTo(checkOutDate) >=0 || !roomTypes.contains(roomType)) {
			throw new InvalidReservationDetailException();
		}
		return new Reservation(reservationId, guest, checkInDate, checkOutDate, 
			numOfPeople, paymentType, roomType);
		}catch(NumberFormatException e) {
			throw new InvalidReservationDetailException();
		}
	}
	/**
	 * A function to create a new Guest.
	 * @param isForReservation {boolean} whether the guest created wants to make a reservation
	 * @param checkInDate {Date} the hotel check in date and time
	 * @param checkOutTime {int} the hotel check out time in milliseconds
	 * @return {Guest} the guest object
	 * @throws InvalidReservationDetailException  exception when check in date is later than check out date
	 * @throws {InvalidGuestDetailException} exception when guest detail input is not valid 
	 */
	@SuppressWarnings("deprecation")
	public static Guest createNewGuest(boolean isForReservation, Date checkInDate, int checkOutTime) 
			throws InvalidGuestDetailException, InvalidReservationDetailException {
		try {
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
		  System.out.print("Enter payment type                                  : ");
		  String paymentType = HotelApp.scanner.nextLine().trim();
		  Date startDate;
			if (isForReservation) {
				System.out.print("Enter date of check-in (MM/DD/YYYY)                 : ");
				startDate = new Date(HotelApp.scanner.nextLine().trim());
				if (startDate.compareTo(new Date(checkInDate.toLocaleString().split(",")[0])) == 0) {
					if (HotelApp.currentTime.getTime() > checkInDate.getTime()) {
						throw new InvalidReservationDetailException("Reservation start date and time "
								+ "cannot be after hotel check in time");
					}
					startDate = checkInDate;
				}
				else if (startDate.compareTo(new Date (
						HotelApp.currentTime.toLocaleString().split(",")[0])) < 0) {
					throw new InvalidReservationDetailException("Cannot reserve from days in the past.");
				}
				else {
					startDate = new Date(checkInDate.getTime() + startDate.getTime() - new Date (
							checkInDate.toLocaleString().split(",")[0]).getTime());
				}
			}
			else {
				startDate = checkInDate;
			}
			System.out.print("Enter date of check-out (MM/DD/YYYY)                : ");
			Date endDate = new Date(new Date(HotelApp.scanner.nextLine().trim()).getTime() + checkOutTime);	
			if (startDate.compareTo(endDate) >= 0) {
				throw new InvalidReservationDetailException("Check out date "
						+ "cannot be before the check in date.");
			}
			return new Guest(guestName, cardDetails, address, country, 
					gender, nationality, Integer.parseInt(contact), identity, 
					paymentType, startDate, endDate);
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
		System.out.print("Enter your room number: ");
		String roomNum = HotelApp.scanner.nextLine();
		try {
			boolean isUserInputValid = false;
			Room room = hotel.getRoomByNo(roomNum);
			if (room.getGuest() == null) {
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
							System.out.print("Enter user input: ");
							userInput = HotelApp.scanner.nextLine().toLowerCase();
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
							default: {
								System.out.println("Invalid choice, please try again");
								System.out.print("Enter user input: ");
								userInput = HotelApp.scanner.nextLine().toLowerCase();
							}
						}
					}
				}
				else{
					System.out.print("Enter the updated information: ");
					String newDetail = HotelApp.scanner.nextLine().trim();
					hotel.updateRoomDetails(roomNum, detailToUpdate, newDetail);
					System.out.println("Room Information Updated!");
				}
			}
			else {
				System.out.println("Unable to update room with guest inside.");
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
			String roomNo;
			Guest guest;
			String status;
			RoomService roomService;
			Hashtable<String, String> orderMap = new Hashtable<String, String>();
			boolean orderMore = true;
			System.out.print(""
					+ "Please select one of the actions: \n"
					+ "|===========================|\n"
					+ "|(A) Change Order Status    |\n"
					+ "|(B) Create a New Order     |\n"
					+ "|(C) Print List of Orders   |\n"
					+ "|===========================|\n"
					+ "\nEnter user input: ");
			switch(scanner.nextLine().trim().toLowerCase()) {
				case "a": {
					try {
						System.out.print("Enter room number: ");
						roomNo = HotelApp.scanner.nextLine().trim();
						System.out.print("Enter the order ID: ");
						String orderId = HotelApp.scanner.nextLine().trim();
						System.out.print("Enter new status: \n"
								+ "|=================|\n"
								+ "|(A) Confirmed    |\n"
								+ "|(B) Preparing    |\n"
								+ "|(C) Delivered    |\n"
								+ "|=================|\n"
								+ "\nEnter user input: ");
						status = HotelApp.scanner.nextLine().trim();
						switch(status.toLowerCase()) {
							case "a": status = "confirmed";break;
							case "b": status = "preparing";break;
							case "c": status = "delivered";break;
							default: System.out.println("Invalid choice! Please retry.");
						}
						guest = hotel.getRoomByNo(roomNo).getGuest();
						roomService = guest.getRoomServiceByOrderId(orderId);
						roomService.setStatus(status);
						System.out.println("Room service update successful!");
				} catch (RoomNotFoundException | OrderIdNotFoundException e) {
					System.out.println(e.getMessage());
				} 
				break;
			}
			case "b": {
				menu.printItems();
				System.out.print("Enter room number: ");
				roomNo = HotelApp.scanner.nextLine().trim();
				try {
					hotel.getRoomByNo(roomNo); //using this to check if roomNo exists
					while (orderMore) {
						System.out.print("Enter food name: ");
						String foodName = HotelApp.scanner.nextLine().trim();
						System.out.print("Enter quantity to order: ");
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
					String orderId = HotelApp.currentTime.toString();
					String formattedOrderId = orderId.substring(4, 19);
					hotel.makeRoomServiceOrder(roomNo, menu, orderMap, formattedOrderId);
					System.out.println("Order successful! Your order id is: " + formattedOrderId);
				} catch (FoodNotOnMenuException | RoomNotFoundException | GuestNotFoundException |
						InputMismatchException| NumberFormatException e ) {
					System.out.println(e.getMessage());
				} 
				break;
			}
			case "c": {
				System.out.print("Enter room number: ");
				roomNo = HotelApp.scanner.nextLine().trim();
				try {
					guest = hotel.getRoomByNo(roomNo).getGuest();
					guest.printRoomServiceList();
				} catch (RoomNotFoundException e) {
					System.out.println(e.getMessage());
				} catch (GuestNotFoundException | NullPointerException e) {
					System.out.println("No Guest Assigned to The Room");
				}
				break;
			}
			default: System.out.println("Invalid input.");
		}
	}
	
	/**
	 * Functional requirement E. To edit the menu.
	 * @param menu
	 */
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
			HotelApp.scanner.nextLine();
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
			HotelApp.scanner.nextLine();
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
		boolean isUserInputValid = false;
		Date startDate = HotelApp.currentTime;
		Date endDate = HotelApp.currentTime;
		try {
			while (!isUserInputValid) {
				System.out.print("Enter start date in the following format (MM/DD/YYYY): ");
				startDate = new Date(HotelApp.scanner.nextLine().trim());
				System.out.print("Enter end date in the following format (MM/DD/YYYY)  : ");
				endDate = new Date(HotelApp.scanner.nextLine().trim());
				if (startDate.compareTo(hotel.getCurrentDate()) < 0) {
					System.out.println("Start date must be from today's date onwards. Retry.");
				}
				else if (endDate.compareTo(startDate) <= 0) {
					System.out.println("End date must be after start date. Retry.");
				}
				else {
					isUserInputValid = true;
				}
			}
			System.out.print("Enter room type                                      : ");
			String roomType = HotelApp.scanner.nextLine().trim();
			boolean roomTypeExists = false;
			while(!roomTypeExists) {
				roomTypeExists = hotel.doesRoomTypeExists(roomType);
				if(!roomTypeExists) {
					System.out.print("Invalid room type. Try again: ");
					roomType = HotelApp.scanner.nextLine().trim();
				}
			}
				
			if(hotel.checkRoomAvailability(startDate, endDate, roomType)) {
				System.out.println("Room type is available.");
			}
			else {
				System.out.println("Room type is not available.");
			}		
			}catch(RoomTypeNotFoundException | IllegalArgumentException e) {
					System.out.println(e.getMessage());
		}		
	}

	/**
	 * A function to show the functional requirements of G. to check in the guest
	 * @param hotel
	 */
	@SuppressWarnings("deprecation")
	public static void showMenuG(Hotel hotel){
		if (HotelApp.currentTime.getTime() < hotel.getCheckInDate().getTime()) {
			System.out.println("Check-in is only available from 2PM");
		}
		else {
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
					if (tempGuest.getStartDateOfStay().compareTo(hotel.getCurrentDate()) > 0) {
						System.out.println("It is not the time yet to check in.");
					}
					else {
						boolean success = false;
						String vacantRoomListString = 
								hotel.getRoomTypeToVacantRoomNoListTable(true).get(roomType).toString();
						System.out.println("Available rooms: " + vacantRoomListString.substring(1,
								vacantRoomListString.length()-1));
						System.out.print("Enter the room number guest will be assigned to: ");
						String roomNo = HotelApp.scanner.nextLine().trim();
						try{
							success = hotel.checkIn(tempGuest, roomNo, roomType,tempreservation);
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
				}
				else {
					System.out.println("Reservation Id is invalid.");
				}
			}
			else {
				try {
					Guest newGuest = HotelApp.createNewGuest(false, hotel.getCheckInDate(), 
							hotel.getCheckOutTimeInMilliSeconds());				
					System.out.print("Enter guest's preferred room type                   : ");
					String roomType = HotelApp.scanner.nextLine().trim();
					System.out.print("Enter number of people                              : ");
					int numOfPeople;
					try {
						numOfPeople = HotelApp.scanner.nextInt();
						HotelApp.scanner.nextLine();
						if (numOfPeople < 1) {
							throw new InvalidGuestDetailException();
						}
					}
					catch (InputMismatchException e) {
						HotelApp.scanner.nextLine();
						throw new InvalidGuestDetailException();
					}
					String paymentType = newGuest.getPaymentType();
					boolean roomTypeExists = false;
					while(!roomTypeExists) {
						roomTypeExists = hotel.doesRoomTypeExists(roomType);
						if(!roomTypeExists) {
							System.out.println("Invalid room type. Try again:");
							roomType = HotelApp.scanner.nextLine().trim();
						}
					}
					Reservation reservation = null;
					if(((int)(new Date(
							newGuest.getEndDateOfStay().toLocaleString().split(",")[0]).getTime() - 
							new Date(newGuest.getStartDateOfStay().toLocaleString(
									).split(",")[0]).getTime())/(1000*60*60*24)) > 1) {
						reservation = new Reservation(hotel.getReservationSystem().generateNewId(), newGuest, 
								newGuest.getStartDateOfStay(), newGuest.getEndDateOfStay(), numOfPeople, paymentType, roomType);
					}
					boolean roomNoAvailable = false;
					if(hotel.checkRoomAvailability(newGuest.getStartDateOfStay(), newGuest.getEndDateOfStay(), 
							roomType)) {
						String vacantRoomListString = 
								hotel.getRoomTypeToVacantRoomNoListTable(true).get(roomType).toString();
						System.out.println("Available rooms: " + vacantRoomListString.substring(1,
								vacantRoomListString.length()-1));
						System.out.print("Enter the room number to assign to                  : ");
						String roomNo = HotelApp.scanner.nextLine().trim();
						try{
							roomNoAvailable = hotel.checkIn(newGuest, roomNo, roomType);
							if(roomNoAvailable) {
								if(reservation != null) {
									hotel.getReservationSystem().addReservation(reservation, 
											hotel.getNumberOfRoomsByRoomType(reservation.getRoomType()));
									hotel.getReservationSystem().updateCheckedInReservationStatusnByIdAndRoomType(
											reservation.getReservationId(), roomType);
								}
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
						InvalidReservationDetailException | DuplicateReservationFoundException | IdGenerationFailedException e) {
						System.out.println(e.getMessage());
				}catch(NumberFormatException e) {
					System.out.println("Invalid entry.");
				}
			}
		}
		
	}

	/**
	 * Functinoal requirement H, to check out the guest.
	 * @param hotel
	 */
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
		} catch (RoomNotFoundException | GuestNotFoundException | RoomTypeNotFoundException e) {
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
						   "|(C) Reserved Rooms          |\n"+
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
						if (vacantRoomNoList != null){
							System.out.println("\tNumber: " + 
									vacantRoomNoList.size() + " out of " 
									+ hotel.getNumberOfRoomsByRoomType(roomType));
							String vacantRoomListString = vacantRoomNoList.toString();
							System.out.println("\tRooms: " + vacantRoomListString.substring(1, 
									vacantRoomListString.length()-1));
						}
						else {
							System.out.println("\tNumber: 0 out of " + 
									hotel.getNumberOfRoomsByRoomType(roomType));
							System.out.println("\tRooms: ");
						}
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
				case "c":{
					System.out.print("Select the date (MM/DD/YYYY): ");
					try {
						@SuppressWarnings("deprecation")
						Date date = new Date(HotelApp.scanner.nextLine().trim());
						ConcurrentHashMap<String, ArrayList<Reservation>> reservationsMap = 
								hotel.getReservationSystem().getReservationsByDate(date);
						if (reservationsMap != null) {
							for (String roomType: reservationsMap.keySet()) {
								ArrayList<String> reservedRoomNoList = new ArrayList<String>();
								String startingRoomNo = "01";
								String roomLevel = hotel.getRoomLevelByRoomType(roomType);
								for (Reservation reservation: reservationsMap.get(roomType)) {
									if (reservation.getStatus().equals("checked-in")) {
										try {
											String roomNoOfCheckedInGuest = 
													hotel.getRoomNoOfCheckedInGuestFromReservation(
															reservation.getGuest(), roomType);
											reservedRoomNoList.add(roomNoOfCheckedInGuest);
										} catch (GuestNotFoundException e) {
											System.out.println(e.getMessage());
										}
									}
									else if (reservation.getStatus().equals("confirmed")){
										while(reservedRoomNoList.contains(roomLevel + "-" + startingRoomNo)) {
											int roomNo = Integer.parseInt(startingRoomNo);
											if (roomNo < 10) {
												startingRoomNo = "0" + Integer.toString(roomNo+1);
											}
											else {
												startingRoomNo = Integer.toString(roomNo+1);
											}
										}
										reservedRoomNoList.add(roomLevel + "-" + startingRoomNo);
									}
								}
								String sortedRoomNoList = 
										Hotel.getSortedRoomNoList(reservedRoomNoList).toString(); 
								System.out.println(roomType + ": " + 
										sortedRoomNoList.substring(1, sortedRoomNoList.length()-1));
							}
						}
						else {
							System.out.println("There is no reservation on this date.");
						}
					}
					catch(IllegalArgumentException e) {
						System.out.println("Invalid date and time format.");
					}
					break;
				}
				default: System.out.println("Invalid input.");
			}
		} catch(RoomTypeNotFoundException e){
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * A function to fast forward the hotel app by a number of days.
	 * @param {numOfDays} the number of days to move forward to
	 * @throws {InvalidHotelTimeException} when the time to shift to is not valid 
	 */
	public static void fastForwardByNumberOfDays(int numOfDays) throws 
		InvalidHotelTimeException {
		if (numOfDays >= 0) {
			HotelApp.currentTime = new Date(HotelApp.currentTime.getTime() + numOfDays*24*60*60*1000);
		}
		else {
			throw new InvalidHotelTimeException("System is not able to revert time back.");
		}
	}
	
	/**
	 * A function to show hotel app menu to change hotel time.
	 */
	public static void showMenuJ() {
		System.out.println("The current date and time is: " + HotelApp.currentTime.toString());
		System.out.print("Enter the number of days to fast forward to: ");
		try {
			int numOfDays = HotelApp.scanner.nextInt();
			HotelApp.fastForwardByNumberOfDays(numOfDays);
			System.out.println("Update successful. \nThe updated date and time is : " + 
					HotelApp.currentTime.toString());
			HotelApp.isSaveState = false;
		}
		catch (InputMismatchException e) {
			System.out.println("Invalid input. ");
		}
		catch (InvalidHotelTimeException e) {
			System.out.println(e.getMessage());
		}
		HotelApp.scanner.nextLine();
	}
	
	/**
	 * A function to save the current state of hotel and menu
	 * @param hotel {Hotel} the Hotel of HotelApp
	 * @param menu {Menu} the Menu of HotelApp
	 */
	public static void saveState(Hotel hotel, Menu menu) {
		if(HotelApp.isSaveState) {
			try {
				String dirToSave = "src/state_data";
				if(!Files.isDirectory(Paths.get(dirToSave))) {
					(new File(dirToSave)).mkdirs();
				}
				FileOutputStream hotelFile=new FileOutputStream(dirToSave + "/hotel_state.txt");  
				ObjectOutputStream out=new ObjectOutputStream(hotelFile);  
				out.writeObject(hotel);  
				out.flush();  
				FileOutputStream menuFile=new FileOutputStream(dirToSave + "/menu_state.txt");  
				out=new ObjectOutputStream(menuFile);  
				out.writeObject(menu);  
				out.flush();
				out.close(); 
			}catch(IOException e){
				System.out.println(e.getMessage());
			}  
		}
	}

	/**
	 * A function to load the previous state of hotel
	 * @return {Hotel} the hotel object from the previous state
	 */
	public static Hotel loadStateHotel() {
		Hotel hotel = null;
		try{   
			ObjectInputStream hotelIn=new ObjectInputStream(
					new FileInputStream("src/state_data/hotel_state.txt"));  
			hotel=(Hotel)hotelIn.readObject();  
			hotelIn.close(); 
		}catch(IOException | ClassNotFoundException e){
			System.out.println(e.getMessage());
		}
		return hotel;  
	}
	
	/**
	 * A function to load the previous state of menu
	 * @return {Menu} the menu object from the previous state
	 */
	public static Menu loadStateMenu() {
		Menu menu = null;
		try {
			ObjectInputStream menuIn=new ObjectInputStream(
					new FileInputStream("src/state_data/menu_state.txt"));  
			menu=(Menu)menuIn.readObject();  
			menuIn.close();
		}catch(IOException | ClassNotFoundException e){
			System.out.println(e.getMessage());
		}
		return menu;
	}
}
