package resources;

public class Menu {
	private String filePath;
	public void addItems(String foodName, float price) {
		// Append new food and price to the .txt file
	}
	public void updateItems(String foodName, float price) {
		// Check if foodName is inside the .txt file, if so modify the price
		// If not, raise error
	}
	public void removeItems(String foodName) {
		// Check if foodName is inside the .txt file, if so remove the food name
		// If not, raise error
	}
	public void printItems() {
		// Print all available food and its costs
	}
}
