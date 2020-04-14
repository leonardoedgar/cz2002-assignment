package main;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.InputMismatchException;
import java.lang.NumberFormatException;
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
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		boolean exitApp = false;
		
		try {
			Hotel hotel = new Hotel("src/data/roomConfig.txt");
			
		
			
			Menu menu = new Menu("src/data/menu.txt");
			while (!exitApp) {
				HotelApp.printHotelAppMenu();
				System.out.print("Enter user input: ");
				switch(sc.nextLine()) {
					case "a":
					case "A": HotelApp.showMenuA(hotel); break;
					case "b":
					case "B": HotelApp.showMenuB(hotel);; break;
					case "c":
					case "C": HotelApp.showMenuC(hotel);break;
					case "D": 
					case "d": HotelApp.showMenuD(hotel, menu);break;
					case "e":
					case "E": HotelApp.showMenuE(menu); break;
					case "f":
					case "F": HotelApp.showMenuF(hotel);break;
					case "g":
					case "G": HotelApp.showMenuG(hotel);break;
					case "H":
					case "h": HotelApp.showMenuH(hotel);break;
					case "i":
					case "I": HotelApp.showMenuI(hotel); break;
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
		catch (FileNotFoundException e) {
			System.out.println("Err: " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("Err: " + e.getMessage());
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
	 */
	public static void showMenuA(Hotel hotel) {
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
				try {
					hotel.printGuestDetailsByName(sc.nextLine().trim());
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
			case "b":
			case "B": {
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
			case "c":
			case "C": {
				System.out.print("Enter reservation ID to remove: ");
				try {
					String reservationId = sc.nextLine().trim();
					Reservation reservation = hotel.getReservationSystem().getReservation(reservationId);
					hotel.getReservationSystem().removeReservation(reservationId, 
							hotel.getNumberOfRoomsByRoomType(reservation.getRoomType()));
					System.out.println("Reservation removed successfully!\n");
				} catch (ReservationNotFoundException | RoomTypeNotFoundException e) {
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
	public static Reservation createNewReservation(ArrayList<String> roomTypes, Date currentDate) 
			throws InvalidReservationDetailException, InvalidGuestDetailException {
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter reservation ID                 : ");
		String reservationId = sc.nextLine().trim();
		Guest guest = HotelApp.createNewGuest(true, currentDate);
		Date checkInDate=guest.getstartDate();
		Date checkOutDate = guest.getendDate();
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
	 * @param isForReservation {boolean} whether the guest created wants to make a reservation
	 * @param currentDate {Date} the current date in the hotel system
	 * @return {Guest} the guest object
	 * @throws InvalidReservationDetailException  exception when check in date is later than check out date
	 * @throws {InvalidGuestDetailException} exception when guest detail input is not valid 
	 */
	public static Guest createNewGuest(boolean isForReservation, Date currentDate) 
			throws InvalidGuestDetailException, InvalidReservationDetailException {
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
		Date startDate= new Date();
		Date endDate= new Date();
		if (isForReservation) {
			System.out.print("Enter date of check-in (MM/DD/YYYY)  : ");
			startDate=new Date(sc.nextLine().trim());
			if (startDate.compareTo(currentDate) < 0) {
				throw new InvalidReservationDetailException();
			}
		}
		else {
			startDate = currentDate;
		}
		System.out.print("Enter date of check-out (MM/DD/YYYY) : ");
		endDate=new Date(sc.nextLine().trim());	
		if (startDate.compareTo(endDate) >= 0) {
			throw new InvalidReservationDetailException();
		}
		
		try {
			return new Guest(guestName, cardDetails, address, country, 
					gender, nationality, Integer.parseInt(contact), identity,startDate,endDate);
		}
		catch (NumberFormatException e) {
			throw new InvalidGuestDetailException();
		}
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
				+ "|===============================|\n"
				+ "|(A) Room status                |\n"
				+ "|(B) Room price                 |\n"
				+ "|(C) Bed type                   |\n"
				+ "|(D) Wifi Availability          |\n"
				+ "|(E) View of the Room           |\n"
				+ "|(F) Smoking Allowance (yes/no) |\n"
				+ "|===============================|\n"
				+ "\nEnter user input: ");
		String choice = sc.nextLine();
		switch(choice) {
		case "a":
		case "A": choice = "status";break;
		case "b":
		case "B": choice = "price";break;
		case "c":
		case "C": choice = "bed type";break;
		case "d":
		case "D": choice = "wifi";break;
		case "e":
		case "E": choice = "view";break;
		case "f":
		case "F": choice = "smoking";break;
		default: System.out.println("Invalid choice! Please try again.");
		}

		if (choice.equals("status")) {
			System.out.print(""
					+ "New room status: \n"
					+ "|======================|\n"
					+ "|(A) Vacant            |\n"
					+ "|(B) Under Maintainance|\n"
					+ "|======================|\n"
					+ "\nEnter user input: ");
			String status = sc.next();
			if (status.equals("a")|status.equals("A")) {
				hotel.updateRoomDetails(room_num, choice, "vacant");
				System.out.println("Room Information Updated!");
			}
			else if (status.equals("b")|status.equals("B")) {
				hotel.updateRoomDetails(room_num, choice, "under maintainance");
				System.out.println("Room Information Updated!");
			}
			else {
				System.out.println("Invalid choice, please try again");
			}
		}
		else{
			System.out.println("Enter the updated information: ");
			String new_data = sc.nextLine();
			hotel.updateRoomDetails(room_num, choice, new_data);
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
			Scanner sc = new Scanner(System.in);
			boolean orderMore = true, orderSuccess = true;
			menu.printItems();
			System.out.print("Enter room number: ");
			String roomNo = sc.nextLine().trim();
			while (orderMore) {
				System.out.print("Enter food name: ");
				String foodName = sc.nextLine().trim();
				System.out.print("Enter quantity to order: ");
				try {
					String quantity = Integer.toString(sc.nextInt());
					sc.nextLine();
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
					orderMore = sc.nextLine().trim().equalsIgnoreCase("yes");
				}
				catch(InputMismatchException e) {
					System.out.println("Invalid input. Retry");
					orderSuccess = false;
					break;
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
		Scanner scn = new Scanner(System.in);
		System.out.println("Would you like to:\n"
				+ "|=================================|\n"
				+ "|(A) Add Items in the Menu        |\n"
				+ "|(B) Remove Items from the Menu   |\n"
				+ "|(C) Update Items from the Menu   |\n"
				+ "|(D) Print the Menu               |\n"
				+ "|=================================|"
				+ "\nYour choice: ");
		switch(scn.nextLine()) {
		case "a":
		case "A":{
			System.out.println("Enter the food name:");
			foodname = scn.nextLine();
			System.out.println("Enter the price of the food:");
			price = scn.nextDouble();
			menu.addItems(foodname,price);
			break;}
		case "b":
		case "B":{
			System.out.println("Enter the food name:");
			foodname = scn.nextLine();
			menu.removeItems(foodname);
			break;}
		
		case "c":
		case "C":{
			System.out.println("Enter the food name:");
			foodname = scn.nextLine();
			System.out.println("Enter the price of the food:");
			price = scn.nextDouble();
			menu.updateItems(foodname,price);
			break;}
		
		case "d":
		case "D":{
			menu.printItems();
			break;}
			
		}
	}
	/**
	 * A function to show the functional requirements f
	 * @param hotel
	 */
	@SuppressWarnings("deprecation")
	public static void showMenuF(Hotel hotel) {

		Scanner sc = new Scanner(System.in);
			
				Date startDate = new Date();
				Date endDate = new Date();
				
				
				System.out.println("Enter start date in the following format: MM/DD/YYYY");
				
				try {
				startDate= new Date(sc.nextLine().trim());

				System.out.println("Enter end date in the following format: MM/DD/YYYY");
				int checker=1;

				while(checker==1) {

					endDate= new Date(sc.nextLine().trim());
					
					if(endDate.compareTo(startDate)>0) {
						checker=0;
					}
					else {
						System.out.println("End date must be after startDate, enter another end date:");
					}
				}
				
				System.out.println("Enter roomType:");
				String roomType=sc.next().trim();
				
				boolean roomTypeChecker = false;
				while(roomTypeChecker == false) {
					roomTypeChecker=hotel.doesRoomTypeExists(roomType);
					if(roomTypeChecker==false) {
						System.out.println("Invalid room type. Try again:");
						roomType = sc.next().trim();
					}
				}
				
				if(hotel.checkRoomAvailability(startDate, endDate, roomType)==true) {
					System.out.println("Room type is available.");
				}
				else {
					System.out.println("Room type is not available.");
				}		
		}catch(RoomTypeNotFoundException e) {
			System.out.println(e.getMessage());
		}
			
			
}
	
	/**
	 * A function to show the functional requirements of G
	 * @param hotel
	 */
	public static void showMenuG(Hotel hotel){
		Scanner sc = new Scanner(System.in);
		//does guest have a reservation
		String hasReservation="";
		System.out.println("Does the guest have a reservation? (y/n)");
		
		//input checking, only allows user to enter y or n
		while(hasReservation=="") {
			hasReservation=sc.next().trim();
			switch(hasReservation) {
			case "y":
			case "Y":break;
			case "n":
			case "N":break;
			default: hasReservation="";
			System.out.println("Invalid input. Enter: (y/n)");break;
		}
		}
		
		if(hasReservation.equals("y")||hasReservation.equals("Y")) {
			//check guest details from reservation system and assign to room
			System.out.println("Enter Reservation Id.");
			String reservationId=sc.next().trim();
			
			
			Reservation tempreservation =hotel.getReservationSystem().getReservation(reservationId);
			if(tempreservation!=null) {
				String roomType = tempreservation.getRoomType();
				Guest tempGuest = tempreservation.getGuest();

				boolean checker=false;
				System.out.println("Enter the room number guest will be assigned to:");
				String roomNo=sc.next().trim();
				
				try{
					checker=hotel.checkIn(tempGuest, roomNo, roomType,tempreservation);
				
				if(checker==true) {
					System.out.println("Check-in successful!");
				}
				else {
					System.out.println("Room is currently unavailable. Choose another room.");
				}
				}catch(RoomNotFoundException e) {

					System.out.println(e.getMessage());
				}
			}
			else {
				System.out.println("Reservation Id is invalid.");
			}
			
			
		}
		
		else if(hasReservation.equals("n")||hasReservation.equals("N")) {
			//create new guest object and assign to room
			try {
				System.out.println("Enter new guest details:");
				Guest newGuest = HotelApp.createNewGuest(false, hotel.getCurrentDate());				
				System.out.println("Enter guest's preferred roomType:");
				String roomType = sc.next().trim();
				boolean roomTypeChecker = false;
				while(roomTypeChecker == false) {
					roomTypeChecker=hotel.doesRoomTypeExists(roomType);
					if(roomTypeChecker==false) {
						System.out.println("Invalid room type. Try again:");
						roomType = sc.next().trim();
					}
				}
					
				
				boolean roomNoChecker=false;
				
				if(hotel.checkRoomAvailability(newGuest.getstartDate(), newGuest.getendDate(), roomType)==true) {
					System.out.println("Enter the room number guest will be assigned to:");
					String roomNo=sc.next().trim();
					try{
						roomNoChecker=hotel.checkIn(newGuest, roomNo, roomType);
					
					
					if(roomNoChecker==true) {
						System.out.println("Check-in successful!");
					}
					else {
						System.out.println("Room is currently unavailable. Choose another room.");
					}
					}catch (RoomNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}else {
					System.out.println("Room type is currently fully booked. Guest is not assigned to a room and the details will be deleted.");
					newGuest=null;
				}
				
			} catch(InvalidGuestDetailException |RoomTypeNotFoundException | 
					InvalidReservationDetailException e) {
					System.out.println(e.getMessage());
			}}
		}
	public static void showMenuH(Hotel hotel) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter room number to check out: ");
		String roomNo = sc.nextLine().trim();
		System.out.println("Enter guest full name for verification: ");
		String guestName = sc.nextLine().trim();
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
		Scanner sc = new Scanner(System.in);
		System.out.println("Print room statistics by:\n"+
						   "|============================|\n"+
						   "|(A) Room type occupancy rate|\n"+
						   "|(B) Room status             |\n"+
						   "|============================|\n"+
						   "Enter your choice:");
		try {
			switch(sc.nextLine()) {
				case "a":
				case "A":{
						ArrayList<String> roomTypeList = hotel.getAvailableRoomTypes();
						for (String roomType: roomTypeList) {
							ArrayList<String> vacantRoomNoList = 
									hotel.getRoomTypeToVacantRoomNoListTable().get(roomType);
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
				case "b":
				case "B":{
					Hashtable<String, ArrayList<String>> roomStatusToRoomNoListTable = 
							hotel.getRoomStatusToRoomNoListTable();
					for (String roomStatus: roomStatusToRoomNoListTable.keySet()) {
						System.out.println(Character.toUpperCase(roomStatus.charAt(0)) + 
								roomStatus.substring(1) + " :");
						String roomListString = roomStatusToRoomNoListTable.get(roomStatus).toString();
						System.out.println(roomListString.substring(1, roomListString.length()-1));
					}
					break;
				}
		}
	} catch(RoomTypeNotFoundException e){
		System.out.println(e.getMessage());
	}}
}
