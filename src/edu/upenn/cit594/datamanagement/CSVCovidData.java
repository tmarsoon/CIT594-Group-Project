package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import edu.upenn.cit594.data.Covid19Data;
import edu.upenn.cit594.logging.Logger;

public class CSVCovidData extends FileSuperLogger{
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	// creating a covid map with the zip code and covid data 
	
	private Map <String, Covid19Data> covidMap;
	
	public CSVCovidData(String filename, Logger logger) {
	        super(filename, logger);
	        this.covidMap = new HashMap<>();
	    }

	public void csvCovidReader(String csvFile) throws FileNotFoundException, IOException, ParseException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))){
			String line;
			while ((line = reader.readLine()) != null) {
				//splitting by comma for csv
				String [] data = line.split(",");
				
				int zipCode = Integer.parseInt(data[0]);
				int negResults = Integer.parseInt(data[1]);
				int posResults = Integer.parseInt(data[2]);
				int testsConducted = negResults + posResults;
				int deaths = Integer.parseInt(data[3]);
				int hospitalizations = Integer.parseInt(data[4]);
				int partialVax = Integer.parseInt(data[5]);
				int fullVax = Integer.parseInt(data[6]);
				int boosters = Integer.parseInt(data[7]);
				Date timeStamp = dateFormat.parse(data[8]);
				
				String date = dateFormat.format(timeStamp);
						
				
				Covid19Data covidData = new Covid19Data(zipCode,timeStamp, partialVax, fullVax, negResults, posResults, testsConducted,deaths, hospitalizations,boosters );
				// add to map
				covidMap.put(date, covidData);
			}
				// Logging the covid
	            logger.logEvent(filename);
	        } catch (FileNotFoundException e) {
	            // Log file not found error
	            logger.logEvent("Error: COVID data file not found: " + filename);
	            e.printStackTrace();
	        } catch (IOException e) {
	            // Log IO error
	            logger.logEvent("Error reading COVID data file: " + filename);
	            e.printStackTrace();
	        } catch (ParseException | NumberFormatException e) {
	            // Log parsing errors
	            logger.logEvent("Error parsing COVID data file: " + filename);
	            e.printStackTrace();
	        }
	    }
	
	/**
     * Gets the number of vaccinations for the specified type along with the date.
     * 
     * @param vaxType  Type of vax
     * @param date  Date for retrieining vaccine data
     * @return Number of vaccinations that include both the type and dat
     * @throws ParseException 
     */
	public int getVaccinationNumber(String vaxType, String date) throws ParseException {
		
		try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //setting a lenient parser to false since when parsing dates, the parser can be lenient
            //setting object to false so the parser strictly follows the format
            dateFormat.setLenient(false);
            //parsing string to the object, Date
            Date dateAsDate = dateFormat.parse(date);
            //formatting the date back to a string
            String dateStr = dateFormat.format(dateAsDate);
         
            //creating covid19data object
            Covid19Data covidData = covidMap.get(dateStr);
            //if the date is out of range, there will be no data
            if (covidData == null) {
                System.out.println("No covid data available: " + dateStr);
                return 0;
            }
            //utilizing switch strategy to handle different vaccinations
            switch (vaxType.toLowerCase()) {
                case "full":
                	//return the number of the  fully vaccinated 
                    return covidData.getFullyVaccinated();
                case "partial":
                	//returning the number of the partially vaccinated
                    return covidData.getPartiallyVaccinated();
                    //returning 0 if vaccine doesn't exist
                default:
                    System.out.println("The vaccine does not exist: " + vaxType);
                    return 0;
            }
        } catch (ParseException e) {
        	//logging error if data cant be parsed and returning 0
            System.out.println("Invalid date format provided: " + date);
            return 0; 
        }
    }
}