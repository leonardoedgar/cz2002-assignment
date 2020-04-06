package resources;

import java.util.Hashtable;
import resources.Guest;
import resources.Reservation;

public class ReservationSystem {
	//{"Date": {"roomType": Reservation}}
	
	Hashtable<java.util.Date, Hashtable<String, Reservation>> reservationTable = new Hashtable<java.util.Date, Hashtable<String, Reservation>>();
	public boolean addReservation(Reservation reservation) {
		// Check if date is inside reservationTable
		// If yes, check  by checking the number of reservations available. If < 12, put confirm, if >= 12, put wait-list 
		// If not, put confirm status for Reservation object and put the object inside reservationTable 
		return true;
	}
	public boolean updateReservation(Reservation reservation) {
		// Get all reservations available, check if any modifications on old reservations. If so, check feasibility.
		// For new reservations that was not made before, check feasibility based on database
		return true;
	}
	public boolean removeReservation(Reservation reservation) {
		// Take reservation id and remove from hashtable
		return true;
	}
	public void printReservation() {
		// Print all reservations from the hashtable
	}
}
