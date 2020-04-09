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

