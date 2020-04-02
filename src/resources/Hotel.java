package resources;
import java.util.Hashtable;
import resources.ReservationSystem;
import resources.Room;
public class Hotel {
	//maybe add a constant totalroom for easier updating(if report requires futrure planning)
	//maybe include a enum for the room names/number to more check availability with getStatus()
	// {"roomType": {"roomNo": Room()}}
	Hashtable<String, Hashtable<String, Room>> roomTable = new Hashtable<String, Hashtable<String, Room>>();
	private ReservationSystem reservationSystem;
	Hotel() {
		for (int i=1; i<5; i++) {
			for(int j=1; j<13; j++) {
				String roomType;
				String roomNo;
				switch(i) {
					case 1: roomType="single";break;
					case 2: roomType="double";break;
					// add case 3 and 4
					default: roomType="undefined";
				}
				if (j > 9) {
					roomNo = "0" + i + "-" + j;
				}
				else {
					roomNo = "0" + i + "-" + "0" + j;
				}
				roomTable.put(roomType, new Hashtable<String, Room>() {{ put(roomNo, new Room(roomNo)); }} );	
			}
		}
		this.reservationSystem = new ReservationSystem();
	}
	public boolean reassignRoomByGuestName(String name, String newRoomNumber) {
		checkIfGuestInsideHotel();
		checkIfNewRoomNumberIsVacant();
		RemoveGuestFromRoom();
		AssignGuestToRoom();
		return true;
	}
	public boolean getGuestDetails(String name) {
		checkIfGuestInsideHotel();
		PrintGuestDetails();
	}
	public boolean createReservation(Guest guest, java.util.Date check_in_date, java.util.Date check_out_date, String roomType) {
		this.reservationSystem.add(guest, check_in_date, check_out_date, roomType);
	}
	
	public boolean checkRoomAvailability(java.util.Date start_date, java.util.Date end_date, String roomType) {
		return true;
	}
	public boolean checkIn(Guest guest, String roomType) {
		AssignGuestToRoom();
		this.reservationSystem.addReservation(CreateReservationFromGuest(guest));
		return true;
	}
	public void updateRoomStatus(String roomNo, String status) {
		// add switch case for valid status, then call room method to update its status
	}
	public void printReport() {
		//print rooms availability
	}
}

