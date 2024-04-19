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
                int zipCode = Integer.parseInt(data[0]);
                int population = Integer.parseInt(data[1]);
                
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
        } catch (ParseException, NumberFormatException e) {
            // Log parsing errors
            logger.logEvent("Error: Parsing covid data file - " + filename);
            e.printStackTrace();
        }
    }
    
    public Map<Integer, ZipCode> getPopulationMap() {
        return populationMap;
    }
}