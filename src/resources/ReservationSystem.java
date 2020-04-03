package resources;
import java.util.Date;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Map.Entry;
import resources.Guest;
import java.util.Collections;
import java.util.Comparator;
import resources.Reservation;
import exception.ReservationNotFoundException;
import exception.DuplicateReservationFoundException;

public class ReservationSystem {
	//{"DD MM YYYY": {"roomType": Reservation}}
	
	private Hashtable<String, Hashtable<String, ArrayList<Reservation>>> reservationTable = 
			new Hashtable<String, Hashtable<String, ArrayList<Reservation>>>();
	public void addReservation(Reservation reservation) throws DuplicateReservationFoundException {
		if(!this.doesDuplicateReservationIdExist(reservation.getReservationId())) {
			this.addNewReservation(reservation);
		}
		else {
			throw new DuplicateReservationFoundException();
		}
	}
	public void updateReservation(Reservation reservation) throws ReservationNotFoundException {
		if (this.doesDuplicateReservationIdExist(reservation.getReservationId())) {
			this.updatePastReservation(reservation);
			this.addNewReservation(reservation);
		}
		else {
			throw new ReservationNotFoundException();
		}
	}
	public void removeReservation(String reservationId) throws ReservationNotFoundException {
		if (this.doesDuplicateReservationIdExist(reservationId)) {
			for(String date: this.reservationTable.keySet()) {
				for (String roomType: this.reservationTable.get(date).keySet()) {
					ArrayList<Reservation> reservationList = this.reservationTable.get(date).get(roomType);
					for (int i=0; i<reservationList.size(); i++) {
						if(reservationList.get(i).getReservationId() == reservationId) {
							this.removeReservationFromArrayByIndex(reservationList, i, date, roomType);
						}
					}
				}
			}
		}
		else {
			throw new ReservationNotFoundException();
		}
	}
	private void removeReservationFromArrayByIndex(ArrayList<Reservation> reservationList, 
			int index, String date, String roomType) {
		if (reservationList.size() > 12) {
			reservationList.get(12).updateStatus("confirmed");
		}
		reservationList.remove(index);
		if(reservationList.size() == 0 ) {
			this.reservationTable.get(date).remove(roomType);
			if (reservationTable.get(date).isEmpty()) {
				this.reservationTable.remove(date);
			}
		}
	}
	public void printReservation() {
		ArrayList<String> sortedDateReservationArr = this.getSortedDateOfReservation();
		for(int i=0; i<sortedDateReservationArr.size(); i++) {
			String date = sortedDateReservationArr.get(i);
			System.out.println("==============================================");
			System.out.println("DD/MM/YYYY: " + date);
			for (String roomType: this.reservationTable.get(date).keySet()) {
				for (Reservation reservation: this.reservationTable.get(date).get(roomType)) {
					System.out.println("----------------------------------------------");
					reservation.printDetails();
					System.out.println("----------------------------------------------");
				}
			}
			System.out.println("==============================================");
		}
	}
	
