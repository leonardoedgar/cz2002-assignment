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
	public Menu(String filepath) {
		Hashtable<String,Double> Menu_list = new Hashtable<String, Double>();
		this.Menu_list = getMenu(filepath);
	}
	

	public void addItems() {
		// Append new food and price to the .txt file
		String foodname;
		double price;
		Scanner scn = new Scanner(System.in);
		System.out.println("Enter the food name:");
		foodname = scn.nextLine();
		System.out.println("Enter the price of the food:");
		price = scn.nextDouble();
		this.Menu_list.put(foodname, price);
		System.out.println("Modification success! Here is the current menu.");
		printItems();
	}
	
	public void updateItems() {
		// Check if foodName is inside the .txt file, if so modify the price
		// If not, raise error
		String foodname;
		double price;
		boolean isFoodOnMenu = false;
		Scanner scn = new Scanner(System.in);
		System.out.println("Enter the food name:");
		foodname = scn.nextLine();
		System.out.println("Enter the price:");
		price = scn.nextDouble();
		for (String food: this.Menu_list.keySet()) {
			if (food.equals(foodname)){
				this.Menu_list.remove(foodname);
				this.Menu_list.put(foodname, price);
				System.out.println("Modification success! Here is the current menu.");
				printItems();
				isFoodOnMenu = true;
				break;
			}
		}
		if (isFoodOnMenu == false) {
		System.out.println("Item is not in the list, but we will add it in.");
		this.Menu_list.remove(foodname);
		this.Menu_list.put(foodname, price);
		System.out.println("Modification success! Here is the current menu.");
		printItems();
		}
		isFoodOnMenu = false;	
	}
	
	public void removeItems() {
		// Check if foodName is inside the .txt file, if so remove the food name
		// If not, raise error
		String foodname;
		boolean isFoodOnMenu = false;
		Scanner scn = new Scanner(System.in);
		System.out.println("Enter the food name:");
		foodname = scn.nextLine();
		for (String food: this.Menu_list.keySet()) {
			if (food.equals(foodname)){
				this.Menu_list.remove(foodname);
				System.out.println("Modification success! Here is the current menu.");
				printItems();
				isFoodOnMenu = true;
				break;
			}
		}
		if (isFoodOnMenu == false) {
		System.out.println("Item is not in the list");}
		isFoodOnMenu= false;
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
