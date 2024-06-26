package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import edu.upenn.cit594.data.ZipCode;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.data.Property;


public class PropertiesReader extends FileSuperLogger {
	 
	private Map<Integer, Property> propertyMap;
	 double market_value = 0;
	 double total_livable_area = 0;
	 int zip_code = 0;

	 	int  market_value_column = -1;
	 	int total_livable_area_column = -1;
	 	int zip_code_column = -1;


    public PropertiesReader(String filename, Logger logger) {
        super(filename, logger);
        this.propertyMap = new HashMap<>();
    }

    public void readProperties(Map<Integer, ZipCode> zipCodeMap)  {
    	try (BufferedReader br = new BufferedReader(new FileReader(filename))) {  
    	String header = br.readLine();
          if (header != null) {
              String[] columns = header.split(",");
              //Locate the column indices
              locateColumn(columns);
          }
          String line;
         
			
          while ((line = br.readLine()) != null) {
        	  
              String[] parts = line.split(",");
              // Extract market value and total livable area
              double marketValue = parseProperty(parts[market_value_column], 0.0);
             
              double totalLivableArea = parseProperty(parts[total_livable_area_column], 0.0);
              // Extract and validate ZIP code
              int zipCode = 0;
              try {
            	  zipCode = Integer.parseInt(parts[zip_code_column]);
            	// Convert the integer to a string
            	  String zipCodeStr = String.valueOf(zipCode);
            	  /*
            	   * This extracts a substring starting from index 0 up to a maximum of 5 characters, 
            	   * effectively truncating the ZIP code to 5 characters.
            	   */
                  String zip = zipCodeStr.substring(0, Math.min(5, zipCodeStr.length()));
              //the following expression is a regex technique to ensure exactly 5 characters are in the zip code
              //if the code has fewer than 5 characters or are non-numeric, the record is ignored
                  if (!zip.matches("\\d{5}")) {
                      // Log or handle invalid ZIP code
                     // System.out.println("Invalid ZIP code: " + zipCodeStr);
                      continue; 
                  }
              } catch (NumberFormatException e) {
                  //Handling invalid ZIP code
                  //System.out.println("Invalid ZIP code: " + parts[zip_code_column]);
                  continue; //Skip processing this line
              }
              
              // creating a ZipCode var: getting zip code from the map
              ZipCode code = zipCodeMap.get(zipCode);

              /*if the market value of the record can't be parsed, it will equal 0.0.
              //Therefore, we will ignore that entry. If non-numeric entries in the total_livable_area are encountered, 
               * its value will be 0.0, and still considered in the calculation.
               */
              
              if (zipCode != -1 && marketValue != 0.0 && code != null) {
                  //Create a Property object with the extracted data
                  Property property = new Property(marketValue, totalLivableArea, zipCode);
                  //Add property to your map or any other data structure
                  propertyMap.put(zipCode, property);
                  // adding the property to the Properties Set (ZipCode Class) for given zip code
                  code.addProperty(property);
       
                   }
          }
          //debug
          System.out.println("Zip code map size: " + zipCodeMap.size());
          
          // Logging the property data file
          logger.logEvent("Successfully read property data file: " + filename);
          
      } catch (FileNotFoundException e) {
          // Log file not found error/
          logger.logEvent("Error: Property data file not found - " + filename);
          e.printStackTrace();
      } catch (IOException e) {
          // Log IO error
          logger.logEvent("Error reading property data file: " + filename);
          e.printStackTrace();
      }
  }
    
    // Method to parse property values
    //this method makes sure that if the program encounters malformed data, it is ignored for the purposes of that calculation
    //Therefore, this produces the resulkt based only on well-formed data since malformished cannot be parsed
    private double parseProperty(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

 // Method to loop through columns and get matching column names since the files may differ
    private void locateColumn (String[] columns){
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equals("market_value")){
                market_value_column = i;
            }else if (columns[i].equals("total_livable_area")){
                total_livable_area_column = i;
            }else if (columns[i].equals("zip_code")){
                zip_code_column = i;
            }
        }
    }
}

