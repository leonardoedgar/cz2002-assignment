package resources;
//added import
import java.util.Date;
import java.util.ArrayList;

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
	public Guest getGuest() {
		return this.guest;
	}
	
	//added this method
	/**
	 * A function to assign a guest to the room
	 * @param guest {Guest} the guest object assigned to this room
	 */
	public void assignGuestToRoom(Guest guest) {
		this.guest=guest;
	}
	/**
	 * A function to check the status of the room
	 * @return {String} String of status of the room
	 */
	public String getStatus() {
		return status;
	}
}
