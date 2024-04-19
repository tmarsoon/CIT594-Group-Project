package edu.upenn.cit594.processor;

/*
 * This interface essentially designs a contract for implementing different strategies for calculating averages
 */
public interface AverageInterface {
	 int getAverage(double totalValue, double propertiesNumber);
}
