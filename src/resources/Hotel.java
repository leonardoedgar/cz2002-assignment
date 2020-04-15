package resources;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import resources.ReservationSystem;
import resources.Room;
import resources.Guest;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import exception.HotelSetupFailureException;
import exception.ReservationNotFoundException;
import exception.RoomNotFoundException;
import exception.FoodNotOnMenuException;
import exception.RoomTypeNotFoundException;
import exception.GuestDetailUpdateFailureException;
import exception.GuestNotFoundException;

/**
 * A class to represent a hotel.
 */
public class Hotel {
	// {"roomType": {"roomNo": Room()}}
	Hashtable<String, Hashtable<String, Room>> roomTable;
	private ReservationSystem reservationSystem;
	private Date currentDate;
	private int checkInTimeInMilliSeconds; 
	private int checkOutTimeInMilliSeconds;
	/**
	 * A class constructor to create a hotel. 
	 * @param roomConfigFilePath {String} the path to room configuration file
	 * @throws {HotelSetupFailureException} exception when hotel failed to set up
	 */
	public Hotel(String roomConfigFilePath) throws HotelSetupFailureException {
		this.setupRooms(this.getRoomConfig(roomConfigFilePath));
		this.reservationSystem = new ReservationSystem();
		this.currentDate = new Date();
		this.checkInTimeInMilliSeconds = 1000*60*60*14;
		this.checkOutTimeInMilliSeconds = 1000*60*60*12;
	}
	
	/**
	 * A function to get hotel's current date.
	 * @return {Date} the current date
	 */
	public Date getCurrentDate() {
		return this.currentDate;
	}

