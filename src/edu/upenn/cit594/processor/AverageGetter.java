package edu.upenn.cit594.processor;

public class AverageGetter implements AverageInterface{
	 @Override
	    public int getAverage(double total, double denominator){
	        return (int) (total/denominator);
	    }
	}

