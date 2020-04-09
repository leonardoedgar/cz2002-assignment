package resources;
import java.util.ArrayList;
import java.util.Hashtable;
import resources.ReservationSystem;
import resources.Room;
import resources.Guest;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import exception.HotelSetupFailureException;
import exception.RoomNotFoundException;
import exception.GuestDetailUpdateFailureException;
import exception.GuestNotFoundException;

/**
 * A class to represent a hotel.
 */
public class Hotel {
	// {"roomType": {"roomNo": Room()}}
	Hashtable<String, Hashtable<String, Room>> roomTable = new Hashtable<String, Hashtable<String, Room>>();
	private ReservationSystem reservationSystem;
	
	/**
	 * A class constructor to create a hotel. 
	 * @param roomConfigFilePath {String} the path to room configuration file
	 * @throws {HotelSetupFailureException} exception when hotel failed to set up
	 */
	public Hotel(String roomConfigFilePath) throws HotelSetupFailureException {
		this.setupRooms(this.getRoomConfig(roomConfigFilePath));
		this.reservationSystem = new ReservationSystem();
	}
	
	//added hashtable {"roomType":guestId:Guest()}
	//during checkin, guess is assigned a roomNo
	Hashtable<Guest,String> guestList = new Hashtable<Guest,String>();
	
	//in main app, everytime a guest is create, add them to the GuestList
	/**
	 * A function to add new guests into the guestList HashTable
	 * @param guest {Guest} the guest to be added
	 * @param roomType {String} the roomType chosen by the guest
	 */
	public void addToGuestList(Guest guest,String roomType) {
		guestList.put(guest,roomType);
	}

	/**
	 * A function to setup rooms in a hotel.
	 * @param roomConfig {Hashtable<String, Hashtable<String, String>>} the room configuration to built from.
	 */
	private void setupRooms(Hashtable<String, Hashtable<String, String>> roomConfig) {
		int floorNo = 1;
		for (String roomType: roomConfig.keySet()) {
			Hashtable<String, String> roomDetail = roomConfig.get(roomType);
			Hashtable<String, Room> roomDataPerLevel = new Hashtable<String, Room>();
			for(int i=1; i<= Integer.parseInt(roomDetail.get("numberOfRooms")); i++) {
				String roomNo = "0" + Integer.toString(floorNo) + "-";
				if (i > 9) {
					roomNo += Integer.toString(i);
				}
				else {
					roomNo += "0" + Integer.toString(i);
				}
				final String finalRoomNo = roomNo;
				roomDataPerLevel.put(finalRoomNo, new Room(
							finalRoomNo, 
							Double.parseDouble(roomDetail.get("cost")),
							roomDetail.get("bedType"),
							roomDetail.get("wifi"),
							roomDetail.get("view"),
							roomDetail.get("smoking")
					)); 
				};
			roomTable.put(roomType, roomDataPerLevel);
			floorNo += 1;
		}
	}
	
	/**
	 * A function to get the room configuration.
	 * @param roomConfigFilePath {String} the path to the room configuration file
	 * @return {Hashtable<String, Hashtable<String. String>>} the room configuration 
	 * @throws {HotelSetupException} failure in setting up the hotel (configuration file not found)
	 */
	@SuppressWarnings("serial")
	private Hashtable<String, Hashtable<String, String>> getRoomConfig(String roomConfigFilePath) throws HotelSetupFailureException {
		try {
			FileReader frStream = new FileReader(roomConfigFilePath);
			BufferedReader brStream = new BufferedReader(frStream);
			Hashtable<String, Hashtable<String, String>> roomConfig = new Hashtable<String, Hashtable<String, String>>();
			brStream.readLine();
			String aLine = brStream.readLine();
			while(aLine != null) {
				String[] aLineArr = aLine.split("\\|");
				roomConfig.put(aLineArr[1].trim(), new Hashtable<String, String>() {{ 
					put("numberOfRooms", aLineArr[2].trim());
					put("cost", aLineArr[3].trim());
					put("bedType", aLineArr[4].trim());
					put("wifi", aLineArr[5].trim());
					put("view", aLineArr[6].trim());
					put("smoking", aLineArr[7].trim());
				}});
				aLine = brStream.readLine();
			}
			brStream.close();
			return roomConfig;
		}
		catch (FileNotFoundException e) {
			System.out.println("Err: " + e.getMessage());
			throw new HotelSetupFailureException();
		}
		catch (IOException e) {
			System.out.println("Err: " + e.getMessage());
			throw new HotelSetupFailureException();
		}
	}

	/** 
	 * A function to get guest in the hotel by the name.
	 * @param name {String} the name of the guest
	 * @return {Guest} the guest object
	 * @throws {GuestNotFoundException} when guest's name is not registered in the hotel
	 */
	public Guest getGuestByName(String name) throws GuestNotFoundException {
		for (String roomType: this.roomTable.keySet()) {
			for(String roomNo: this.roomTable.get(roomType).keySet()) {
				Room roomDetail = this.roomTable.get(roomType).get(roomNo);
				if (roomDetail.getGuest().getName() == name) {
					return roomDetail.getGuest();
				}
			}
		}
		throw new GuestNotFoundException();
	}
	
