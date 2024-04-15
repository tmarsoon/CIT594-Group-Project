package edu.upenn.cit594.data;

import java.util.HashSet;


public class ZipCode {
	    private int zip_code;
	    private int population;
	    private HashSet<Property> properties;
	    private HashSet<Covid19Data> covid19Data;

	    public ZipCode(int zip_code, int population){
	        this.zip_code = zip_code;
	        this.population = population;
	        properties = new HashSet<>();
	        covid19Data = new HashSet<>();
	    }

	    public void addProperty(Property properties){
	        this.properties.add(properties);
	    }
	    
	    public void addCovid19Data(Covid19Data covid19Data) {
	        this.covid19Data.add(covid19Data);
	    }

	    public int getZip_Code() {
	        return zip_code;
	    }

	    public void setZip_Code(int zip_code) {
	        this.zip_code = zip_code;
	    }

	    public int getPopulation() {
	        return population;
	    }

	    public void setPopulation(int population) {
	        this.population = population;
	    }

	    public HashSet<Property> getProperties() {
	        return properties;
	    }

	    public void setProperties(HashSet<Property> properties) {
	        this.properties = properties;
	    }

	    public HashSet<Covid19Data> getCovid19Data() {
	        return covid19Data;
	    }

	    public void setCovid19Data(HashSet<Covid19Data> covid19Data) {
	        this.covid19Data = covid19Data;
	    }
}
