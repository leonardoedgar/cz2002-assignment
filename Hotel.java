public class Hotel {
	//maybe add a constant totalroom for easier updating(if report requires futrure planning)
	//maybe include a enum for the room names/number to more check availability with getStatus()
	private Room[] roomList = new Room[48];
	
	
	public Hotel() {
		for(int i=0;i<48;i++) {
			//creation of rooms for the hotel, needs to have details from Room
			roomList[i]=rm;
			
		}
	}
	
	/**
	 * A method to find vacant rooms in the hotel
	 * @return {Array} list of rooms that are currently vacant
	 */
	public Room[] getAvailableRoomList() {
		Room[] availableRoom = new Room[48];
		
		for(int i=0;i<48;i++) {
			if(roomList[i].getStatus()== "Vacant") {
				availableRoom[i]=rm[i];
			}
		}
		
		return availableRoom;
	}

	/**
	 * A method to print the report for room occupancy based on input choice of true(room type) or false (room status)
	 * @param typeOfReport
	 */
	public void printReport(boolean typeOfReport) {
		int vacantSingle=0;
		int vacantDouble=0;
		int vacantDeluxe=0;
		int vacantVIPSuite=0;
		int totalVacant=0;
		
		for(int i=0;i<48;i++) {
			if(roomList[i].getStatus()== "Vacant") {
				if(roomList[i] instanceof Single) { 
					vacantSingle+=1;
				}
				if(roomList[i] instanceof DoubleRoom) { 
					vacantDouble+=1;
				}
				if(roomList[i] instanceof Deluxe) { 
					vacantDeluxe+=1;
				}
				if(roomList[i] instanceof VIPSuite) { 
					vacantVIPSuite+=1;
				}
				
			}
		}
		totalVacant=vacantSingle+vacantDouble+vacantDeluxe+vacantVIPSuite;
			
			
		if(typeOfReport==true) {
			//report by roomtype occupancy
			//change totalSingle to the predefined number of singlerooms, same for the rest
			System.out.println("Single : Number : "+vacantSingle +" out of "+totalSingle +"\n Rooms : ");
			System.out.println("Double : Number : "+vacantDouble +" out of "+totalDouble +"\n Rooms : ");
			System.out.println("Deluxe : Number : "+ vacantDeluxe+" out of "+ totalDeluxe +"\n Rooms : ");
			System.out.println("VIP Suite : Number : "+vacantVIPSuite +" out of "+ totalVIPSuite +"\n Rooms : ");
		}
		else {
			//report by roomstatus
			System.out.println("Vacant\t:\n \t\tRooms : "+ totalVacant);
			System.out.println("Occupied\t:\n \t\tRooms : "+ (48-totalVacant));
			
		}
	}
	
}
