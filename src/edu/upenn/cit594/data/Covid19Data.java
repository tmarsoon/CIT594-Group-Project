package edu.upenn.cit594.data;

import java.util.Date;

/** 
 * @param zip_code  Represents where the vaccinations were provided.
 * @param timeStamp  Utilizes the Date data type to represent YYYY-MM-DD hh:mm:ss.
 * @param partiallyVaccinated  The total number of people who have their first vaccine dose, but not their second.
 * @param  fullyVaccinated  Total number of people who have received their second vaccine dose.
 * @param negativeResult  Total number of covid tests that had a negative result.
 * @param positiveResult  Total number of covid tests that had a positive result. 
 * @param testConducted  Total number of covid infection tests given
 * @param deaths  Total number of people that have died from covid
 * @param covidHospitalizaitons  Total number of all people (living or deceased) that have been administered to the hospital.
 * @param boostersGiven  Total number of boosters given. 
 */
public class Covid19Data {

	    private int zip_code;
	    private Date timeStamp;
	    private int partiallyVaccinated;
	    private int fullyVaccinated;
	    //the remaining variables are for the free-form analysis in part 3.7
	    private int negativeResult;
	    private int positiveResult;
	    private int testsConducted; 
	    private int deaths;
	    private int covidHospitalizations;
	    private int boostersGiven;
	   
	    
	    public Covid19Data(int zip_code, Date timeStamp, int partiallyVaccinated, int fullyVaccinated,
	    		int negativeResult, int positiveResult, int testsConducted, int deaths, int covidHospitalizations, int boostersGiven) { 
	    	
	        this.zip_code = zip_code;
	        this.timeStamp = timeStamp;
	        this.partiallyVaccinated = partiallyVaccinated; 
	        this.fullyVaccinated = fullyVaccinated;
	        //free-form
	        this.negativeResult = negativeResult; 
	        this.positiveResult = positiveResult; 
	        this.testsConducted = testsConducted;
	        this.deaths = deaths;
	        this.covidHospitalizations = covidHospitalizations;
	        this.boostersGiven = boostersGiven;	        
	    }

	    public int getZip_Code() {
	    	return zip_code;
	    }
	    
	    public Date getTimeStamp() {
	        return timeStamp;
	    }

	    public int getPartiallyVaccinated() {
	        return partiallyVaccinated;
	    }

	    public int getFullyVaccinated() {
	        return fullyVaccinated;
	    }
	    
	    public int getNegativeResult() {
	        return negativeResult;
	    }
	    
	    public int getTestsConducted() {
	    	testsConducted = negativeResult + positiveResult;
	        return testsConducted;
	    }

	    public int getDeaths() {
	        return deaths;
	    }

	    public int getCovidHospitalizations() {
	        return covidHospitalizations;
	    }
	    
	    public int getBoostersGiven() {
	    	return boostersGiven;
	}

}
