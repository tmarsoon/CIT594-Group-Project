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

public class CSVCovidData {
	
	// creating a covid map with the zip code and covid data 
	private Map <Date, Covid19Data> covidMap;
	
	public CSVCovidData() {
		this.covidMap = new HashMap<>();
	}

	public void csvCovidReader(String csvFile) throws FileNotFoundException, IOException, ParseException {
		String line;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
		try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))){
			while ((line = reader.readLine()) != null) {
				String [] data = line.split("\t");
				
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
				
				Covid19Data covidData = new Covid19Data(zipCode,timeStamp, partialVax, fullVax, negResults, posResults, testsConducted,deaths, hospitalizations,boosters );
				// add to map
				covidMap.put(timeStamp, covidData);
			}
		}
	}

	public int getVaccinationNumber(String vaxType, Date date) {
		
		// getting the data for zip code
		Covid19Data covidData = covidMap.get(date);
		
		// if the covidData does not exist for the zip code, return 0
		if (covidData == null) {
			return 0;
		}
		
		int vaccinationNumber = 0;
		
		if (vaxType.equalsIgnoreCase("full")) {
			// get the full vax info for the specified zip code
			vaccinationNumber = covidData.getFullyVaccinated();
		}
		
		else if (vaxType.equalsIgnoreCase("partial")){
			// get the partial vax info for the specified zip code
			vaccinationNumber = covidData.getPartiallyVaccinated();
		}
		
		else {
			System.out.println("The vaccination type specified does not exist: " + vaxType);
		}
		
		return vaccinationNumber;
	}
}
