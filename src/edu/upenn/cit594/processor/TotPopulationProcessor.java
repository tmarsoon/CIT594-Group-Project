package edu.upenn.cit594.processor;

import java.io.IOException;
import java.util.List;

import edu.upenn.cit594.datamanagement.PopulationReader;

public class TotPopulationProcessor {

	public int totalPopCalculator(String populationFile ) {
		
		int totPop = 0;
		
		// using the Population reader class to be implemented in data management
		PopulationReader populationReader = new PopulationReader();
		// adding the pop values to a list using the readPopulation() method to be implemented
		List <Integer > populationData = populationReader.readPopulation(populationFile);
		
		// adding the values together
		
		for (int population :populationData ) {
			totPop += population;
		}
		
		return totPop;
	}


}