	private ArrayList<String> getSortedDateOfReservation() {
		ArrayList<String> dateArr = new ArrayList<String>();
		for (String date: this.reservationTable.keySet()) {
			dateArr.add(date);
		}
		Collections.sort(dateArr, new Comparator<String>() {
			  public int compare(String date1, String date2) {
			    return new Date(date1).compareTo(new Date(date2));
			  }
		});
		return dateArr;
	}
	private void addNewReservation(Reservation reservation) {
		reservation.updateStatus("confirmed");
		for(
				Date reserveDate=reservation.getDateOfCheckIn(); 
				reservation.getDateOfCheckOut().compareTo(reserveDate)>0;
				reserveDate=new Date(reserveDate.getTime() + 60*60*24*1000)
		) {
			try {
				ArrayList<Reservation> reservationList = 
						this.getReservationsOnSameDateAndRoomType(reserveDate, reservation.getRoomType());
				boolean reservationUpdated = false;
				for (int i=0; i<reservationList.size(); i++) {
					if(reservationList.get(i).getReservationId() == reservation.getReservationId()) {
						reservationUpdated = true;
					}
				}
				if (!reservationUpdated) {
					Reservation clonedReservation = Reservation.copy(reservation);
					if(reservationList.size() >= 12) {
						clonedReservation.updateStatus("waitlist");
					}
					reservationList.add(clonedReservation);
				}
				
			}
			catch (ReservationNotFoundException e1) {
				try {
					this.getReservationOnSameDate(reserveDate).put(
							reservation.getRoomType(), 
							new ArrayList<Reservation>() {{add(reservation);}});
				}
				catch (ReservationNotFoundException e2) {
					this.reservationTable.put(
							ReservationSystem.getFormattedDate(reserveDate), 
							new Hashtable<String, ArrayList<Reservation>>() {{
								put(
										reservation.getRoomType(), 
										new ArrayList<Reservation>() {{add(reservation);}}
								);}}
					);
				}
			}
		}
	}
	private void updatePastReservation(Reservation reservation) {
		for(String date: this.reservationTable.keySet()) {
			for (String roomType: this.reservationTable.get(date).keySet()) {
				ArrayList<Reservation> reservationList = 
						this.reservationTable.get(date).get(roomType);
				for (int i=0; i<reservationList.size();i++) {
					if (reservationList.get(i).getReservationId() == 
							reservation.getReservationId()) {
						if(reservationList.get(i).getDateOfCheckIn() != 
								reservation.getDateOfCheckIn() || 
								reservationList.get(i).getDateOfCheckOut() != 
								reservation.getDateOfCheckOut()) {
							Date reservationStartDate = reservation.getDateOfCheckIn();
							Date reservationEndDate = new Date(
									reservation.getDateOfCheckOut().getTime() - 24*60*60*1000);
							Date currentDate = new Date(date);
							if (reservationStartDate.compareTo(currentDate)*
									currentDate.compareTo(reservationEndDate) >= 0) {
								if(reservation.getRoomType() == reservationList.get(i).getRoomType()) {
									Reservation newReservation = Reservation.copy(reservation);
									newReservation.updateStatus(reservationList.get(i).getStatus());
									reservationList.set(i, newReservation);
								}
								else {
									this.removeReservationFromArrayByIndex(reservationList, i, 
										date, roomType);
								}
							}
							else {
								this.removeReservationFromArrayByIndex(reservationList, i, 
									date, roomType);
							}
						}
						else if (reservationList.get(i).getRoomType() != reservation.getRoomType()) {
							this.removeReservationFromArrayByIndex(reservationList, i, 
								date, roomType);
						}
						else {
							reservation.updateStatus(reservationList.get(i).getStatus());
							reservationList.set(i, reservation);
						}
					}
				}
			}
		}
	}
	private boolean doesReservationOnSameDateExist(Date date) {
		return this.reservationTable.containsKey(ReservationSystem.getFormattedDate(date));
	}
	
	private Hashtable<String, ArrayList<Reservation>> getReservationOnSameDate(Date date) throws ReservationNotFoundException {
		if (this.doesReservationOnSameDateExist(date)) {
			return this.reservationTable.get(ReservationSystem.getFormattedDate(date));
		}
		throw new ReservationNotFoundException(); 
	}
	
	private boolean doesReservationOnSameDateAndRoomTypeExist(Date date, String roomType) throws ReservationNotFoundException {
		if (this.doesReservationOnSameDateExist(date)) {
			return this.getReservationOnSameDate(date).containsKey(roomType);
		}
		return false;
	}
	
	private ArrayList<Reservation> getReservationsOnSameDateAndRoomType(Date date, String roomType) throws ReservationNotFoundException {
		if (this.doesReservationOnSameDateAndRoomTypeExist(date, roomType)) {
			return this.getReservationOnSameDate(date).get(roomType);
		}
		throw new ReservationNotFoundException();
	}
	
	private static String getFormattedDate(Date date) {
		return date.toLocaleString().split(",")[0];
	}
	
	private boolean doesDuplicateReservationIdExist(String reservationId) {
		for(String date: this.reservationTable.keySet()) {
			for (String roomType: this.reservationTable.get(date).keySet()) {
				for (Reservation reservation: this.reservationTable.get(date).get(roomType)) {
					if (reservation.getReservationId() == reservationId) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
