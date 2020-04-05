package resources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;

public class Menu {
	public Hashtable<String,Double> Menu_list;
	public Menu(String filepath) {
		Hashtable<String,Double> Menu_list = new Hashtable<String, Double>();
		this.Menu_list = getMenu(filepath);
	}
	

	public void addItems(String foodName, double price) {
		// Append new food and price to the .txt file
		this.Menu_list.put(foodName, price);
		System.out.println("Modification success! Here is the current menu.");
		printItems();
	}
	public void updateItems(String foodName, double price) {
		// Check if foodName is inside the .txt file, if so modify the price
		// If not, raise error
		this.Menu_list.remove(foodName);
		this.Menu_list.put(foodName, price);
		System.out.println("Modification success! Here is the current menu.");
		printItems();
	}
	public void removeItems(String foodName) {
		// Check if foodName is inside the .txt file, if so remove the food name
		// If not, raise error
		this.Menu_list.remove(foodName);
		System.out.println("Modification success! Here is the current menu.");
		printItems();
	}
	public void printItems() {
		System.out.println("Food \t\t\t\tPrice");
		for (String food: this.Menu_list.keySet()) {
			System.out.printf("%-30.30s  %-30.30s%n", food, Menu_list.get(food));
		}
		
	}
	
	private Hashtable<String,Double> getMenu(String MenuFilePath) {
		// Here we will read the txt file in the format of csv. with the format of "food,price". Then next we will create a hashtable for the menu.
		String row;
		String[] data = null;
		Hashtable<String,Double> Menu = new Hashtable<String, Double>();
		try {
			BufferedReader csvReader = new BufferedReader(new FileReader(MenuFilePath));
			while ((row = csvReader.readLine()) != null) {
			    data = row.split(",");
			    Menu.put(data[0],Double.valueOf(data[1]));
			}
			csvReader.close();
		}
		catch (FileNotFoundException e) {
			System.out.println("Err: " + e.getMessage());
		}
		catch (IOException e) {
			System.out.println("Err: " + e.getMessage());
		}
		return Menu;
	}
	
}
