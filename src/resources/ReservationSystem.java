package resources;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import resources.Reservation;
import exception.ReservationNotFoundException;
import exception.DuplicateReservationFoundException;
import exception.IdGenerationFailedException;

/**
 * A class to represent a reservation system.
 */
public class ReservationSystem {
	//{"DD MM YYYY": {"roomType": Reservation}}
	private ConcurrentHashMap<String, ConcurrentHashMap<String, ArrayList<Reservation>>> reservationTable;
	
	ReservationSystem() {
		this.reservationTable = 
				new ConcurrentHashMap<String, ConcurrentHashMap<String, ArrayList<Reservation>>>();
	}
	/**
	 * A function to generate a new random alphanumeric ReservationId of length 10
	 * @return {String} the new ReservationId
	 */
	public String generateNewId() throws IdGenerationFailedException {
		String validIdCharacters="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		int idLength=10;
		int maxtry=10;
		int noOfTry=0;
		StringBuilder newReservationId = new StringBuilder(idLength);
		
		do {
		for(int i=0;i<idLength;i++) {
			if(noOfTry>=maxtry) {
				throw new IdGenerationFailedException();
			}
			int index = (int) (Math.random()*validIdCharacters.length());
			newReservationId.append(validIdCharacters.charAt(index));
		}
		noOfTry++;
		}while(doesDuplicateReservationIdExist(newReservationId.toString()));
		
		return newReservationId.toString();
	}
	/**
	 * A function to update the reservation status of all reservation objects with the corresponding reservationId
	 * @param reservationId {String} the reservationId
	 * @param updatedStatus {String} the new status of the reservation
	 */
	public void updateAllReservationStatus(String reservationId, String updatedStatus, String roomType) {
		for(String date: this.reservationTable.keySet()) {
			if(!(this.reservationTable.get(date).get(roomType)==null)) {
			for(Reservation reservation: this.reservationTable.get(date).get(roomType)) {
				if(reservation.getReservationId().equals(reservationId)) {
					reservation.updateStatus(updatedStatus);
				}
			}
			}
		}
	}

	/**
	 * A function to add a reservation.
	 * @param reservation { Reservation} the reservation to add
	 * @throws {DuplicateReservationFoundException} exception when duplicate reservation is found
	 */
	public void addReservation(Reservation reservation, int numberOfRooms) throws DuplicateReservationFoundException {
		if(!this.doesDuplicateReservationIdExist(reservation.getReservationId())) {
			this.addNewReservation(reservation, numberOfRooms);
		}
		else {
			throw new DuplicateReservationFoundException();
		}
	}
	
	/**
	 * A function to update a reservation.
	 * @param reservation {Reservation} the reservation to update to
	 * @throws {ReservationNotFoundException} exception when the old reservation is not found
	 */
	public void updateReservation(Reservation reservation, int numberOfRooms) throws ReservationNotFoundException {
		if (this.doesDuplicateReservationIdExist(reservation.getReservationId())) {
			this.updatePastReservation(reservation,numberOfRooms);
			this.addNewReservation(reservation,numberOfRooms);
		}
		else {
			throw new ReservationNotFoundException();
		}
	}
	
	/**
	 * A function to remove a reservation
	 * @param reservationId {String} the reservation id to remove
	 * @throws {ReservationNotFoundException} exception when reservation to remove is not found
	 */
	public void removeReservation(String reservationId, int numberOfRooms) throws ReservationNotFoundException {
		if (this.doesDuplicateReservationIdExist(reservationId)) {
			for(String date: this.reservationTable.keySet()) {
				for (String roomType: this.reservationTable.get(date).keySet()) {
					ArrayList<Reservation> reservationList = this.reservationTable.get(date).get(roomType);
					for (int i=0; i<reservationList.size(); i++) {
						if(reservationList.get(i).getReservationId().equals(reservationId)) {
							this.removeReservationFromArrayByIndex(reservationList, i, date, roomType, numberOfRooms);
						}
					}
				}
			}
		}
		else {
			throw new ReservationNotFoundException();
		}
	}
	
