package edu.upenn.cit594.ui;

import java.util.ArrayList;
import java.util.List;

public class AvailableActions {

	/**
	 * diplays the menu of options to the user based on the file used
	 * @param args
	 */
	public void displayMenu(String [] args) {
		System.out.println("BEGIN OUTPUT");
		List <Integer> availableOptions = displayAvailableOptions(args);
		// print each option to console
		for (Integer option:availableOptions ) {
			System.out.println(option);
		}
		System.out.println("END OUTPUT");
	}

	private List < Integer> displayAvailableOptions(String [] args) {
		
		List < Integer> availableOptions = new ArrayList<>();
		// 0 and 1 are always valid options 
		availableOptions.add(0);
		availableOptions.add(1);
		
		// adding options based on file 
		if (hasCovidFile(args)) {
			availableOptions.add(3);
		}
		
		if (hasPropertiesFile(args)) {
			availableOptions.add(4);
			availableOptions.add(5);
			availableOptions.add(6);
		}
		
		if (hasPopulationFile(args)) {
			availableOptions.add(2);
		}
		
		// returning the list of option
		return availableOptions;
		
	}

	private boolean hasPopulationFile(String[] args) {
		
		for (String arg : args) {
			
			// split at the = sign
			String [] parts = arg.split("=");
			if (parts.length == 2) {
				String name = parts[0];
				
				if (name.equals("--population")) {
					return true;
				}
			}
		}
		
		return false;
	}

	private boolean hasPropertiesFile(String[] args) {
		
		for (String arg : args) {
			
			// split at the = sign
			String [] parts = arg.split("=");
			if (parts.length == 2) {
				String name = parts[0];
				
				if (name.equals("--properties")) {
					return true;
				}
			}
		}
		
		return false;
	}

	private boolean hasCovidFile(String[] args) {
		
		for (String arg : args) {
			
			// split at the = sign
			String [] parts = arg.split("=");
			if (parts.length == 2) {
				String name = parts[0];
				
				if (name.equals("--covid")) {
					return true;
				}
			}
		}
		
		return false;
	}
}

