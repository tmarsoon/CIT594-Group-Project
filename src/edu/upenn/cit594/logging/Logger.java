package edu.upenn.cit594.logging;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Logger {
	private static Logger instance;
	private PrintStream output;


	private Logger() {
		//Initializing the logger to have a standard error output since the the main method may be invoked multiple times
		this.output = System.err;
		
	}
	
	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	 /**
     * 	Logging an event with a timestamp
     * 	The timestamp here will be prnted before the message is sent to the logger
     * @param event  The event message
     */
	public void logEvent(String event) {
		String timestamp = String.valueOf(System.currentTimeMillis());
	        output.println(timestamp + " " + event);
	}
	
	/**
	 * sets the output file destination.
	 * If the previous destination is a file, it will be closed while switching.
	 * @param fileToWrite  File that the Logger wirtes to
	 */
	public void changeOutputDest(String fileToWrite) {
		//if a file exist, ouput should be closed
		if (output != System.err) {
			output.close();
		}
		//if the file is empty or contains no content, ouput the error and return void
		if (fileToWrite == null || fileToWrite.isEmpty()) {
			output = System.err;
			return;
		}
	//if necessary, attempt to append the log file
	  try {
          //append to the specified log file
          output = new PrintStream(new FileOutputStream(fileToWrite, true));
      } catch (FileNotFoundException e) {
          // If unable to create the log file, revert to standard error
          System.err.println("Unable to open log file: " + fileToWrite);
          output = System.err;
      }
  }
	
	
	/**
	 * logs the command line arguments
	 * @param args
	 */
	public void logCommandLineArgs(String[] args) {
		//creating a stringbuilder object since SB can be modified
		//append() will automatically intiate a capacity update to the object
		  StringBuilder stringBuild = new StringBuilder("Command Line Arguments: ");
		  //creating a for loop of single entry arguments with a space in between
		  for (String arg : args) {
		      stringBuild.append(arg).append(" ");
		        }
		        logEvent(stringBuild.toString());
		    }
	
	
	/**
	 * logs the name of the input file
	 * @param inputFile
	 */
	public void logInputFile(String inputFile) {
		logEvent("Input File: " + inputFile);
	}
	
	/**
	 * logs the user input/responses
	 * @param userInput
	 */
	public void logUserInput(String userInput) {
		 logEvent("User Input/Response: " + userInput);
	}
	
}
