package edu.upenn.cit594.datamanagement;

import java.util.Map;

import edu.upenn.cit594.data.ZipCode;
import edu.upenn.cit594.logging.Logger;
import java.io.*;
import java.text.ParseException;
import java.util.HashMap;

public class PopulationReader extends FileSuperLogger {
	//although utilizing a list would result in a similar run time with two columns, utilizing a map will result in faster retrieval
	private Map<Integer, ZipCode> populationMap;

    public PopulationReader(String filename, Logger logger) {
        super(filename, logger);
        this.populationMap = new HashMap<>();
    }

    //parse exception issue here
    public void readPopulationData(String csvFile) throws FileNotFoundException, IOException, ParseException, NumberFormatException {
       
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
        	 String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(","); // Assuming data is comma-separated
                int zipCode;
                int population;
                try {
                zipCode = Integer.parseInt(data[0]);
                population = Integer.parseInt(data[1]);               
            } catch (NumberFormatException e) {
                //if population figure is not an integer, we skip it
                continue;
            }
                //utilizing String.valueOf to convert integer to String and then check it is 5 digits in length
                if (String.valueOf(zipCode).length() != 5) {  
                  //if the zip_code is not 5 digits, we skip it
                    continue; // Skip processing this line
                }
                ZipCode zip_code = new ZipCode(zipCode, population);
                populationMap.put(zipCode,zip_code);
            }
            //Logging the population data file
            logger.logEvent(filename);
        } catch (FileNotFoundException e) {
            // Log file not found error
            logger.logEvent("Error: Covid data isn't found - " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            // Log IO error
            logger.logEvent("Error:  Reading covid data file - " + filename);
            e.printStackTrace();
        }
    }
    
    public Map<Integer, ZipCode> getPopulationMap() {
        return populationMap;
    }
}