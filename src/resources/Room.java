package resources;
/**
 * A class to represent a room in a hotel.
 */
public class Room {
	Guest guest;
	String roomNo;
	String status;
	
	Room(String roomNo) {
		this.roomNo = roomNo;
		this.status = "";
	}
	void updateStatus(String status) {
		this.status = status;
	}
}
