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
	
	void updateCost(String cost) {
		this.roomCost = Double.parseDouble(cost);
	}
	
	void updateBedType(String bed_type) {
		this.bedType = bed_type;
	}
	
	void updateWifi(String wifi) {
		this.wifi = wifi;
	}
	
	void updateSmoking(String smoking) {
		this.smoking = smoking == "yes";
	}
	
	void updateView(String view) {
		this.view = view;
	}
	
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
	Guest getGuest() {
		return this.guest;
	}
}
