package edu.upenn.cit594.processor;

import java.io.IOException;
import java.text.ParseException;

import java.util.HashMap;
import java.util.Map;


import edu.upenn.cit594.data.Property;
import edu.upenn.cit594.data.ZipCode;
import edu.upenn.cit594.datamanagement.CSVCovidData;
import edu.upenn.cit594.datamanagement.JSONCovidData;
import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertiesReader;
import edu.upenn.cit594.logging.Logger;

/**
 * Q3
 * Calculates the average market value of homes in a given zipcode
 * 
 * @param zipCode input by user
 * @return average market value
 */
public class Processor {
	
	  	protected CSVCovidData csvCovidReader;
	    protected JSONCovidData jsonCovidReader;
	    protected PopulationReader populationReader;
	    protected PropertiesReader propertiesReader;
	    protected Logger logger;
	    protected Map<Integer, ZipCode> populationMap;
	    
	    /**
	     * Creates all required reader objects in the constructor
	     * @param ccd CSVCovidData
	     * @param jcd JSONCovidData
	     * @param popr PopulationReader
	     * @param propr PropertiesReader
	     * @throws IOException 
	     * @throws ParseException 
	     * @throws NumberFormatException 
	     */
	    public Processor(String populationFile, CSVCovidData ccd, JSONCovidData jcd, PopulationReader popr, PropertiesReader propr) throws IOException, NumberFormatException, ParseException {
	        
	        // set instance vars
	        csvCovidReader = ccd;
	        jsonCovidReader = jcd;
	        populationReader = popr;
	        
	        // reading population file
	        popr.readPopulationData(populationFile);
	        
	        // populating the population map
	        populationMap = popr.getPopulationMap();
	        
	        // setting instance of variable
	        propertiesReader = propr;	
	        
	        //Starting Data
	        dataStart();

	    }

	    public Map<Integer, ZipCode> getPopulationMap() {
	        return populationMap;
	    }

