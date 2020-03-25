package resources;

/**
 * A class to represent a room in a hotel.
 */
public class Room {
	private String roomNo;
	private String bedType;
	private Boolean smoking;
	private Boolean wifiEnabled;
	private double roomCost;
	private String status;
	Room(String roomNo, String bedType, Boolean smoking, Boolean wifiEnabled, double roomCost, String status) {
		this.roomNo = roomNo;
		this.bedType = bedType;
		this.smoking = smoking;
		this.wifiEnabled = wifiEnabled;
		this.roomCost = roomCost;
		this.status = status;
	}
	
	/**
	 * A method to get the vacancy status of the room.
	 * @return {String} status of the room
	 */
	public String getStatus() {
		return this.status;
	}
	
	/**
	 * A method to get the room cost.
	 * @return {double} the room cost
	 */
	public double getRoomCost() {
		return this.roomCost;
	}
	
	/**
	 * A function to set the vacancy status of the room.
	 * @param {String} the new status of the room
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}
