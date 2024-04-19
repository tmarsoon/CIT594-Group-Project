package edu.upenn.cit594.processor;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

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
	     */
	    public Processor(CSVCovidData ccd, JSONCovidData jcd, PopulationReader popr, PropertiesReader propr) {
	        
	        // set instance vars
	        csvCovidReader = ccd;
	        jsonCovidReader = jcd;
	        populationReader = popr;
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
	    private void dataStart() {
	        
	        try {
	            populationMap = populationReader.readPopulationData();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (ParseException e) {
	        
	            e.printStackTrace();
	        }

	        // Populate ArrayList fields of zipCode objects step 1: Properties
	        try {
	            propertiesReader.processProperties(populationMap);
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        

	    }

//memorization for average market value
private HashMap<Integer, Integer> memValue = new HashMap<>(); 
public int getAverageMarketValue (int zipCode) {
    if (memValue.containsKey(zipCode)){
        return memValue.get(zipCode);
    }else {
        double sumMarketValue = 0;
        int averageMarketValue = 0;
        if (populationReader.containsKey(zipCode)) {
            ZipCode code = populationMap.get(zipCode);
            for (Property property : code.getProperties()) {
                sumMarketValue += property.getMarketValue();
            }
            GetAverage average = new GetAverage(); // Implement Strategy
            averageMarketValue = average.getAverage(sumMarketValue, code.getProperties().size());

        } else {
            System.out.println("No data available or invalid ZipCode");
        }
        memValue.put(zipCode, averageMarketValue);
        return averageMarketValue;
    }
}

/**
 * Q4
 * Calculates the average total liveable area of homes in a given zipcode
 * @param zipCode
 * @return average liveable area
 */
private HashMap<Integer, Integer> memArea = new HashMap<>(); //Memoization
public int getAverageLivableArea(int zipCode) {
    if (memArea.containsKey(zipCode)){
        return memArea.get(zipCode);
    }else {
        double sumLivableArea = 0;
        int averageLivableArea = 0;
        if (populationMap.containsKey(zipCode)) {
            ZipCode code = populationMap.get(zipCode);
            for (Property property : code.getProperties()) {
                sumLivableArea += property.getLivableArea();
            }
            GetAverage average = new GetAverage(); // Implement Strategy
            averageLivableArea = average.getAverage(sumLivableArea, code.getProperties().size());
        } else {
            System.out.println("No data available or invalid ZipCode");
        }
        memArea.put(zipCode, averageLivableArea);
        return averageLivableArea;
    }
}

/**
 * Q5
 * Calculates the total residential market value per capita in a given zipcode
 * @param zipCode
 * @return total RMV per capita
 */
private HashMap<Integer, Integer> memValuePC = new HashMap<>(); //Memoization
public int getTotalValuePC(int zipCode){
    if (memValuePC.containsKey(zipCode)){
        return memValuePC.get(zipCode);
    }else {
        double sumMarketValue = 0;
        int marketValuePC = 0;
        if (!populationReader.containsKey(zipCode)) {
            return 0;
        }
        ZipCode code = populationMap.get(zipCode);
        for (Property property : code.getProperties()) {
            sumMarketValue += property.getMarketValue();
        }
        if (sumMarketValue == 0 || code.getPopulation() == 0) {
            return 0;
        }
        marketValuePC = (int) (sumMarketValue / code.getPopulation());
        memValuePC.put(zipCode, marketValuePC);
        return marketValuePC;
    }
}


