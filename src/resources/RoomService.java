package resources;
import java.util.Hashtable;
import java.util.Date;
import exception.FoodNotOnMenuException;
import resources.Menu;

/**
 * A class to represent a room service in a hotel.
 */
public class RoomService {
	private Menu menu;
	private double totalCost;
	private Hashtable<String, String> orderMap;
	private Date dateOfTransaction;
	private String status;
	private int orderIdLocal;
	/**
	 * A class constructor to construct a room service.
	 * @param menu {Menu} the room service menu in a hotel
	 */
	public RoomService(Menu menu) {
		this.menu = menu;
		this.status = "confirmed";
		this.orderIdLocal = RoomService.orderId;
		RoomService.orderId = RoomService.orderId + 1;
		RoomService.orderId= (RoomService.orderId % 2000000000);
	}
	
	public static int orderId = 1;
	
	/**
	 * A function to make an order.
	 * @param orderMap {Hashtable<String, String>} the order and quantity map
	 * @throws {FoodNotOnMenuException} when food ordered is not on the menu
	 */
	public void makeOrder(Hashtable<String, String> orderMap) 
			throws FoodNotOnMenuException {
		this.updateTotalCost(orderMap);
		this.orderMap = orderMap;
		this.dateOfTransaction = new Date();
	}
	
	/**
	 * A function to update the total cost of the room service.
	 * @param orderMap {Hashtable<String, String>} the order and quantity map
	 * @throws {FoodNotOnMenuException} when food ordered is not on the menu
	 */
	private void updateTotalCost(Hashtable<String, String> orderMap) 
			throws FoodNotOnMenuException {
		double cost = 0;
		for (String foodName: orderMap.keySet()) {
			cost += this.menu.getCostOfAFood(foodName)*Integer.parseInt(
					orderMap.get(foodName));
		}
		this.totalCost = cost;
	}
	
	/**
	 * A function to print room service order.
	 */
	public void printOrderHistory() {
		if (this.totalCost != 0) {
			System.out.println("Date of transaction: " + this.dateOfTransaction.toString());
			for (String foodName: this.orderMap.keySet()) {
				String foodQuantity = this.orderMap.get(foodName);
				try {
					double totalCostPerFood = 
							this.menu.getCostOfAFood(foodName)*Integer.parseInt(foodQuantity);
					System.out.println(foodQuantity + " " + foodName + ": SGD" + 
							String.format("%.2f", totalCostPerFood));	
				} catch (FoodNotOnMenuException e) {
					continue;
				}
			}
		}
		else {
			System.out.println("There is no order yet!.");
		}
	}
	
	/**
	 * A function to get the total cost of the room service order.
	 * @return {double} the total cost
	 */
	public double getTotalCost() {
		return this.totalCost;
	}
	
	/**
	 * this function will get the recent status of the room service
	 */
	public String getStatus() {
		return this.status;
	}
	
	/**
	 * to update the current status of the room service
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * to update the orderId
	 * @param orderId
	 */
	public static int updateOrderId(int orderId) {
		orderId++;
		orderId = orderId% 2000000000;
		return orderId;
	}
	
	/**
	 * get OrderMap
	 */
	public Hashtable<String, String> getOrderMap() {
		return this.orderMap;
	}
	
	/**
	 * to get the orderID
	 * @return
	 */
	public int getOrderId() {
		return this.orderIdLocal;
	}
	
}
