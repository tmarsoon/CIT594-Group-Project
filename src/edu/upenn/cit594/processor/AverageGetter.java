package edu.upenn.cit594.processor;

/*
 * This class implements the interface and provides a case-by-case strategy to calculate averages
 */
public class AverageGetter implements AverageInterface{
	 @Override
	    public int getAverage(double totalValue, double propertiesNumber){
	        return (int) (totalValue/propertiesNumber);
	    }
	}