	    /**
	     * Instantiates the TreeMap and populates ArrayList fields of ZipCode objects
	     * for data analysis
	     */
	    private void dataStart() throws IOException {
	    	 populationMap = populationReader.getPopulationMap();

	    	   propertiesReader.readProperties(populationMap);
	    	}
//Enters 2 - 3.2 finished    
	    private int memPop = 0;
	    public int getTotalPopulation() {
	       if (memPop == 0) {
	    		int totalPopulation = 0;
		        for (ZipCode zipCode : populationMap.values()) {
		            totalPopulation += zipCode.getPopulation();
	       }
	    	memPop = totalPopulation;
	       }
	          return memPop;
	    }
	    
//Enters3 - 3.3, not finished
	    /**
	     * method to get and return the total vax per capita 
	     * @param vaxType
	     * @param date
	     * @param filetype: json or csv
	     * @return
	     */
	    public double getTotalVaccinationsPerCapita(String fileType, String vaxType, String date) {

	    	double totalVaccinationsPerCapita = 0.0;
	    	
	        try {
	            // Get the total number of vaccinations for the specified type and date
	        	// if file type is csv
	        	if (fileType.endsWith("csv")) {
		            int totalVaccinations = csvCovidReader.getVaccinationNumber(vaxType, date);
		            System.out.println("total vax : " + totalVaccinations);
		            
		            totalVaccinationsPerCapita = vaccinationPerCapitaCalculator(totalVaccinations);
	        	}
	        	
	        	// otherwise if the file is json
	        	else if (fileType.endsWith("JSON")) {
	        		int totalVaccinations = jsonCovidReader.getVaccinationNumber(vaxType, date);
		            System.out.println("total vax : " + totalVaccinations);
		            
		            totalVaccinationsPerCapita = vaccinationPerCapitaCalculator(totalVaccinations);
	        	}
	        	
	        	// otherwise, the file is not valid
	        	else {
	        		 System.out.println("The file type provided is not valid (not csv or JSON).");
	        	}
	            

	        } catch (ParseException e) {
	            System.out.println("Invalid date provided or date is out of range.");
	        }
	        return totalVaccinationsPerCapita;
	    }
	    

/**
 * heloer method to calculate the vaccination per capita	    
 * @param totalVaccinations
 * @return
 */
private double vaccinationPerCapitaCalculator(int totalVaccinations) {
	
	double totalVaccinationsPerCapita = 0.0;
	
	// Display vaccinations per capita for each ZIP Code
    for (ZipCode zipCode : populationMap.values()) {
        int population = zipCode.getPopulation();
        
        //Skip if population is 0 or unknown
        if (population == 0) {
        	System.out.println("The population for this zip code is 0.");
        	continue;
        }
        //Calculate vaccinations per capita
        double vaccinationsPerCapita = (double) totalVaccinations / population;
        
        //Skip if vaccinations per capita is 0
        if (vaccinationsPerCapita == 0) continue;
        
        // accumulate the total vaccinations
        totalVaccinationsPerCapita += vaccinationsPerCapita;
        
        System.out.printf("%05d %.4f\n", zipCode.getZip_Code(), vaccinationsPerCapita);
    }
    
    return totalVaccinationsPerCapita;
}



//Enters4 - 3.4, finished
//memorization for average market value
private HashMap<Integer, Integer> memValue = new HashMap<>(); 
public int getAverageMarketValue(int zipCode) {
	
	//read the pro
    if (memValue.containsKey(zipCode)) {
        return memValue.get(zipCode);
    } else {
        if (populationMap.containsKey(zipCode)) {
            ZipCode code = populationMap.get(zipCode);
           
            // iterating over the properties to retrieve all properties associated with zipcode
            for (Property property : code.getProperties()) {
            	//debug
            	System.out.println("Retrieving properties for the zip code provided: " + property);
            }
            int numProperties = code.getProperties().size();
            //debug
            System.out.println("The number of properties for this zip code is : "+ numProperties); // issue here, nothing gets pulled
            
            if (numProperties == 0) {
                // If there are no properties in the ZIP code, return 0
            	System.out.println("There are no properties in the given zip code.");
                return 0;
            }
            
            double sumMarketValue = 0;
            for (Property property : code.getProperties()) {
                sumMarketValue += property.getMarket_Value();
            }
            // Calculate average market value truncated to an integer
            //average market value gets truncated to an integer
            int averageMarketValue = (int) (sumMarketValue / numProperties);
            memValue.put(zipCode, averageMarketValue);
            return averageMarketValue;
        } else {
            System.out.println("No data available or invalid ZipCode");
            return 0;
        }
    }
}

//Enters 5- 3.5, finished
private HashMap<Integer, Integer> memArea = new HashMap<>(); //Memoization
public int getAverageLivableArea(int zipCode) {
	 if (memArea.containsKey(zipCode)) {
	        return memArea.get(zipCode);
	    } else {
	        double sumLivableArea = 0;
	        int averageLivableArea = 0;
	        if (populationMap.containsKey(zipCode)) {
	            ZipCode code = populationMap.get(zipCode);
	            
	            
	            int numberOfProperties = code.getProperties().size();
	            
	            if (numberOfProperties == 0) {
	                System.out.println("No properties available for the specified ZIP Code.");
	                return 0;
	            }
	            for (Property property : code.getProperties()) {
	                sumLivableArea += property.getTotal_Livable_Area();
	            }
	            //trucating the the average total livable are as an integer
	            averageLivableArea = (int) (sumLivableArea / numberOfProperties);
	        } else {
	            System.out.println("Invalid or unavailable ZIP Code.");
	            return 0;
	        }
	        memArea.put(zipCode, averageLivableArea);
	        return averageLivableArea;
	    }
	}

//Enters 6 - 3.6 finished
private HashMap<Integer, Integer> memValuePC = new HashMap<>(); //Memoization
public int getTotalMarketValuePerCapita(int zipCode){
	  if (memValuePC.containsKey(zipCode)) {
	        return memValuePC.get(zipCode);
	    } else {
	        if (!populationMap.containsKey(zipCode)
	        		) {
	            System.out.println("Invalid or unavailable ZIP Code.");
	            return 0;
	        }

	        ZipCode code = populationMap.get(zipCode);
	        int population = code.getPopulation();
	        if (population == 0) {
	            System.out.println("Population of ZIP Code is 0.");
	            return 0;
	        }

	        double sumMarketValue = 0;
	        for (Property property : code.getProperties()) {
	            sumMarketValue += property.getMarket_Value();
	        }

	        if (sumMarketValue == 0) {
	            System.out.println("Total market value of properties in ZIP Code is 0.");
	            return 0;
	        }

	        int marketValuePerCapita = (int) (sumMarketValue / population);
	        memValuePC.put(zipCode, marketValuePerCapita);
	        return marketValuePerCapita;
	    }
	}
}
