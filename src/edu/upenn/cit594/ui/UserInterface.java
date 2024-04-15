package edu.upenn.cit594.ui;

import java.util.Scanner;


public class UserInterface {
		
	public int requestUserInput() {
		
		Scanner scanner = new Scanner(System.in);
		int input;
		
		do {
			System.out.println("Which action would you like to perform?");
			System.out.println("0. Exit the program.\n"
					+ "1. Show the available actions.\n"
					+ "2. Show the total population for all ZIP Codes.\n"
					+ "3. Show the total vaccinations per capita for each ZIP Code for the specified date.\n" 
					+ "4. Show the average market value for properties in a specified ZIP Code.\n"
					+ "5. Show the average total livable area for properties in a specified ZIP Code.\n"
					+ "6. Show the total market value of properties, per capita, for a specified ZIP Code.\n"
					+ "7. Show the results of your custom feature.");
			System.out.print("> ");
			System.out.flush(); 
			
			if (scanner.hasNextInt()) {
		
				// get the input
				input = scanner.nextInt();
		
				if ((input >= 0 && input <= 7)) {
					break;
				}
		
			} else {
				System.out.println("Invalid input. Please enter a number between 0 and 7. > ");
				input = scanner.nextInt();
			}
			
		} while (true);
		
		scanner.close();
		return input;
	}
}
