package edu.upenn.cit594.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.processor.Processor;



public class UserInterface {
	 private Scanner scanner;
	 protected Processor processor;
	 protected Logger logger;
	public UserInterface(Processor processor, Logger logger) {
		this.scanner = new Scanner(System.in);
		this.processor = processor;
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
	
	 public void executeAction(int action) {
	        switch (action) {
	            case 0:
	                System.out.println("Exiting the program.");
	                System.exit(0);
	                break;
	            case 1:
	            	requestUserInput();
	                break;
	          //this should display 1603797
	            case 2:
	            	// debug
	            	System.out.println("calculating total pop...");
	            	 int totalPopulation = processor.getTotalPopulation();
	                 System.out.println("Total Population: " + totalPopulation);
	                 break;
	            case 3:  //not finished
	            	 System.out.println("Please enter 'partial' or 'full': ");
	            	 String vaxType = scanner.nextLine().toLowerCase();
	            	//first we need to validate the vaxtype entered by the user is correct
	    	        if (!vaxType.equalsIgnoreCase("partial") && !vaxType.equalsIgnoreCase("full")) {
	    	            System.out.println("Invalid vaccination type. Please enter 'partial' or 'full'.");
	    	            return;
	    	        }
	    	        System.out.println("Please enter the date in the format YYYY-MM-DD: ");
	    	        String date = scanner.nextLine();
	    	       
	    	        // Validate date format will call private helper method
	    	        if (!isValidDateFormat(date)) {
	    	            System.out.println("Invalid date format. Please enter a date in the format YYYY-MM-DD.");
	    	            return;
	    	        }
	    	      //need to call available actions  
	    	        	
	    	       double totalVaxPerCapita = processor.getTotalVaccinationsPerCapita(vaxType, date);
	    	       System.out.println("The total number of vaccination per capita for " + date + " is: " + totalVaxPerCapita);
	             
	                break;
	            case 4: //finished
	            	  int specifiedZipCode4 = request5DigitZip();
	                  int averageMarketValue = processor.getAverageMarketValue(specifiedZipCode4);
	                  System.out.println("Average Market Value for ZIP Code " + specifiedZipCode4 + ": " + averageMarketValue);
	                  break;
	            case 5: //finished
	            	 int specifiedZipCode5 = request5DigitZip();
	                 int averageLivableArea = processor.getAverageLivableArea(specifiedZipCode5);
	                 System.out.println("Average Total Livable Area for ZIP Code " + specifiedZipCode5 + ": " + averageLivableArea);
	                 break;	        
	            case 6: //finished
	            	 int specifiedZipCode6 = request5DigitZip();
	                 int totalMarketValuePerCapita = processor.getTotalMarketValuePerCapita(specifiedZipCode6);
	                 System.out.println("Total Market Value of Properties Per Capita for ZIP Code " + specifiedZipCode6 + ": " + totalMarketValuePerCapita);
	                 break;
	            case 7:
	              
	                break;
	            default:
	                System.out.println("Invalid option. Please enter a number between 0 and 7.");
	                break;
	        }
	    }
	
	 
	   
	    /**
	     * Checks if the provided date string has the format YYYY-MM-DD.
	     * 
	     * @param date The date string to validate.
	     * @return true if the date format is valid, false otherwise.
	     */
	    private boolean isValidDateFormat(String date) {
	        try {
	            // Attempt to parse the date
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            dateFormat.setLenient(false);
	            dateFormat.parse(date);
	            return true;
	        } catch (ParseException e) {
	            return false;
	        }
	    }
}
