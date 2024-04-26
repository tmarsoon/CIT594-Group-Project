package edu.upenn.cit594.datamanagement;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
//importing JSON related packages
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



import edu.upenn.cit594.data.Covid19Data;
import edu.upenn.cit594.data.VaccinationData;
import edu.upenn.cit594.logging.Logger;

	public class JSONCovidData extends FileSuperLogger {

	    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    private Map<String, Covid19Data> covidMap;
	    private Map <String, Map <Integer, VaccinationData>> covidVaxMap;

	    public JSONCovidData(String filename, Logger logger) {
	        super(filename, logger);
	        this.covidMap = new HashMap<>();
	        this.covidVaxMap = new HashMap<>();
	    }
	    //org.json.simple.parser.ParseException and java.text.ParseException causes a naming conflict
	    //both must be used to throw for the jsonArray parser and the dateFormat variable
	    //here we throw the import statement directly to bypass this conflict
	    public void jsonCovidReader(String jsonFile) throws FileNotFoundException, IOException, ParseException, java.text.ParseException {
	        JSONParser dataParser = new JSONParser();

	        try (FileReader fileReader = new FileReader(jsonFile)) {
	            JSONArray jsonArray = (JSONArray) dataParser.parse(fileReader);
	            
	            for (Object obj : jsonArray) {
	                JSONObject jsonObj = (JSONObject) obj;
	                
	                Object zipCodeObj = jsonObj.get("zip_code");
	                if ( zipCodeObj != null) {
	                	int zipCode = Integer.parseInt(zipCodeObj.toString());
	                }
	                int zipCode = getIntFromObj(jsonObj, "zip_code");
	                int negResults = getIntFromObj(jsonObj, "NEG");
	                int posResults = getIntFromObj(jsonObj,"POS");
	                int testsConducted = negResults + posResults;
	                int deaths = getIntFromObj(jsonObj, "deaths");
	                int hospitalizations = getIntFromObj(jsonObj,"hospitalized");
	                int partialVax = getIntFromObj(jsonObj, "partially_vaccinated");
	                int fullVax = getIntFromObj(jsonObj, "fully_vaccinated");
	                int boosters = getIntFromObj(jsonObj, "boosted");
	               //im not seeing a boosted section in the json file

	                Date timeStamp = getDateFromObj(jsonObj, "etl_timestamp");
	                String date = dateFormat.format(timeStamp);

	              //debug checking that it reads it right
					//System.out.println("For " + date + " and zip code " + zipCode + " the full vax is " + fullVax + " partial vax " + partialVax);
					
	                
	                Covid19Data jsonCovidData = new Covid19Data(zipCode, timeStamp, partialVax, fullVax, negResults,
	                        posResults, testsConducted, deaths, hospitalizations, boosters);
	                
	                VaccinationData vaccinationData = new VaccinationData(zipCode, partialVax, fullVax);
					
					
					// add to covid map and covid vax map
	                covidMap.put(date, jsonCovidData);
					
					if (!covidVaxMap.containsKey(vaccinationData)) {
						covidVaxMap.put(date, new HashMap<>());
					}
					
					// adding elements to nested map
					covidVaxMap.get(date).put(zipCode, vaccinationData);
					//debug checking that it adds to the map correctly
					//System.out.println("Adding data to covid vax map for " + date + " and zip code " + zipCode + " vaccination data " + vaccinationData.toString()); 

	                
	            }
	         // Logging the covid event
	            logger.logEvent(filename);
	        } catch (FileNotFoundException e) {
	            // Log file not found error
	            logger.logEvent("Error: Covid data isn't found - " + filename);
	            e.printStackTrace();
	        } catch (IOException e) {
	            // Log IO error
	            logger.logEvent("Error:  Reading covid data file - " + filename);
	            e.printStackTrace();
	        } catch (ParseException | NumberFormatException e) {
	            // Log parsing errors
	            logger.logEvent("Error: Parsing covid data file - " + filename);
	            e.printStackTrace();
	        }
	    }
	  
	    /**
	     * helper method to parse file for valid values 
	     * @param covidData
	     * @param key
	     * @return
	     */
	    private int getIntFromObj(JSONObject covidData, String key) {
	    	
	    	// get the object from the map
	    	Object objectToRetrieve = covidData.get(key);
	    	
	    	if (objectToRetrieve != null) {
	    		try {
	    			
	    			// return the parsed int
	    			return Integer.parseInt(objectToRetrieve.toString());
	    			
	    		} catch (NumberFormatException e) {
		            // Log parsing errors
		            logger.logEvent("Error: Parsing covid data file - " + filename);
		            e.printStackTrace();
	    		}
	    	}
	    	
	    	// otherwise return 0
	    	return 0;
	    }
	    
	    /**
	     * helper method to parse file for valid values 
	     * @param covidData
	     * @param key
	     * @return
	     * @throws java.text.ParseException 
	     */
	    private Date getDateFromObj(JSONObject covidDateData, String key) throws java.text.ParseException {
	    	
	    	// get the object from the map
	    	Object objectToRetrieve = covidDateData.get(key);
	    	
	    	if (objectToRetrieve != null) {
	    		try {
	    			// cleaning up the data by removing quotes mark at beginning and end of date field
					// using regex to replace these quotes with empty space to avoid parsing errors
	    			String covidDateStr = objectToRetrieve.toString().replaceAll("^\"|\"$", "");
			
	    			// return the parsed date
	    			
	    			return dateFormat.parse(covidDateStr);
	    		} catch (NumberFormatException e) {
		            // Log parsing errors
		            logger.logEvent("Error: Parsing covid data file - " + filename);
		            e.printStackTrace();
	    	}
	    	}
	    	
	    	// otherwise return empty date
	    	return new Date();
	    }
	    
	    
	    //here we throw the import statement directly to bypass this parseexception naming conflict
	    public int getVaccinationNumber(String vaxType, String date) throws java.text.ParseException {
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

			switch (vaxType.toLowerCase()) {
			    case "full":
			        return covidData.getFullyVaccinated();
			    case "partial":
			        return covidData.getPartiallyVaccinated();
			    default:
			        System.out.println("The vaccine does not exist: " + vaxType);
			        return 0;
			}
	    }
	 
	}
