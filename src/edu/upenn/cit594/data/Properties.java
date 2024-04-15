package edu.upenn.cit594.data;

public class Properties {
	
		   	
		//create three fields as per directions
			private double market_value;
			private double total_livable_area;
			private int zip_code;

		    public Properties(double market_value, double total_livable_area, int zip_code){
		        this.market_value = market_value;
		        this.total_livable_area= total_livable_area;
		        this.zip_code = zip_code;
		    }

		    //creating getters and setters for control and encapsulation 	
		    public double getMarket_Value() {
		        return market_value;
		    }

		    public void setMarket_Value(double market_value) {
		        this.market_value = market_value;
		    }
		    
		    public double getTotal_Livable_Area() {
		        return total_livable_area;
		    }

		    public void setTotal_Livable_Area(double total_livable_area) {
		        this.total_livable_area = total_livable_area;
		    }
		    
		    public int getZip_Code() {
		        return zip_code;
		    }
		    
		    public void setZip_Code(int zip_code) {
		    	this.zip_code = zip_code;
		    }
	}
		    
		    
		 

