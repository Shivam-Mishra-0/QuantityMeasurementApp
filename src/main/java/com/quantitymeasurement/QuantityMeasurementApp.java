package com.quantitymeasurement;

import com.quantitymeasurement.controller.QuantityMeasurementController;
import com.quantitymeasurement.entity.QuantityDTO;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.quantitymeasurement.service.QuantityMeasurementServiceImpl;


public class QuantityMeasurementApp {
	
	private static QuantityMeasurementApp instance;
	
	public QuantityMeasurementController controller;
	
	public IQuantityMeasurementRepository repository;
	
	
	private QuantityMeasurementApp() {
		this.repository = QuantityMeasurementCacheRepository.getInstance();
		QuantityMeasurementServiceImpl service = new QuantityMeasurementServiceImpl(
			this.repository
		);
		this.controller = new QuantityMeasurementController(service);
	}
	
	
	public static QuantityMeasurementApp getInstance() {
		if(instance == null) {
			instance = new QuantityMeasurementApp();
		}
		return instance;
	}
 	
	
	public static void main(String[] args) {
 
	    System.out.println("**** Quantity Measurement Application Started ****\n");
 
	    QuantityMeasurementApp app = QuantityMeasurementApp.getInstance();
	    QuantityMeasurementController controller = app.controller;
 
	   
	    
	    QuantityDTO quantity1 = new QuantityDTO(
	        2,
	        QuantityDTO.LengthUnit.FEET.getUnitName(),
	        QuantityDTO.LengthUnit.FEET.getMeasurementType()
	    );
 
	    QuantityDTO quantity2 = new QuantityDTO(
	        24,
	        QuantityDTO.LengthUnit.INCHES.getUnitName(),
	        QuantityDTO.LengthUnit.INCHES.getMeasurementType()
	    );
 
	    controller.demonstrateComparison(quantity1, quantity2);
 
	    System.out.println();
 
	   
	    
	    QuantityDTO temp1 = new QuantityDTO(
	        0,
	        QuantityDTO.TemperatureUnit.CELSIUS.getUnitName(),
	        QuantityDTO.TemperatureUnit.CELSIUS.getMeasurementType()
	    );
 
	    QuantityDTO temp2 = new QuantityDTO(
	        0,
	        QuantityDTO.TemperatureUnit.FAHRENHEIT.getUnitName(),
	        QuantityDTO.TemperatureUnit.FAHRENHEIT.getMeasurementType()
	    );
 
	    controller.demonstrateConversion(temp1, temp2);
 
	    System.out.println();
 
	    
	    
	    QuantityDTO tempTarget = new QuantityDTO(
	        0,
	        QuantityDTO.TemperatureUnit.CELSIUS.getUnitName(),
	        QuantityDTO.TemperatureUnit.CELSIUS.getMeasurementType()
	    );
 
	    controller.demonstrateAddition(temp1, temp2, tempTarget);
 
	    System.out.println();
 
	    
	    
	    QuantityDTO weightQuantity = new QuantityDTO(
	        10,
	        QuantityDTO.WeightUnit.KILOGRAM.getUnitName(),
	        QuantityDTO.WeightUnit.KILOGRAM.getMeasurementType()
	    );
 
	    controller.demonstrateAddition(quantity1, weightQuantity);
 
	    System.out.println();
 
	    
	    QuantityDTO yardsTarget = new QuantityDTO(
	        0,
	        QuantityDTO.LengthUnit.YARDS.getUnitName(),
	        QuantityDTO.LengthUnit.YARDS.getMeasurementType()
	    );
 
	    controller.demonstrateConversion(quantity2, yardsTarget);
	    System.out.println();
 
	    controller.demonstrateAddition(quantity1, quantity2);
	    System.out.println();
 
	    controller.demonstrateAddition(quantity1, quantity2, yardsTarget);
	    System.out.println();
 
	    controller.demonstrateSubtraction(quantity1, quantity2);
	    System.out.println();
 
	    controller.demonstrateDivision(quantity1, quantity2);
	    System.out.println();

	   
 
	    System.out.println("---- Stored Measurements ----");
 
	    app.repository
	        .getAllMeasurements()
	        .forEach(System.out::println);
 
	    System.out.println("\n**** Quantity Measurement Application Stopped ****");
	}


	public QuantityMeasurementController getController() {
		// TODO Auto-generated method stub
		return null;
	}


	public IQuantityMeasurementRepository getRepository() {
		// TODO Auto-generated method stub
		return null;
	}
}