	/**
	 * A function to setup rooms in a hotel.
	 * @param roomConfig {Hashtable<String, Hashtable<String, String>>} the room configuration to built from.
	 */
	private void setupRooms(Hashtable<String, Hashtable<String, String>> roomConfig) {
		this.roomTable = new Hashtable<String, Hashtable<String, Room>>();
		for (String roomType: roomConfig.keySet()) {
			Hashtable<String, String> roomDetail = roomConfig.get(roomType);
			Hashtable<String, Room> roomDataPerLevel = new Hashtable<String, Room>();
			for(int i=1; i<= Integer.parseInt(roomDetail.get("numberOfRooms")); i++) {
				String roomNo = "0" + roomDetail.get("level") + "-";
				if (i > 9) {
					roomNo += Integer.toString(i);
				}
				else {
					roomNo +=  "0" + Integer.toString(i);
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
			if (Integer.parseInt(roomDetail.get("numberOfRooms")) > 0) {
				roomTable.put(roomType, roomDataPerLevel);
			}
		}
	}
	
	/**
	 * A function to get the room configuration.
	 * @param roomConfigFilePath {String} the path to the room configuration file
	 * @return {Hashtable<String, Hashtable<String. String>} the room configuration 
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
					put("level", aLineArr[8].trim());
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
				
				//added this if statement
				if(roomDetail.getGuest()!=null) {
					if (roomDetail.getGuest().getName().contentEquals(name)) {
						return roomDetail.getGuest();
					}
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
	public void updateGuestDetailsByName(String name, String detailToUpdate, String newData) 
			throws GuestNotFoundException, GuestDetailUpdateFailureException {
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
	 * A function to get a room by its number
	 * @param roomNum {String} the room number
	 * @return {Room} the room object
	 * @throws {RoomNotFoundException} when room is not found in the hotel system
	 */
	public Room getRoomByNo(String roomNum) throws RoomNotFoundException{
		for (String roomType: this.roomTable.keySet()) {
			for (String roomNo: this.roomTable.get(roomType).keySet()) {
				if ((roomNo).equals(roomNum)) {
					return this.roomTable.get(roomType).get(roomNo);
				}
			}
		}
		throw new RoomNotFoundException();
	}

	/**
	 * A function to update room details.
	 * @param roomNo {String} the room number
	 * @param details {String} the room detail to update
	 * @param newData {newData} the new detail
	 * @throws {RoomNotFoundException} when room is not found in the hotel system
	 */
	public void updateRoomDetails(String roomNo, String details, String newData) 
			throws RoomNotFoundException, RoomTypeNotFoundException{
		Room room = this.getRoomByNo(roomNo);
		int delta=0;
		switch(details) {
		case "status":{
			if (newData.equals("under maintainance") | newData.equals("renovation")){
				delta = -1;
			}
			else if (newData.equals("vacant")) {
				delta = 1 ;
			}
			if (!newData.equals(room.getStatus())) {
				String roomType = this.getRoomTypeFromRoomNo(roomNo);
				this.getReservationSystem().shiftReservation(this.getNumberOfRoomsByRoomType(roomType), 
						roomType, delta);
				room.updateStatus(newData);
			}
			break;
		}
		case "price":{
			room.updateCost(newData);
			break;
		}
		case "bed type":{
			room.updateBedType(newData);
			break;
		}
		case "wifi":{
			room.updateWifi(newData);
			break;
		}
		case "view":{
			room.updateView(newData);
			break;
		}
		case "smoking":{
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
	 * A function to check for number of guests staying in a roomType for a given date range
	 * @param startDate {Date} Starting date
	 * @param endDate {Date} Ending date
	 * @param roomType {String} The room type you want to check for
	 * @return {Integer} The number of rooms that currently have guests
	 */
	private int checkHotelClash(java.util.Date startDate, java.util.Date endDate,String roomType) {
		int roomsClash=0;
		Hashtable<String, Room> tempTable=roomTable.get(roomType);
			
			//checks for date clashes with guests that have already check-in
			for(String key: tempTable.keySet()) {
				
				if(tempTable.get(key).getGuest()!=null) {
					Room tempRoom = tempTable.get(key);
					Guest tempGuest = tempRoom.getGuest();
					roomsClash=roomsClash+checkDateClash(startDate, endDate, 
							tempGuest.getStartDateOfStay(),tempGuest.getEndDateOfStay()); //if dates clash, minus 1 from available rooms of that roomType
				}
	
			}
			
			return roomsClash;
	}
	
	/**
	 * A function to check for number of guests staying in a roomType for a given date range
	 * @param startDate {Date} Starting date
	 * @param endDate {Date} Ending date
	 * @param roomType {String} The room type you want to check for
	 * @return {Integer} The number of rooms currently booked by reservation
	 */
	private int checkReservationClash(java.util.Date startDate, java.util.Date endDate, String roomType) {
		int roomsClash=0;
		
		ArrayList<Reservation> tempList = new ArrayList<Reservation>();
		ArrayList<String> reservationIdInDateRange = new ArrayList<String>();
		
		//checks for date clashes with guests in the reservation system
		tempList=reservationSystem.getRoomTypeReservation(roomType);
		
		
		//pass in start and end date of reservation
		for(int i=0; i<tempList.size(); i++){
			Reservation tempRes = tempList.get(i);
			if(!(reservationIdInDateRange.contains(tempRes.getReservationId()))) {
				//add new id to arraylist
				reservationIdInDateRange.add(tempRes.getReservationId());
				if(!tempRes.getStatus().contentEquals("checked-in")) {
							roomsClash += checkDateClash(startDate, endDate, 
									tempRes.getDateOfCheckIn(), tempRes.getDateOfCheckOut());
					}
				}
				
			}
		return roomsClash;
	}
		
	/**
	 * A function to check if there are any rooms of a particular room type available
	 * @param startDate {Date} Start date of new guest
	 * @param endDate {Date} End date of new guest
	 * @param roomType {String} roomType chosen by the new guest
	 * @return {boolean}, true if available, false if not available
	 * @throws RoomTypeNotFoundException
	 */
	public boolean checkRoomAvailability(java.util.Date startDate, java.util.Date endDate, String roomType) throws RoomTypeNotFoundException{
		//assume all rooms are available at the start
		if(roomTable.get(roomType)==null) {
			return false;
		}
		int roomsLeftForDate=this.getNumberOfRoomsByRoomType(roomType);
		//checkHotel method
		roomsLeftForDate=roomsLeftForDate-checkHotelClash(startDate,endDate,roomType)-checkReservationClash(startDate,endDate,roomType);
		if(roomsLeftForDate<=0) {
			return false; //not available
		}
		
		return true; //available
	
	}

	/**
	 * A function to check-in a guest who arrived at the hotel into a room chosen by the staff
	 * @param roomNo {String} roomNo of an empty room
	 * @param guest {Guest} object of the guest who wants to check-in
	 * @param roomType {String} roomType chosen by the guest 
	 * @return {boolean} true if successful, false if failure
	 * @throws RoomNotFoundException
	 */
	public boolean checkIn (Guest guest, String roomNo, String roomType) throws RoomNotFoundException {
		
		Hashtable<String, Room> tempRoomList = roomTable.get(roomType);

		Room tempRoom = tempRoomList.get(roomNo);
		
		if(tempRoom==null) {
			throw new RoomNotFoundException();
			}
		
		if(tempRoom.getStatus().equals("vacant")) {
			tempRoom.assignGuestToRoom(guest);
			tempRoom.updateStatus("occupied");
			
			return true;
		}
		else {
			return false;
		}
		
		
	}
	


	/**
	 * A function to check-in a guest with a reservation who arrived at the hotel into a room chosen by the staff
	 * @param roomNo {String} roomNo of an empty room
	 * @param guest {Guest} object of the guest who wants to check-in
	 * @param roomType {String} roomType chosen by the guest 
	 * @param reservation {Reservation} reservation object belonging to the guest
	 * @return {boolean} true if successful, false if failure
	 * @throws RoomNotFoundException
	 */
	public boolean checkIn(Guest guest, String roomNo, String roomType, Reservation reservation) throws RoomNotFoundException {
		
		Hashtable<String, Room> tempRoomList = roomTable.get(roomType);
		
		Room tempRoom = tempRoomList.get(roomNo);
		
		if(tempRoom==null) {
		throw new RoomNotFoundException();
		}
		if (reservation.getStatus().equals("waitlist")) {
			throw new RoomNotFoundException("Check in failed. The reservation is in waitlist");
		}
		if(tempRoom.getStatus().equals("vacant")) {
			
			tempRoom.assignGuestToRoom(guest);
			tempRoom.updateStatus("occupied");

			this.reservationSystem.updateAllReservationStatus(reservation.getReservationId(),"checked-in",roomType);

			return true;
		}
		else {
			return false;
		}
		
	}
	
	/**
	 * A function to make room service order.
	 * @param roomNo {String} the room number of the guest
	 * @param menu {Menu} the room service menu
	 * @param orderMap {Hashtable<String, String>} the ordered items and quantity from the menu
	 * @throws GuestNotFoundException 
	 * @throws {FoodNotOnMenuException} when food ordered is not on the menu 
	 * @throws {RoomNotFoundException} when room number is not found in the hotel
	 */
	public void makeRoomServiceOrder(String roomNo, Menu menu, 
			Hashtable<String, String> orderMap) throws FoodNotOnMenuException, 
			RoomNotFoundException, GuestNotFoundException {
		try {
			this.getRoomByNo(roomNo).getGuest().makeOrder(menu, orderMap);
		} catch (NullPointerException e){
			throw new GuestNotFoundException();
		}
	}
	
	/**
	 * A function to get the room type from a room number.
	 * @param roomNo {String} the room number
	 * @return {String} the room type
	 * @throws {RoomNotFoundException} when the room number is not found
	 */
	private String getRoomTypeFromRoomNo(String roomNo) throws RoomNotFoundException {
		String theRoomType = null;
		for (String roomType: this.roomTable.keySet()) {
			if (this.roomTable.get(roomType).containsKey(roomNo)) {
				theRoomType = roomType;
				break;
			}
		}
		if (theRoomType == null) {
			throw new RoomNotFoundException();
		}
		return theRoomType;
	}
	
	/**
	 * A function to perform check out in a hotel.
	 * @param roomNo {String} the room number
	 * @param guestName {String} the guest name
	 * @return {boolean} represents the success of the check out process
	 * @throws RoomTypeNotFoundException when room type is not in the hotel
	 * @throws {GuestNotFoundException} when guest is not in the hotel
	 * @throws {RoomNotFoundException} when room to be checked out is not found
	 */
	public boolean checkOut(String roomNo, String guestName) throws 
	RoomNotFoundException, GuestNotFoundException, RoomTypeNotFoundException {
		Room guestRoom = this.getRoomByNo(roomNo);
		Guest guestToCheckOut = guestRoom.getGuest();
		try {
			if (guestToCheckOut.getName().equals(guestName)) {
				String roomType = this.getRoomTypeFromRoomNo(roomNo);
				guestToCheckOut.makePayment(roomType, guestRoom.getRoomCost());
				int numberOfRooms = this.getNumberOfRoomsByRoomType(roomType);
				guestRoom.removeGuest();
				if (guestToCheckOut.getEndDateOfStay().compareTo(this.currentDate) > 0) {
					try {
						this.getReservationSystem().removeReservationByGuestAndRoomType(
								guestToCheckOut, roomType, numberOfRooms);
					} catch (ReservationNotFoundException e) {
						System.out.println(e.getMessage());
					}
				}
				return true;
			}
			else {
				return false;
			}
		} catch (NullPointerException e) {
			throw new GuestNotFoundException();
		}
	}

	/**
	 * A function to get the room status to room number list table.
	 * @param sort {boolean} whether to get sorted room number list
	 * @return {<Hashtable<String, ArrayList<Room>>>} the room status to room number list table
	 */
	@SuppressWarnings("serial")
	public Hashtable<String, ArrayList<String>> getRoomStatusToRoomNoListTable(boolean sort) {
		Hashtable<String, ArrayList<String>> roomStatusToRoomNoListTable = 
				new Hashtable<String, ArrayList<String>>();
		for (String roomType: this.roomTable.keySet()) {
			for(String roomNo: this.roomTable.get(roomType).keySet()) {
				Room room = this.roomTable.get(roomType).get(roomNo);
				String roomStatus = room.getStatus();
				if (roomStatusToRoomNoListTable.containsKey(roomStatus)) {
					roomStatusToRoomNoListTable.get(roomStatus).add(roomNo);
				}
				else {
					roomStatusToRoomNoListTable.put(roomStatus, 
							new ArrayList<String>() {{add(roomNo);}});
				}
			}
		}
		if (sort) {
			for (String roomStatus: roomStatusToRoomNoListTable.keySet()) {
				roomStatusToRoomNoListTable.replace(roomStatus, 
						Hotel.getSortedRoomNoList(roomStatusToRoomNoListTable.get(roomStatus)));
			}
		}
		return roomStatusToRoomNoListTable;
	}

	/**
	 * A function to get room type to vacant room number list table.
	 * @param sort {boolean} whether to get sorted room number list
	 * @return {Hashtable<String, ArrayList<String>>} the room type to vacant room number list table
	 */
	@SuppressWarnings("serial")
	public Hashtable<String, ArrayList<String>> getRoomTypeToVacantRoomNoListTable(boolean sort) {
		Hashtable<String, ArrayList<String>> roomTypeToVacantRoomNoListTable = 
				new Hashtable<String, ArrayList<String>>();
		for (String roomType: this.roomTable.keySet()) {
			for(String roomNo: this.roomTable.get(roomType).keySet()) {
				Room room = this.roomTable.get(roomType).get(roomNo);
				if (room.getStatus().equals("vacant")) {
					if (roomTypeToVacantRoomNoListTable.containsKey(roomType)) {
						roomTypeToVacantRoomNoListTable.get(roomType).add(roomNo);
					}
					else {
						roomTypeToVacantRoomNoListTable.put(roomType, new ArrayList<String>() {{
							add(roomNo);
						}});
					}
				}
			}
		}
		if (sort) {
			for (String roomType: roomTypeToVacantRoomNoListTable.keySet()) {
				roomTypeToVacantRoomNoListTable.replace(roomType, 
						Hotel.getSortedRoomNoList(roomTypeToVacantRoomNoListTable.get(roomType)));
			}
		}
		return roomTypeToVacantRoomNoListTable;		
	}
	
	/**
	 * A static method to get sorted list of room number.
	 * @param {roomNoListToSort} the list of room number to sort
	 * @return {ArrayList<String>} the sorted list of room number
	 */
	public static ArrayList<String> getSortedRoomNoList(ArrayList<String> roomNoListToSort) {
		ArrayList<String> sortedRoomNoList = new ArrayList<String>();
		for (String roomNo: roomNoListToSort) {
			if (sortedRoomNoList.size() == 0) {
				sortedRoomNoList.add(roomNo);
			}
			else {
				boolean roomAdded = false;
				for (int index=0; index<sortedRoomNoList.size(); index++) {
					String levelNo = roomNo.split("-")[0];
					String explicitRoomNo = roomNo.split("-")[1];
					String levelNoOfRoomInSortedList = sortedRoomNoList.get(index).split("-")[0];
					String explicitRoomNoInSortedList = sortedRoomNoList.get(index).split("-")[1];
					if (Integer.parseInt(levelNoOfRoomInSortedList) > Integer.parseInt(levelNo) || 
							(Integer.parseInt(levelNoOfRoomInSortedList) == Integer.parseInt(levelNo) && 
							Integer.parseInt(explicitRoomNoInSortedList) > 
							Integer.parseInt(explicitRoomNo))) {
							sortedRoomNoList.add(index, roomNo);
							roomAdded = true;
							break;
					}
				}
				if (!roomAdded) {
					sortedRoomNoList.add(roomNo);
				}
			}
		}
		return sortedRoomNoList;
	}
  
	 /**
	 * A function to update the number of available room in a certain room type
	 * @param room_type {String} the room type
	 * @param num_room {int} this is the number of available room of the corresponding room type
	 * @param delta {int} the number of rooms that the status should be change (1 if you are changing waitlist to confirmed, -1 otherwise)
	 * @return {void} 
	 * @throws RoomTypeNotFoundException 
	 */
	public void updateReservation(String roomType, int delta) 
			throws RoomTypeNotFoundException {
		this.getReservationSystem().shiftReservation(this.getNumberOfRoomsByRoomType(roomType), 
				roomType, delta);
	}

	/**
	 * A function to check if roomType exists in the hotel
	 * @param roomType {String} the roomType you want to check
	 * @return {boolean} true if roomType exists, false if roomType does not exist
	 */
	public boolean doesRoomTypeExists(String roomType) {
		for(int i=0;i<getAvailableRoomTypes().size();i++) {
			if(roomType.equals(getAvailableRoomTypes().get(i))) {
				return true;
			}
		}
		return false;

	}
	
	/**
	 * A function to get the number of available rooms by its type.
	 * @param roomType {String} the room type
	 * @return {int} the number of rooms
	 * @throws {RoomTypeNotFoundException} when the room type is not in the hotel system
	 */
	public int getNumberOfRoomsByRoomType(String aRoomType) throws RoomTypeNotFoundException {
		int numberOfRooms = 0;
		for (String roomType: this.roomTable.keySet()) {
			if (roomType.equals(aRoomType)) {
				for (String roomNo: this.roomTable.get(roomType).keySet()) {
					Room room = this.roomTable.get(roomType).get(roomNo);
					if (room.getStatus() == "vacant" || room.getStatus() == "occupied") {
						numberOfRooms += 1;
					}
				}
			}
		}
		if (numberOfRooms > 0) {
			return numberOfRooms;
		}
		else {
			throw new RoomTypeNotFoundException();
		}
	}
	
	/**
	 * A function to update today reservation status with current time.
	 */
	public void updateTodayReservationStatusWithCurrentTime() {
		this.currentDate = new Date();
		long checkInTimeTolerance = 1000*60*60;
		@SuppressWarnings("deprecation")
		long currentTime = this.currentDate.getTime() - 
				new Date(this.currentDate.toLocaleString().split(",")[0]).getTime();
		long checkInTime = this.checkInTimeInMilliSeconds;
		if (currentTime > checkInTime + checkInTimeTolerance) {
			this.reservationSystem.expireAllReservationsOnDate(currentDate);
		}
	}
	
	/**
	 * A function to get the hotel check in date and time.
	 * @return {Date} the check in date and time
	 */
	@SuppressWarnings("deprecation")
	public Date getCheckInDate() {
		return new Date(new Date(new Date().toLocaleString().split(",")[0]).getTime() + 
				this.checkInTimeInMilliSeconds);
	}
	
	/**
	 * A function to get the hotel check out time
	 * @return {int} the time in milliseconds
	 */
	public int getCheckOutTimeInMilliSeconds() {
		return this.checkOutTimeInMilliSeconds;
	}
}
