package resources;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class Menu {
	public Hashtable<String,Double> Menu_list;
	public Menu(String filepath) throws FileNotFoundException, IOException {
		Hashtable<String,Double> Menu_list = new Hashtable<String, Double>();
		this.Menu_list = getMenu(filepath);
	}
	
/**
 * add the items in the list (will also check if the food is already in the menu)
 * @param foodname
 * @param price
 */
	public void addItems(String foodname, double price) {
		// Append new food and price to the .txt file
		boolean isFoodOnMenu = false;
		for (String food: this.Menu_list.keySet()) {
			if (food.equals(foodname)){
				System.out.println("The food is already in the menu.");
				isFoodOnMenu = true;
				break;
			}
		}
		if (isFoodOnMenu == false) {
			this.Menu_list.put(foodname, price);
			System.out.println("Modification success!");}
		isFoodOnMenu= false;
	}
	
/**
 * update the menu, will not update if the food is not yet in the menu.
 * @param foodname
 * @param price
 */
	public void updateItems(String foodname, double price) {
		// Check if foodName is inside the .txt file, if so modify the price
		// If not, raise error
		boolean isFoodOnMenu = false;
		for (String food: this.Menu_list.keySet()) {
			if (food.equals(foodname)){
				this.Menu_list.remove(foodname);
				this.Menu_list.put(foodname, price);
				System.out.println("Modification success!");
				isFoodOnMenu = true;
				break;
			}
		}
		if (isFoodOnMenu == false) {
		System.out.println("Item is not in the list.");
		}
		isFoodOnMenu = false;	
	}
	
/**
 * remove items from the menu. Will also check if the food is on the menu.	
 * @param foodname
 */
	public void removeItems(String foodname) {
		// Check if foodName is inside the .txt file, if so remove the food name
		// If not, raise error
		boolean isFoodOnMenu = false;
		for (String food: this.Menu_list.keySet()) {
			if (food.equals(foodname)){
				this.Menu_list.remove(foodname);
				System.out.println("Modification success!");
				isFoodOnMenu = true;
				break;
			}
		}
		if (isFoodOnMenu == false) {
		System.out.println("Item is not in the list.");}
		isFoodOnMenu= false;
	}
	
/**
 * print the current menu.
 */
	public void printItems() {
		System.out.println("Food \t\t\t\tPrice");
		for (String food: this.Menu_list.keySet()) {
			System.out.printf("%-30.30s  %-30.30s%n", food, Menu_list.get(food));
		}	
	}
	
	private Hashtable<String,Double> getMenu(String MenuFilePath) throws FileNotFoundException,IOException {
		// Here we will read the txt file in the format of csv. with the format of "food,price". Then next we will create a hashtable for the menu.
		String row;
		String[] data = null;
		Hashtable<String,Double> Menu = new Hashtable<String, Double>();
		BufferedReader csvReader = new BufferedReader(new FileReader(MenuFilePath));
		while ((row = csvReader.readLine()) != null) {
		    data = row.split(",");
		    Menu.put(data[0],Double.valueOf(data[1]));
		}
		csvReader.close();
		
		return Menu;
	}
	
}
