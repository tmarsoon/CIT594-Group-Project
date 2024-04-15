package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

import edu.upenn.cit594.logging.Logger;


public class PropertiesReader {


	    int code = 0;
	    double livableArea = 0;
	    double marketValue = 0;
	    

	    int zipCodePos = -1;
	    int totalLivableAreaPos = -1;
	    int marketValuePos = -1;

	    
	    public PropertiesReader(String filename, Logger logger) {
	        super(filename, logger);
	    }

	   

