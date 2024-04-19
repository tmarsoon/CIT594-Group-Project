package edu.upenn.cit594.datamanagement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.upenn.cit594.data.ZipCode;
import edu.upenn.cit594.logging.Logger;
import edu.upenn.cit594.data.Property;


public class PropertiesReader extends FileSuperLogger {
    
    private Map<String, Property> propertyMap;

    public PropertiesReader(String filename, Logger logger) {
        super(filename, logger);
        this.propertyMap = new HashMap<>();
    }

    public void readProperties() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                Property property = parseProperty(line);
                if (property != null) {
                    propertyMap.put(property.getZipCode(), property);
                }
            }
            //Logging the property data file
            logger.logEvent(filename);
        } catch (FileNotFoundException e) {
            // Log file not found error
            logger.logEvent("Error: Property data file not found - " + filename);
            e.printStackTrace();
        } catch (IOException e) {
            // Log IO error
            logger.logEvent("Error reading property data file: " + filename);
            e.printStackTrace();
        }
    }

    private Property parseProperty(String line) {
        String[] parts = line.split(",");
     
        String marketValueStr = parts[0];
        String totalLivableAreaStr = parts[1];
        int zipCodeStr = parts[2];

        // Parsing market value
        double marketValue;
        try {
            marketValue = Double.parseDouble(marketValueStr);
        } catch (NumberFormatException e) {
            // Invalid market value, skip this line
            return null;
        }

        // Parsing total livable area
        double totalLivableArea;
        try {
            totalLivableArea = Double.parseDouble(totalLivableAreaStr);
        } catch (NumberFormatException e) {
            // Invalid total livable area, skip this line
            return null;
        }

        // Parsing ZIP code
        String zipCode = parseZipCode(zipCodeStr);

        // Create Property object
        return new Property(marketValue, totalLivableArea, zipCode);
    }

    private String parseZipCode(String zipCodeStr) {
        // Use only the first 5 characters and ensure they are numeric
        String zip = zipCodeStr.substring(0, Math.min(5, zipCodeStr.length()));
        if (zip.matches("\\d{5}")) {
            return zip;
        }
        return null; // Return null for invalid ZIP codes
    }

    public Map<String, Property> getPropertyMap() {
        return propertyMap;
    }
}