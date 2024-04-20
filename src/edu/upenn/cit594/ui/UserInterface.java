package edu.upenn.cit594.ui;

import java.util.Scanner;

import edu.upenn.cit594.processor.Processor;
import edu.upenn.cit594.processor.TotPopulationProcessor;


public class UserInterface {
	private Scanner scanner;
	 protected Processor processor;
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
	
	 public void executeAction(int action) {
	        switch (action) {
	            case 0:
	                System.out.println("Exiting the program.");
	                System.exit(0);
	                break;
	            case 1:
	                displayMenu();
	                break;
	            case 2:
	                int totalPopulation = populationProcessor.totalPopCalculator("population.csv");
	                System.out.println("Total Population: " + totalPopulation);
	                break;
	            case 3:
	                int zipCode = request5DigitZip();
	                // Assume you have a method in VaccinationProcessor to get total vaccinations per capita
	                int vaccinationsPerCapita = vaccinationProcessor.getTotalVaccinationsPerCapita(zipCode);
	                System.out.println("Total Vaccinations Per Capita for ZIP Code " + zipCode + ": " + vaccinationsPerCapita);
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
	}
	

