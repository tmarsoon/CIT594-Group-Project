package edu.upenn.cit594.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
	     */
	    public Processor(CSVCovidData ccd, JSONCovidData jcd, PopulationReader popr, PropertiesReader propr) throws IOException {
	        
	        // set instance vars
	        csvCovidReader = ccd;
	        jsonCovidReader = jcd;
	        populationReader = popr;
	        propertiesReader = propr;	        
	        try {
	    		dataStart();
	    	} catch (FileNotFoundException e) {
	    		
	    		e.printStackTrace();
	    	} catch (IOException e) {
	    		
	    		e.printStackTrace();
	    	}
	    }

	    public Map<Integer, ZipCode> getPopulationMap() {
	        return populationMap;
	    }

	    /**
	     * Instantiates the data for population mapping
	     * @param dataStart
	     * @throws IOException 
	
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

	try {
		dataStart();
	} catch (FileNotFoundException e) {
		
		e.printStackTrace();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	if (memValue.containsKey(zipCode)) {
        return memValue.get(zipCode);
    } else {
    	double sumMarketValue = 0;
        int averageMarketValue = 0;
        if (populationMap.containsKey(zipCode)) {
            ZipCode code = populationMap.get(zipCode);
            for (Property property : code.getProperties()) {
                sumMarketValue += property.getMarket_Value();
            }
            AverageGetter average = new AverageGetter(); 
            averageMarketValue = average.getAverage(sumMarketValue, code.getProperties().size());

        } else {
            System.out.println("No data available or invalid ZipCode");
        }
        memValue.put(zipCode, averageMarketValue);
        return averageMarketValue;
    }
}

   

//Enters 5- 3.5, finished
private HashMap<Integer, Integer> memArea = new HashMap<>(); //Memoization
public int getAverageLivableArea(int zipCode) {

	 try {
	        dataStart();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

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
	            //utilizing interface
	            AverageGetter averageGetter = new AverageGetter();
	            //trucating the the average total livable are as an integer
	            averageLivableArea = averageGetter.getAverage(sumLivableArea, numberOfProperties);
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
	 try {
	        dataStart();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
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


//Enters7

public int getTotalPopByZip(int zipCode) {
    if (populationMap.containsKey(zipCode)) {
        return populationMap.get(zipCode).getPopulation();
    } else {
        System.out.println("Invalid or unavailable ZIP Code.");
        return 0; // or handle the error accordingly
    }
}
private HashMap<Integer, Integer> mem2 = new HashMap<>(); //Memoization
public int getTotalPropValuePerZip(int zipCode2) {
	try {
		dataStart();
	} catch (FileNotFoundException e) {
		
		e.printStackTrace();
	} catch (IOException e) {
		
		e.printStackTrace();
	}
	if (mem2.containsKey(zipCode2)) {
        return mem2.get(zipCode2);
    } else {
    	int sumMarketValue = 0;

        if (populationMap.containsKey(zipCode2)) {
            ZipCode code = populationMap.get(zipCode2);
            for (Property property : code.getProperties()) {
                sumMarketValue += property.getMarket_Value();
            } 
        }
        mem2.put(zipCode2, sumMarketValue);
        return sumMarketValue;   
}
}



AverageInterface averageGetter = new AverageGetter();
public double calculateCorrelation(int zipCode4) {
	
	 Map<String, Double> positivityRates = csvCovidReader.calculatePositivityRates();
	 // Calculate mean of COVID-19 positivity rates
	    double meanPositivityRate = calculateMean(positivityRates);

     int totalPropertyValue =  getTotalPropValuePerZip(zipCode4);
        // Calculate average market value truncated to an integer
        //average market value gets truncated to an integer
     int av =getTotalMarketValuePerCapita(zipCode4);
	   int totalPopulation =  getTotalPopByZip(zipCode4);
	   NumberFormat format = NumberFormat.getInstance();
	   String formattedTotalPropertyValue = format.format(totalPropertyValue);
	    System.out.println("Total Property Value: $" + formattedTotalPropertyValue);
	    String formattedTotalPopulation = format.format(totalPopulation);
	    System.out.println("Total Population: " + formattedTotalPopulation);
	    
	    System.out.println("Avaerage Market Value per cap: " + av);
	    // Calculate mean of property values and population using the provided average calculator
	    double meanPropertyValue = averageGetter.getAverage(totalPropertyValue, populationMap.size());
	    
	    double meanPopulation = averageGetter.getAverage(totalPopulation, populationMap.size());
	   
	    // Calculate covariance between COVID-19 positivity rates and property values
	    double covariance = calculateCovariance(positivityRates, totalPropertyValue, meanPositivityRate, meanPropertyValue);
	   double  stdDevPositivityRate = calculateStandardDeviation(positivityRates, meanPositivityRate); 
	    double  stdDevPropertyValue = calculateStandardDeviation2(totalPropertyValue, meanPropertyValue);
	    double  correlationCoefficient = covariance / (stdDevPositivityRate * stdDevPropertyValue * meanPopulation);
	    //print statement down here to prevent division by percentage
	    DecimalFormat df = new DecimalFormat("#.##");
		meanPositivityRate = Double.parseDouble(df.format(meanPositivityRate*100));
		 System.out.println("Mean Positivity Rate: " + meanPositivityRate + "%");
	    return correlationCoefficient;
	}

private double calculateMean(Map<String, Double> data) {
    double sum = 0;
    for (double value : data.values()) {
        sum += value;
    }
    return sum / data.size();
}

private double calculateCovariance(Map<String, Double> data1, double totalPropertyValue, double mean1, double mean2) {
    double covariance = 0;
    for (String key : data1.keySet()) {
        covariance += (data1.get(key) - mean1) * (totalPropertyValue - mean2);
    }
    return covariance / data1.size();
}

private int memProp = 0;
public int getTotalNumberOfProperties() {
    if (memProp == 0) {
        int totalProperties = 0;
        for (ZipCode zipCode : populationMap.values()) {
            totalProperties += zipCode.getProperties().size();
        }
        memProp = totalProperties;
    }
    return memProp;
}

private double calculateStandardDeviation(Map<String, Double> totalPropertyValue, double mean) {
    double sumSquaredDifferences = 0;
    int totalNumberOfProperties = getTotalNumberOfProperties();
    // Calculate the sum of squared differences
    for (ZipCode zipCode : populationMap.values()) {
        for (Property property : zipCode.getProperties()) {
            sumSquaredDifferences += Math.pow(property.getMarket_Value() - mean, 2);
        }
    }
    // Calculate the variance
    double variance = sumSquaredDifferences / totalNumberOfProperties;
    // Calculate the standard deviation
    return Math.sqrt(variance);
}

    
        
private double calculateStandardDeviation2(double totalPropertyValue, double mean) {
        double sumSquaredDifferences = 0;
        int totalNumberOfProperties = getTotalNumberOfProperties();
        // Calculate the sum of squared differences
        for (ZipCode zipCode : populationMap.values()) {
            for (Property property : zipCode.getProperties()) {
                sumSquaredDifferences += Math.pow(property.getMarket_Value() - mean, 2);
            }
        }
    
    // Calculate the variance
    double variance = sumSquaredDifferences / totalNumberOfProperties;
    // Calculate the standard deviation
    return Math.sqrt(variance);
}
}
	
