package edu.upenn.cit594;

import java.io.File;

import edu.upenn.cit594.datamanagement.PopulationReader;
import edu.upenn.cit594.datamanagement.PropertiesReader;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.ui.UserInterface;

public class Main {

	public static void main(String[] args) {
	
		        // handle invalid number of runtime args error      
		        if(args.length != 4) {
		            System.out.println("Error in runtime arguments.");
		            return;
		        }
		        
		        // store args into local vars
		        String covidDataFile = args[0];
		        String propertiesDataFile = args[1];
		        String populationDataFile = args[2];
		        String logFile = args[3];
		       
		        File covidDataFiler = new File(covidDataFile);
		        File propertiesDataFiler = new File(propertiesDataFile);
		        File populationDataFiler = new File(populationDataFile);
		        File logFiler = new File(logFile);
		        
		        //if any of the files doesn't exist, display an error message
		        if (!covidDataFiler.exists() || !propertiesDataFiler.exists() || !populationDataFiler.exists() || !logFiler.exists()) {
		        	System.err.print(true);
		        	//return void to terminate
		        	return;
		        }
		        	/*      	            
		             * to handle the error that should be thrown for our program not creating/opening the specified log file,
		             * a try and catch block is needed since there is a chance that various exceptions can occur
		             */
		            try {
		            	if (!logFiler.createNewFile()) {
		            		 logFiler.createNewFile();
		            	}
		            } catch (Exception e) {
		            	System.out.println("Error has occured during the process of creating/opening the log file: " + e.getMessage());
		            	return;
		            }
		            
		            // handle invalid violations format
		            if (!isValidCovid19Format(covidDataFile)) {
		                System.err.print("Error: File format not supported in this program.");
		             }
		       
		      // Creating the logger 
		       Logger logger = Logger.getInstance();
		       logger.filename =logFile;
		       changeOutputDest(logFile);
		      

		        // Create Reader objects
		        
		        PropertiesReader propertiesReader = new PropertiesReader(propertiesDataFile, changeOutputDest);
		        PopulationReader populationReader = new PopulationReader(populationFile, logger);
		        
		     
		        
		        // Create Processor object and initialize data
		        System.out.println("Please wait while we process the data...");
		        Processor processor = new Processor(
		        		, propertiesReader, populationReader);

		        //instantiating new object with interfacedesign data type
		        UserInterface ui = new UserInterface(processor, logger);
		      //calling print method
		        ui.displayMenu();

		    }
		    
		    /**
		     * Private helper function to determine whether the covidDataData file contains the correct extention
		     * @param format args[0]
		     * @return
		     */
		    private static boolean isValidCovid19Format(String format) {
		        if (format.equals("csv") || format.equals("json")) {
		            return true;
		        } else {
		            return false;
		        }
		    }
		}

