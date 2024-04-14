package edu.upenn.cit594.ui;
public class Console {

	/**
	 * displays meny showed to user
	 * @return
	 */
	public String displayMenu() {
		
		System.out.println("0. Exit the program.\n"
				+ "1. Show the available actions.\n"
				+ "2. Show the total population for all ZIP Codes.\n"
				+ "3. Show the total vaccinations per capita for each ZIP Code for the specified date.\n" 
				+ "4. Show the average market value for properties in a specified ZIP Code.\n"
				+ "5. Show the average total livable area for properties in a specified ZIP Code.\n"
				+ "6. Show the total market value of properties, per capita, for a specified ZIP Code.\n"
				+ "7. Show the results of your custom feature.");
	}

	
}
