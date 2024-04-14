package edu.upenn.cit594.logging;

public class Logger {
	private static Logger instance;
	private PrintStream output;

	private Logger() {
		this.output = System.err;
	}
	
	public static Logger getInstance() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	/**
	 * logging the event
	 * @param event
	 */
	public void logEvent(String event) {
		// TODO: log event
	}
	
	/**
	 * sets the output file destination
	 * @param fileToWrite
	 */
	public void changeOutputDest(String fileToWrite) {
		//TODO implement 
	}
	
	/**
	 * creates the time stamp to log
	 */
	public void timeStampLog() {
		//TODO implement
	}
	
	/**
	 * logs the command line arguments
	 * @param args
	 */
	public void logCommandLineArgs(String[] args) {
		//TODO implement
	}
	
	
	/**
	 * logs the name of the input file
	 * @param inputFile
	 */
	public void logInputFile(String inputFile) {
		//TODO implement
	}
	
	/**
	 * logs the user input/responses
	 * @param userInput
	 */
	public void logUserInput(String userInput) {
		//TODO implement
	}
	
}