	/**
	 * A function to update guest details by the name.
	 * @param name {String} the guest's name
	 * @param detailToUpdate {String} detail's of guest to update
	 * @param newData {String} the new data
	 * @throws {GuestNotFoundException} when guest's name is not registered in the system
	 * @throws {GuestDetailUpdateFailureException} when system failed to update guest's data due to data does not exist
	 */
	public void updateGuestDetailsByName(String name, String detailToUpdate, String newData) throws GuestNotFoundException, GuestDetailUpdateFailureException {
		Guest guest = this.getGuestByName(name);
		guest.updateDetails(detailToUpdate, newData);
	}
	
	/**
	 * A function to print guest details by the name.
	 * @param name {String} the name of the guest
	 * @throws {GuestNotFoundException} when guest's name is not registered in the system
	 */
	public void printGuestDetailsByName(String name) throws GuestNotFoundException {
		Guest guest = this.getGuestByName(name);
		guest.printDetails();
	}

/**
 * this method will try to find if the room exist in the hotel system by comparing the room number. If no such roome exist, we will throw roomnotfound error.
 * @param Room_num
 * @return
 * @throws RoomNotFoundException
 */
	public Room getRoomByNo(String Room_num) throws RoomNotFoundException{
		for (String roomType: this.roomTable.keySet()) {
			for (String roomNo: this.roomTable.get(roomType).keySet()) {
				if ((roomNo).equals(Room_num)) {
					return this.roomTable.get(roomType).get(roomNo);
				}
			}
		}
		throw new RoomNotFoundException();
	}
//	public Room getRoomByNo(String Room_num){
//		for (String roomType: this.roomTable.keySet()) {
//			for (String roomNo: this.roomTable.get(roomType).keySet()) {
//				if ((roomType+"-"+roomNo).equals(Room_num)) {
//					return this.roomTable.get(roomType).get(roomNo);
//				}
//			}
//		}
//	}

	/**
	 * details refer to the choice of information to be updated by the user of this app.
	 * @param roomNo
	 * @param details
	 * @param newData
	 * @throws RoomNotFoundException
	 */
	public void updateRoomDetails(String roomNo, char details, String newData) throws RoomNotFoundException{
		Room room = this.getRoomByNo(roomNo);
		switch(details) {
		case 'a':
		case 'A':{
			room.updateStatus(newData);
			break;
		}
		case 'b':
		case 'B':{
			room.updateCost(newData);
			break;
		}
		case 'c':
		case 'C':{
			room.updateBedType(newData);
			break;
		}
		case 'd':
		case 'D':{
			room.updateWifi(newData);
			break;
		}
		case 'e':
		case 'E':{
			room.updateView(newData);
			break;
		}
		case 'f':
		case 'F':{
			room.updateSmoking(newData);
			break;
		}
		default: System.out.println("Invalid input. Retry\n");
		}
	}

	/**
	 * A function to get the reservation system in a hotel.
	 * @return {ReservationSystem} the reservation system
	 */
	public ReservationSystem getReservationSystem() {
		return this.reservationSystem;
	}
	
	/**
	 * A function to get available room types in a hotel.
	 * @return {ArrayList<String>} that contains available room types
	 */
	public ArrayList<String> getAvailableRoomTypes() {
		ArrayList<String> roomTypes = new ArrayList<String>();
		for(String roomType: this.roomTable.keySet()) {
			roomTypes.add(roomType);
		}
		return roomTypes;
	}
	
	/**
	 * A function to check for Date clashes
	 * @param newStartDate {Date} Start date of new guest
	 * @param newEndDate {Date} End date of new guest
	 * @param existingStartDate {Date} Start date of existing guest
	 * @param existingEndDate {Date} End date of existing guest
	 * @return {Integer} returns 1 if dates clash, 0 if dates do not clash
	 */
	public int checkDateClash(Date newStartDate,Date newEndDate,Date existingStartDate,Date existingEndDate){
		//returns 1 if date clash, 0 if does not clash
		
		//both new and existing start on same day, clash
		if(newStartDate.compareTo(existingStartDate)==0) {
			return 1;
		}
		//newStartDate after or existingStartDate
		if(newStartDate.compareTo(existingStartDate)>0) {
			
			
			//if newStartDate after or on existingEndDate (Prev guest checked out), no clash
			if(newStartDate.compareTo(existingEndDate)>=0) {
				return 0;
			}
			//otherwise clash
			return 1;
		}
		
		//newStartDate before existingStartDate
		if(newStartDate.compareTo(existingStartDate)<0){
			
			//newEndDate before or on existingStartDate (new guest leaves before existing guest comes), no clash
			if(newEndDate.compareTo(existingStartDate)<=0) {
				return 0;
			}
			//otherwise clash
			return 1;
		}
		
		return 1;
		
	}
	
