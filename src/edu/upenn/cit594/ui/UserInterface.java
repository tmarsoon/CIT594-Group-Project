package edu.upenn.cit594.ui;

import java.util.Scanner;

import edu.upenn.cit594.processor.TotPopulation;


public class UserInterface {
	private Scanner scanner;
	
	public UserInterface() {
		this.scanner = new Scanner(System.in);
	}
		
	
	private int readInput() {
		while (!scanner.hasNextInt()) {
			System.out.println("Invalid input. Please enter a number between 0 and 7. > ");
			scanner.next();
		}
		int input = scanner.nextInt();
		scanner.nextLine();
		return input;
	}
	
	
	public int requestUserInput() {
		
			displayMenu();
			System.out.print("> ");
			System.out.flush(); 
			return readInput();
	}
	
	/** 
	 * method to prompt user for a valid 5 digit Zip code
	 * @return
	 */
	public int request5DigitZip() {
		while (true) {
			System.out.print("Please enter a 5-digit Zip Code.");
			System.out.print("> ");
			System.out.flush(); 
			
			if (scanner.hasNextInt()) {
				int zipCode = scanner.nextInt();
				// casting to string to check that the zip has 5 digits
				if (String.valueOf(zipCode).length() == 5) {
					return zipCode;
				} else {
					System.out.print("Invalid Zip Code. Please enter a 5-digit Zip Code.");
					System.out.print("> ");
					scanner.nextLine();
				}
			}
			else {
				System.out.print("Invalid Zip Code. Please enter a 5-digit Zip Code.");
				System.out.print("> ");
				scanner.nextLine();
			}
		}
	}
	
	
	public void displayMenu() {
		System.out.println("Which action would you like to perform?");
		System.out.println("0. Exit the program.\n"
				+ "1. Show the available actions.\n"
				+ "2. Show the total population for all ZIP Codes.\n"
				+ "3. Show the total vaccinations per capita for each ZIP Code for the specified date.\n" 
				+ "4. Show the average market value for properties in a specified ZIP Code.\n"
				+ "5. Show the average total livable area for properties in a specified ZIP Code.\n"
				+ "6. Show the total market value of properties, per capita, for a specified ZIP Code.\n"
				+ "7. Show the results of your custom feature.");
		
	}
	
	public int displayTotPop(String populationFile) {
		int totPop = TotPopulation.totalPopCalculator(populationFile);
		
		return totPop;
	}
}