	/**
	 * this method will shift the reservation list to have add more "waitlist" (delta =-1) or add more "confirmed" (delta = 1) depending on delta
	 * @param num_room number of available rooms in certain roomtype (updated information)
	 * @param roomtype 
	 * @param delta delta is the number of rooms to be changed to "vacant" or "under maintainance"
	 */
	public void shiftReservation(int numRoom,String roomtype, int delta) {
		int num_room = numRoom + delta;
		for(String date: this.reservationTable.keySet()) {
			int counter = 1;
			if(!(this.reservationTable.get(date).get(roomtype)==null)) {
				for (Reservation reserve: this.reservationTable.get(date).get(roomtype)) {
  //				only set to waitlist if the person is between the current number of room and the prev number of room 
					if (counter > num_room && counter <=num_room-delta && delta == -1) {
						reserve.updateStatus("waitlist");
					}
					else if (counter <= num_room && counter > num_room-delta && delta == 1) {
						reserve.updateStatus("confirmed");
					}
					counter ++;
				}
			}
		}
	}
	
	/**
	 * A function to remove a reservation by index from an array.
	 * @param reservationList {ArrayList<Reservation>} the reservation array
	 * @param index {int} the index to remove
	 * @param date {Date} the date of the reservation to remove
	 * @param roomType {String} the room type
	 */
	private void removeReservationFromArrayByIndex(ArrayList<Reservation> reservationList, 
			int index, String date, String roomType, int numberOfRooms) {
		if (reservationList.size() > numberOfRooms) {
			reservationList.get(numberOfRooms).updateStatus("confirmed");
		}
		reservationList.remove(index);
		if(reservationList.size() == 0 ) {
			this.reservationTable.get(date).remove(roomType);
			if (reservationTable.get(date).isEmpty()) {
				this.reservationTable.remove(date);
			}
		}
	}
	
