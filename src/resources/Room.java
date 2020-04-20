package resources;

import java.io.Serializable;

/**
 * A class to represent a room in a hotel.
 */
@SuppressWarnings("serial")
public class Room implements Serializable{
	private Guest guest;
	private String roomNo;
	private String status;
	private double roomCost;
	private String bedType; 
	private String wifi;
	private String view;
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
	public Room(String roomNo, double roomCost, String bedType, String wifi, String view, String smoking) {
		this.roomNo = roomNo;
		this.status = "vacant";
		this.roomCost = roomCost;
		this.bedType = bedType;
		this.wifi = wifi;
		this.view = view;
		this.smoking = smoking.equals("yes");
	}

	/**
	 * A function to update the status of the room
	 * @param status {String} the new status of the room
	 */
	public void updateStatus(String status) {
		this.status = status;
	}
	
	/**
	 * A function to update the cost of the room
	 * @param cost {String} the new cost of the room
	 */
	public void updateCost(String cost) {
		this.roomCost = Double.parseDouble(cost);
	}
	
	/**
	 * A function to update the bedType of the room
	 * @param bed_type {String} the new bedType of the room
	 */
	public void updateBedType(String bed_type) {
		this.bedType = bed_type;
	}
	
	/**
	 * A function to update the wifi status of the room
	 * @param wifi {String} the new wifi of the room
	 */
	public void updateWifi(String wifi) {
		this.wifi = wifi;
	}
	
	/**
	 * A function to update the smoking status of the room
	 * @param smoking {String} the new smoking of the room
	 */
	public void updateSmoking(String smoking) {
		this.smoking = smoking.contentEquals("yes");
	}
	
	/**
	 * A function to update the view of the room
	 * @param view {String} the new view of the room
	 */
	public void updateView(String view) {
		this.view = view;
	}
	
	/**
	 * A function to print the room details
	 */
	public void printRoom() {
		System.out.println(""
				+"Room Number: "+this.roomNo+"\n"
				+"Room Status: "+this.status+"\n"
				+ "Room Price: "+ this.roomCost+"\n"
				+"Bed Type: "+ this.bedType+"\n"
				+"Wifi Availability: "+this.wifi+"\n"
				+"Room View: "+this.view+"\n"
				+"Smoking? "+this.smoking+"\n");
	}

	/**
	 * A function to get the guest of the room
	 * @return {Guest} the guest object
	 */
	public Guest getGuest() {
		return this.guest;
	}
	
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
	public String getStatus(){
		return status;
	}
	
	/**
	 * A function to get the room cost.
	 * @return {double} the room cost
	 */
	public double getRoomCost() {
		return this.roomCost;
	}
	
	/**
	 * A function to remove a guest from the room.
	 */
	public void removeGuest() {
		this.guest = null;
		this.status = "vacant";
	}
}
