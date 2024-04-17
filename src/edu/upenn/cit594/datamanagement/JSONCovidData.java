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
import edu.upenn.cit594.logging.Logger;

	public class JSONCovidData extends FileSuperLogger {

	    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    private Map<String, Covid19Data> covidMap;

	    public JSONCovidData(String filename, Logger logger) {
	        super(filename, logger);
	        this.covidMap = new HashMap<>();
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

	                int zipCode = Integer.parseInt(jsonObj.get("zip_code").toString());
	                int negResults = Integer.parseInt(jsonObj.get("NEG").toString());
	                int posResults = Integer.parseInt(jsonObj.get("POS").toString());
	                int testsConducted = negResults + posResults;
	                int deaths = Integer.parseInt(jsonObj.get("deaths").toString());
	                int hospitalizations = Integer.parseInt(jsonObj.get("hospitalized").toString());
	                int partialVax = Integer.parseInt(jsonObj.get("partially_vaccinated").toString());
	                int fullVax = Integer.parseInt(jsonObj.get("fully_vaccinated").toString());
	                int boosters = Integer.parseInt(jsonObj.get("boosted").toString());
	               //im not seeing a boosted section in the json file

	                Date timeStamp = dateFormat.parse(jsonObj.get("etl_timestamp").toString());
	                String date = dateFormat.format(timeStamp);

	                Covid19Data jsonCovidData = new Covid19Data(zipCode, timeStamp, partialVax, fullVax, negResults,
	                        posResults, testsConducted, deaths, hospitalizations, boosters);
	                covidMap.put(date, jsonCovidData);
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
	        } catch (NumberFormatException e) {
	            // Log parsing errors
	            logger.logEvent("Error: Parsing covid data file - " + filename);
	            e.printStackTrace();
	        }
	    }

	    public int getVaccinationNumber(String vaxType, String date) throws ParseException, java.text.ParseException {
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