	/**
	 * A function to print a reservation.
	 */
	public void printReservation() {
		ArrayList<String> sortedDateReservationArr = this.getSortedDateOfReservation();
		if (sortedDateReservationArr.size() == 0) {
			System.out.println(""
					+ "|=========================|\n"
					+ "|      NO RESERVATIONS    |\n"
					+ "|=========================|\n");
		}
		else {
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
	}
	
	/**
	 * A function to get a sorted reservation by date.
	 * @return {ArrayList<String>} an array of sorted reservation
	 */
	private ArrayList<String> getSortedDateOfReservation() {
		ArrayList<String> dateArr = new ArrayList<String>();
		for (String date: this.reservationTable.keySet()) {
			dateArr.add(date);
		}
		Collections.sort(dateArr, new Comparator<String>() {
			  @SuppressWarnings("deprecation")
			public int compare(String date1, String date2) {
			    return new Date(date1).compareTo(new Date(date2));
			  }
		});
		return dateArr;
	}
	
	/**
	 * A function to add a new reservation.
	 * @param reservation {Reservation} the new reservation to add
	 */
	@SuppressWarnings("serial")
	private void addNewReservation(Reservation reservation, int numberOfRooms) {
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
					if(reservationList.get(i).getReservationId().equals(reservation.getReservationId())) {
						reservationUpdated = true;
					}
				}
				if (!reservationUpdated) {
					Reservation clonedReservation = Reservation.copy(reservation);
					if(reservationList.size() >= numberOfRooms) {
						clonedReservation.updateStatus("waitlist");
					}
					reservationList.add(clonedReservation);
				}
				
			}
			catch (ReservationNotFoundException e1) {
				Reservation clonedReservation = Reservation.copy(reservation);
				try {
					if(numberOfRooms==0) {
						clonedReservation.updateStatus("waitlist");
					}
					this.getReservationOnSameDate(reserveDate).put(
							reservation.getRoomType(), 
							new ArrayList<Reservation>() {{add(clonedReservation);}});
				}
				catch (ReservationNotFoundException e2) {
					this.reservationTable.put(
							ReservationSystem.getFormattedDate(reserveDate), 
							new ConcurrentHashMap<String, ArrayList<Reservation>>() {{
								put(
										reservation.getRoomType(), 
										new ArrayList<Reservation>() {{add(clonedReservation);}}
								);}}
					);
				}
			}
		}
	}
	
	/**
	 * A function to update the past reservation with a new one.
	 * @param reservation {Reservation} the new reservation to update to
	 */
	private void updatePastReservation(Reservation reservation, int numberOfRooms) {
		for(String date: this.reservationTable.keySet()) {
			for (String roomType: this.reservationTable.get(date).keySet()) {
				ArrayList<Reservation> reservationList = 
						this.reservationTable.get(date).get(roomType);
				for (int i=0; i<reservationList.size();i++) {
					if (reservationList.get(i).getReservationId().equals( 
							reservation.getReservationId())) {
						if(!reservationList.get(i).getDateOfCheckIn().equals(reservation.getDateOfCheckIn()) || 
								!reservationList.get(i).getDateOfCheckOut().equals(reservation.getDateOfCheckOut())) {
							Date reservationStartDate = reservation.getDateOfCheckIn();
							Date reservationEndDate = new Date(
									reservation.getDateOfCheckOut().getTime() - 24*60*60*1000);
							@SuppressWarnings("deprecation")
							Date currentDate = new Date(date);
							if (reservationStartDate.compareTo(currentDate)*
									currentDate.compareTo(reservationEndDate) >= 0) {
								if(reservation.getRoomType().equals(reservationList.get(i).getRoomType())) {
									Reservation newReservation = Reservation.copy(reservation);
									newReservation.updateStatus(reservationList.get(i).getStatus());
									reservationList.set(i, newReservation);
								}
								else {
									this.removeReservationFromArrayByIndex(reservationList, i, 
										date, roomType,numberOfRooms);
								}
							}
							else {
								this.removeReservationFromArrayByIndex(reservationList, i, 
									date, roomType, numberOfRooms);
							}
						}
						else if (reservationList.get(i).getRoomType().equals(reservation.getRoomType())) {
							this.removeReservationFromArrayByIndex(reservationList, i, 
								date, roomType, numberOfRooms);
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
	
	/**
	 * A function to know whether reservation on a particular date exists.
	 * @param date {Date} the date to check with.
	 * @return {boolean} the existence of the reservation on the same date
	 */
	private boolean doesReservationOnSameDateExist(Date date) {
		return this.reservationTable.containsKey(ReservationSystem.getFormattedDate(date));
	}
	
	/**
	 * A function to get reservations on the same date.
	 * @param date {Date} the date of the reservation
	 * @return {ArrayList<Reservation>} all reservations on the same date
	 * @throws {ReservationNotFoundException} exception when no reservation found on the same date
	 */
	private ConcurrentHashMap<String, ArrayList<Reservation>> getReservationOnSameDate(Date date) throws ReservationNotFoundException {
		if (this.doesReservationOnSameDateExist(date)) {
			return this.reservationTable.get(ReservationSystem.getFormattedDate(date));
		}
		throw new ReservationNotFoundException(); 
	}
	
	/**
	 * A function to know whether there is any reservations on a particular date and room type.
	 * @param date {Date} the date of the reservation
	 * @param roomType {String} the room type
	 * @return {boolean} the existence of any reservations on a particular date and room type
	 * @throws {ReservationNotFoundException} exception when no reservation found
	 */
	private boolean doesReservationOnSameDateAndRoomTypeExist(Date date, String roomType) throws ReservationNotFoundException {
		if (this.doesReservationOnSameDateExist(date)) {
			return this.getReservationOnSameDate(date).containsKey(roomType);
		}
		return false;
	}
	
	/**
	 * A function to get all reservations on the same date and room type.
	 * @param date {Date} the date of the reservation
	 * @param roomType {String} the room type
	 * @return {ArrayList<reservation>} all reservation on the same date and room type
	 * @throws {ReservationNotFoundException} exception when no reservation found
	 */
	private ArrayList<Reservation> getReservationsOnSameDateAndRoomType(Date date, String roomType) throws ReservationNotFoundException {
		if (this.doesReservationOnSameDateAndRoomTypeExist(date, roomType)) {
			return this.getReservationOnSameDate(date).get(roomType);
		}
		throw new ReservationNotFoundException();
	}
	
	/**
	 * A function to get a formatted date string.
	 * @param date {Date} the date to format
	 * @return {String} the formatted date
	 */
	@SuppressWarnings("deprecation")
	private static String getFormattedDate(Date date) {
		return date.toLocaleString().split(",")[0];
	}
	
	/**
	 * A function to know whether duplication reservation id exists.
	 * @param reservationId {String} the reservation id to check for duplication
	 * @return {boolean} the existence of duplicate reservation id
	 */
	private boolean doesDuplicateReservationIdExist(String reservationId) {
		for(String date: this.reservationTable.keySet()) {
			for (String roomType: this.reservationTable.get(date).keySet()) {
				for (Reservation reservation: this.reservationTable.get(date).get(roomType)) {
					if (reservation.getReservationId().equals(reservationId)) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	// get all reservations for a particular roomType
	// for each day
	/**
	 * Get all reservations for a particular roomType.
	 * @param roomType {String} roomType of reservation
	 * @return {ArrayList<Reservation>} An ArrayList of all reservations for a particular roomType
	 */
	public ArrayList<Reservation> getRoomTypeReservation(String roomType){
		ArrayList<Reservation> reservationList = new ArrayList<Reservation>();
		ConcurrentHashMap<String, ArrayList<Reservation>> tempMap = new ConcurrentHashMap<String, ArrayList<Reservation>>();
		
		
		//checks through the hashmap for reservations with a particular roomType
		for(String date:this.reservationTable.keySet()) {
			tempMap = this.reservationTable.get(date);
			
			for(String roomt:tempMap.keySet()) {
				
				if(roomt.equals(roomType)) {
						reservationList.addAll(tempMap.get(roomt));
				}
				
			}
				
				
			
		}

		return reservationList;
	}
	
	/**
	 * A function to get the reservation object using the reservationId
	 * @param reservationId The reservationId of the reservation object
	 * @return {Reservation} The reservation object with the specified reservationId
	 */
	public Reservation getReservation(String reservationId) {
		
		for(String date: this.reservationTable.keySet()) {
			for (String roomType: this.reservationTable.get(date).keySet()) {
				for (Reservation reservation: this.reservationTable.get(date).get(roomType)) {
					if (reservation.getReservationId().equals(reservationId)) {
						return reservation;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * A function to remove reservation by the guest and the room type.
	 * @param aGuest {Guest} the guest object
	 * @param aRoomType {String} the room type
	 * @param numberOfRooms {int} the number of rooms that are available for the specified room type
	 * @throws {ReservationNotFoundException} when reservation to be removed is not found 
	 */
	public void removeReservationByGuestAndRoomType(Guest aGuest, String aRoomType, int numberOfRooms) 
			throws ReservationNotFoundException {
		String reservationId = null;
		for (String date: this.reservationTable.keySet()) {
			for (String roomType: this.reservationTable.get(date).keySet()) {
				for (Reservation reservation: this.reservationTable.get(date).get(roomType)) {
					if (Guest.isIdentical(reservation.getGuest(), aGuest) && 
							reservation.getRoomType().equals(aRoomType)) {
						reservationId = reservation.getReservationId();
						break;
					}
				}
			}
		}
		if (reservationId != null) {
			this.removeReservation(reservationId, numberOfRooms);
		}
		else {
			throw new ReservationNotFoundException();
		}
	}
	
	/**
	 * A function to expire all reservations up to certain date.
	 * @param date {Date} the up to date of reservations to expire
	 */
	@SuppressWarnings("deprecation")
	public void expireAllReservationsUpToDate(Date date) {
		String formattedDate = ReservationSystem.getFormattedDate(date);
		for (String registeredFormattedDate: this.reservationTable.keySet()) {
			if (new Date(registeredFormattedDate).compareTo(new Date(formattedDate)) <= 0) {
				this.reservationTable.remove(registeredFormattedDate);
			}
		}
	}
}