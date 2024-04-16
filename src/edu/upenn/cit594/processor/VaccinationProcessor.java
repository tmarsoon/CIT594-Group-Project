package edu.upenn.cit594.processor;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.upenn.cit594.datamanagement.JSONCovidData;
import edu.upenn.cit594.datamanagement.PopulationReader;

public class VaccinationProcessor {

	public String getDate() {
		
		Scanner scanner = new Scanner(System.in);
		String date;
		
		// ask for date from user
		System.out.println("Please type in a date in the format: YYYY-MM-DD.");
		System.out.println("> ");
		
		// while input is invalid, reprompt user
		while (true) {
			// get input
			date = scanner.nextLine().trim();
			
			if (validDateFormat(date)) {
				break;
				
			} else {
				System.out.println("Please type in a date in the format: YYYY-MM-DD.");
				System.out.println("> ");
			}
		}
		
		return date;
	}
	
	private boolean validDateFormat(String dateInput) {
		
		// return true if the input matches valid pattern
		return dateInput.matches("\\d{4}-\\d{2}-\\d{2}");
	}
	
	private double vaccinationPerCapita(String date, int zipCode, String vaccinationType ) {
		
		int vaccinationNb = JSONCovidData.getVaccinationNumber();
		int zipCodePopulation = PopulationReader.localPopulation(zipCode);
		
		if (zipCodePopulation == 0) {
			return 0;
		}
		
		return vaccinationNb/zipCodePopulation;
	}

}