	/**
	 * A function to check if there are any rooms of a particular room type available
	 * @param startDate {Date} Start date of new guest
	 * @param endDate {Date} End date of new guest
	 * @param roomType {String} roomType chosen by the new guest
	 * @return boolean, true if available, false if not available
	 */
	public boolean checkRoomAvailability(java.util.Date startDate, java.util.Date endDate, String roomType) {
		//assume all rooms are available at the start
		if(roomTable.get(roomType)==null) {
			System.out.println("Room type does not exist.");
			return false;
		}
		
		int roomsLeftForDate=12;

		//check all guests in guestList
		for(Guest key: guestList.keySet()) {
			
			if(guestList.get(key).contentEquals(roomType)) {
				roomsLeftForDate=roomsLeftForDate-checkDateClash(startDate,endDate,key.getstartDate(),key.getendDate()); //if dates clash, minus 1 from available rooms of that roomType
		
			}
		}
		
		if(roomsLeftForDate==0) {
			return false; //not available
		}
		
		return true; //available
	
	}
	
	
	/** 
	 * A function to get guest in the hotel by the identity.
	 * @param identity {String} the identity number of the guest
	 * @return {Guest} the guest object containing the identity number
	 * @throws {GuestNotFoundException} when guest's identity is not registered in the hotel
	 */
	public Guest getGuestByIdentity(String identity) throws GuestNotFoundException {
		
		for(Guest key:guestList.keySet()) {
			if(key.getIdentity().contentEquals(identity)) {
				return key;
			}
		}
		throw new GuestNotFoundException();
	}
	
	public String getGuestRoomType(Guest guest) {
	String roomType=null;
		
		//check the roomType guest has requested for during creation of guest object
		for(Guest key:guestList.keySet()) {
			if(key==guest){
				roomType=guestList.get(key);
			}
		}
		
		return roomType;
	}
	
	//assuming staff checks for currently available rooms using report and assigns a room based on that
	//add exception for invalid roomNo
	/**
	 * A function to check-in a guest who arrived at the hotel into a room chosen by the staff
	 * @param roomNo {String} roomNo of an empty room
	 * @param guest {Guest} object of the guest who wants to check-in
	 * @param roomType {String} roomType chosen by the guest whe
	 * @return
	 */
	public boolean checkIn(Guest guest,String roomNo) {
		String roomType=getGuestRoomType(guest);
		
		Hashtable<String, Room> tempRoomList = roomTable.get(roomType);

		if(tempRoomList==null) {
			System.out.println("Room type does not exist.");
			return false;
		}
		
		//mapping of roomtype was different for some reason: double =1 single =4, vip=3, deluxe=2
		Room tempRoom = tempRoomList.get(roomNo);
		if(tempRoom==null) {
			System.out.println("Room number does not exist for room type "+roomType+".");
			return false;
		}
		//use the method below to check for mapping of hashtable
		//System.out.print(roomTable.get(roomType));
		
		if(tempRoom.getStatus().equals("vacant")) {
			tempRoom.assignGuestToRoom(guest);
			//need to change room status from vacant to nonvacant
			//updateRoomStatus();
			System.out.println("Check-in successful!");
			return true;
		}
		else {
			System.out.println("Room is currently unavailable. Choose another room.");
			return false;
		}
		
	}

//	public void printStatusReport() {
//	for (String roomType: this.roomTable.keySet()) {
//		for(String roomNo: this.roomTable.get(roomType).keySet()) {
//			System.out.println("Room Type: " + roomType + " Room No: " + roomNo + " view: "+ this.roomTable.get(roomType).get(roomNo).view);
//		}
//	}
//}
//	
//	public boolean reassignRoomByGuestName(String name, String newRoomNumber) {
//	checkIfGuestInsideHotel();
//	checkIfNewRoomNumberIsVacant();
//	RemoveGuestFromRoom();
//	AssignGuestToRoom();
//	return true;
//}
//	
//	public boolean createReservation(Guest guest, java.util.Date check_in_date, java.util.Date check_out_date, String roomType) {
//		this.reservationSystem.add(guest, check_in_date, check_out_date, roomType);
//	}
//	
//	public boolean checkRoomAvailability(java.util.Date start_date, java.util.Date end_date, String roomType) {
//		return true;
//	}
//	public boolean checkIn(Guest guest, String roomType) {
//		AssignGuestToRoom();
//		this.reservationSystem.addReservation(CreateReservationFromGuest(guest));
//		return true;
//	}
//	public void updateRoomStatus(String roomNo, String status) {
//		// add switch case for valid status, then call room method to update its status
//	}
//	public void printReport() {
//		//print rooms availability
//	}
}

