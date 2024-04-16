package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.upenn.cit594.data.Covid19Data;

public class CSVCovidData {

	public void csvCovidReader(String csvFile) throws FileNotFoundException, IOException, ParseException {
		String line;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
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
			}
		}
	}
}

