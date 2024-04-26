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
import edu.upenn.cit594.data.VaccinationData;
import edu.upenn.cit594.logging.Logger;

public class CSVCovidData extends FileSuperLogger{
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	// creating a covid map with the zip code and covid data 
	private Map <String, Map <Integer, VaccinationData>> covidVaxMap;
	private Map <String, Covid19Data> covidMap;
	
	
	
	public CSVCovidData(String filename, Logger logger) {
	        super(filename, logger);
	        this.covidVaxMap = new HashMap<>();
	        this.covidMap = new HashMap<>();;
	    }

	public void csvCovidReader(String csvFile) throws FileNotFoundException, IOException, ParseException, NumberFormatException {
		
		try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))){
			String line;
			
			// to skip the header portion of the file, we create a flag 
			boolean skipHeader = false;
			
			while ((line = reader.readLine()) != null) {
			
				
				// if the header has not been skipped, update the flag and continue to next line
				if (!skipHeader) {
					skipHeader = true;
					continue; // continuing without doing any action - ie skipping header
				}
				//splitting by comma for csv
				String [] data = line.split(",");
				
				// calling the parseInteger method to avoid empty or null fields
				int zipCode = parseInteger(data[0]);
				int negResults = parseInteger(data[1]);
				int posResults = parseInteger(data[2]);
				int deaths = parseInteger(data[3]);
				int hospitalizations = parseInteger(data[4]);
				int partialVax = parseInteger(data[5]);
				int fullVax = parseInteger(data[6]);
				int boosters = parseInteger(data[7]);
				Date timeStamp = parseDate(data[8]);
				
				if (timeStamp == null || zipCode ==-1 || partialVax ==-1 || fullVax ==-1 ) {
					continue;}
				
				int testsConducted = negResults + posResults;
				
				String[] dateArray = dateFormat.format(timeStamp).split(" ");
				String date = dateArray[0];
				
				//debug checking that it reads it right
				//System.out.println("For " + date + " and zip code " + zipCode + " the full vax is " + fullVax + " partial vax " + partialVax);
				
				Covid19Data covidData = new Covid19Data(zipCode,timeStamp, partialVax, partialVax, negResults, posResults, testsConducted,deaths, hospitalizations,boosters );
				VaccinationData vaccinationData = new VaccinationData(zipCode, partialVax, fullVax);
				
				
				// add to covid map and covid vax map
				covidMap.put(date, covidData);
				
				if (!covidVaxMap.containsKey(vaccinationData)) {
					covidVaxMap.put(date, new HashMap<>());
				}
				
				// adding elements to nested map
				covidVaxMap.get(date).put(zipCode, vaccinationData);
				//debug checking that it adds to the map correctly
				//System.out.println("Adding data to covid vax map for " + date + " and zip code " + zipCode + " vaccination data " + vaccinationData.toString()); 
		
				// Logging the covid event
	            logger.logEvent("Reading the covid data file - " + filename);
			}
			
			
			} catch (FileNotFoundException e) {
	            // Log file not found error
	            logger.logEvent("Error: Covid data isn't found - " + filename);
	            e.printStackTrace();
	        } catch (IOException e) {
	            // Log IO error
	            logger.logEvent("Error:  Reading covid data file - " + filename);
	            e.printStackTrace();
	        } catch (NumberFormatException e) {
	            // Log parsing errors
	            logger.logEvent("Error: Parsing covid data file! - " + filename);
	            e.printStackTrace();
	        }
	    }
	
	/**
	 * helper method to parse integers and avoid null or empty strings
	 * @param dataToParse
	 * @return
	 */
	private int parseInteger(String dataToParse){
		
		// if the value is not empty or null, return the integer parsed
		if (dataToParse != null && !dataToParse.isEmpty()) {
			try {
				return Integer.parseInt(dataToParse);
				
			} catch (NumberFormatException e) {
				// Log parsing errors
	            logger.logEvent("Error: Parsing covid data file - " + filename);
	            e.printStackTrace();
			}	
		}//otherwise return -1 
		return -1;
	}
	
	/**
	 * helper method to parse Dates and avoid null or empty strings
	 * @param dataToParse
	 * @return
	 * @throws ParseException 
	 */
	private Date parseDate(String dataToParse) throws ParseException {
		// if the value is not empty or null, return the integer parsed
			if (dataToParse != null && !dataToParse.isEmpty()) {
				
				// cleaning up the data by removing quotes mark at beginning and end of date field
				// using regex to replace these quotes with empty space to avoid parsing errors
				dataToParse = dataToParse.replaceAll("^\"|\"$", "");
				String dataToParse2 = dataToParse;
				//dataToParse2 = dataToParse2.replaceAll("MM/dd/yyyy HH:mm:ss", "yyyy-MM-dd HH:mm:00");
		       //impleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		        return dateFormat.parse(dataToParse2);
		    }
		
			 // otherwise, return null
			return null;
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
            //dateFormat.setLenient(false);
            //parsing string to the object, Date
            Date dateAsDate = dateFormat.parse(date);
            //formatting the date back to a string
            String dateStr = dateFormat.format(dateAsDate);
       
            //creating covid vaccination map
            Map <Integer, VaccinationData> vaccinationMap = covidVaxMap.get(dateStr);
         

            //if the date is out of range, there will be no data
            if (vaccinationMap == null ||  vaccinationMap.isEmpty() ) {
                System.out.println("No vaccination data available for the following date: " + dateStr);
                return 0;
            }
            
            for (VaccinationData vaccinationData: vaccinationMap.values()) {
            //utilizing switch strategy to handle different vaccinations
            switch (vaxType.toLowerCase()) {
                case "full":
                	//return the number of the  fully vaccinated 
                    return vaccinationData.getFullVaccinations();
                case "partial":
                	//returning the number of the partially vaccinated
                    return vaccinationData.getPartialVaccinations();
                    //returning 0 if vaccine doesn't exist
                default:
                    System.out.println("The vaccine type does not exist: " + vaxType);
                    return 0;
            	}
            }
            
            // if nothing matches,
            System.out.println("No vaccination data available: " + dateStr);
            return 0;
            
        } catch (ParseException e) {
        	//logging error if data cant be parsed and returning 0
            System.out.println("Invalid date format provided: " + date);
            return 0; 
        }
    }
	
	
	
	
	//free-form
	public Map<String, Double> calculatePositivityRates() {
		 Map<String, Double> positivityRates = new HashMap<>();
		 for (String date : covidMap.keySet()) {
		        Covid19Data covidData = covidMap.get(date);
		        int positiveCases = covidData.getPosResults();
		        int totalTests = covidData.getNegResults() + positiveCases;
		        double positivityRate = (double) positiveCases / totalTests;
		        positivityRates.put(date, positivityRate);
		    }
		    return positivityRates;
		}
}
