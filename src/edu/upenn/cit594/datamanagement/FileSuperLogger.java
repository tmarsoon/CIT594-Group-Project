package edu.upenn.cit594.datamanagement;

import edu.upenn.cit594.logging.Logger;

/*
 * Unlike the solo project, 
 */
public abstract class FileSuperLogger {
	    
	//creating two variables with protected as the data type so we can access these within other classes of the same package
	    protected String filename;
	    protected Logger logger;
	    
	    public FileSuperLogger(String filename, Logger logger) {
	        this.filename = filename;
	        this.logger = logger;
	    }
	    
	    /**
	     * Calling the changeOutPutDest method from Logger class
	     * @param openLogger
	     */
	    protected void openLogger() {
	        if (logger == null) {
	            return;
	        }
	        logger.changeOutputDest(filename);
	    }

	}

