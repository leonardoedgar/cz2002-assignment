package resources;

/**
 * A class to represent a room in a hotel.
 */
public class Room {
	private Guest guest;
	private String roomNo;
	private String status;
	private double roomCost;
	private String bedType; 
	private String wifi;
	public String view;
	private boolean smoking;
	
	/**
	 * A class constructor to create a room.
	 * @param roomNo {String} the room number
	 * @param roomCost {double} the room cost
	 * @param bedType {String} the bed type
	 * @param wifi {String} the wifi availability
	 * @param view {String} the view from the room
	 * @param smoking {String} the smoking restriction
	 */
	Room(String roomNo, double roomCost, String bedType, String wifi, String view, String smoking) {
		this.roomNo = roomNo;
		this.status = "vacant";
		this.roomCost = roomCost;
		this.bedType = bedType;
		this.wifi = wifi;
		this.view = view;
		this.smoking = smoking == "yes";
	}

	/**
	 * A function to update the status of the room
	 * @param status {String} the new status of the room
	 */
	void updateStatus(String status) {
		this.status = status;
	}

	/**
	 * A function to get the guest of the room
	 * @return {Guest} the guest object
	 */
	Guest getGuest() {
		return this.guest;
	}
}
