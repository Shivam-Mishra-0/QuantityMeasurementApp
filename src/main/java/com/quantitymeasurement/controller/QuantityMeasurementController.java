package com.quantitymeasurement.controller;

import java.util.logging.Logger;

import com.quantitymeasurement.entity.QuantityDTO;
import com.quantitymeasurement.service.IQuantityMeasurementService;


public class QuantityMeasurementController {

    
    private static final Logger logger = Logger.getLogger(
        QuantityMeasurementController.class.getName()
    );

    private IQuantityMeasurementService quantityMeasurementService;

    
    public QuantityMeasurementController(IQuantityMeasurementService quantityMeasurementService) {
        this.quantityMeasurementService = quantityMeasurementService;
        logger.info("QuantityMeasurementController initialized.");
    }

    
    public boolean performComparison(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return quantityMeasurementService.compare(thisQuantityDTO, thatQuantityDTO);
    }

    
    public QuantityDTO performConversion(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return quantityMeasurementService.convert(thisQuantityDTO, thatQuantityDTO);
    }

    
    public QuantityDTO performAddition(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return quantityMeasurementService.add(thisQuantityDTO, thatQuantityDTO);
    }

    
    public QuantityDTO performAddition(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO) {
        return quantityMeasurementService.add(thisQuantityDTO, thatQuantityDTO, targetUnitDTO);
    }

    
    public QuantityDTO performSubtraction(
            QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return quantityMeasurementService.subtract(thisQuantityDTO, thatQuantityDTO);
    }

    
    public QuantityDTO performSubtraction(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO) {
        return quantityMeasurementService.subtract(thisQuantityDTO, thatQuantityDTO, targetUnitDTO);
    }

    /**
     * Delegates a division request to the service layer.
     *
     * @param thisQuantityDTO dividend quantity
     * @param thatQuantityDTO divisor quantity
     * @return numeric division result
     */
    public double performDivision(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return quantityMeasurementService.divide(thisQuantityDTO, thatQuantityDTO);
    }

    
    public void demonstrateComparison(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        System.out.println("--- Equality Demonstration ---");
        System.out.println("Operation: COMPARISON");
        System.out.println("This Quantity: " + thisQuantityDTO.getValue() + " " + thisQuantityDTO.getUnit());
        System.out.println("That Quantity: " + thatQuantityDTO.getValue() + " " + thatQuantityDTO.getUnit());
        try {
            boolean result = performComparison(thisQuantityDTO, thatQuantityDTO);
            System.out.println("Comparison Result: " + result);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    
    public void demonstrateConversion(QuantityDTO thisQuantityDTO, QuantityDTO targetQuantityDTO) {
        System.out.println("--- Conversion Demonstration ---");
        System.out.println("Operation: CONVERT");
        System.out.println("This Quantity: " + thisQuantityDTO.getValue() + " " + thisQuantityDTO.getUnit());
        System.out.println("Target Unit:   " + targetQuantityDTO.getUnit());
        try {
            QuantityDTO result = performConversion(thisQuantityDTO, targetQuantityDTO);
            System.out.println("Conversion Result: " + result.getValue() + " " + result.getUnit());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    
    public void demonstrateAddition(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        System.out.println("--- Addition Demonstration ---");
        System.out.println("Operation: ADD");
        System.out.println("This Quantity: " + thisQuantityDTO.getValue() + " " + thisQuantityDTO.getUnit());
        System.out.println("That Quantity: " + thatQuantityDTO.getValue() + " " + thatQuantityDTO.getUnit());
        try {
            QuantityDTO result = performAddition(thisQuantityDTO, thatQuantityDTO);
            System.out.println("Addition Result: " + result.getValue() + " " + result.getUnit());
        } catch (UnsupportedOperationException e) {
            System.out.println("Error: " + thisQuantityDTO.getUnit() + " does not support ADD operations.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Cannot perform arithmetic between different measurement categories: "
                + thisQuantityDTO.getMeasurementType() + " and " + thatQuantityDTO.getMeasurementType());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    
    public void demonstrateAddition(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO) {
        System.out.println("--- Addition Demonstration (with Target Unit) ---");
        System.out.println("Operation: ADD");
        System.out.println("This Quantity: " + thisQuantityDTO.getValue() + " " + thisQuantityDTO.getUnit());
        System.out.println("That Quantity: " + thatQuantityDTO.getValue() + " " + thatQuantityDTO.getUnit());
        System.out.println("Target Unit:   " + targetUnitDTO.getUnit());
        try {
            QuantityDTO result = performAddition(thisQuantityDTO, thatQuantityDTO, targetUnitDTO);
            System.out.println("Addition Result: " + result.getValue() + " " + result.getUnit());
        } catch (UnsupportedOperationException e) {
            System.out.println("Error: " + thisQuantityDTO.getUnit() + " does not support ADD operations.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Cannot perform arithmetic between different measurement categories: "
                + thisQuantityDTO.getMeasurementType() + " and " + thatQuantityDTO.getMeasurementType());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    
    public void demonstrateSubtraction(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        System.out.println("--- Subtraction Demonstration ---");
        System.out.println("Operation: SUBTRACT");
        System.out.println("This Quantity: " + thisQuantityDTO.getValue() + " " + thisQuantityDTO.getUnit());
        System.out.println("That Quantity: " + thatQuantityDTO.getValue() + " " + thatQuantityDTO.getUnit());
        try {
            QuantityDTO result = performSubtraction(thisQuantityDTO, thatQuantityDTO);
            System.out.println("Subtraction Result: " + result.getValue() + " " + result.getUnit());
        } catch (UnsupportedOperationException e) {
            System.out.println("Error: " + thisQuantityDTO.getUnit() + " does not support SUBTRACT operations.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: Cannot perform arithmetic between different measurement categories.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    
    public void demonstrateDivision(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        System.out.println("--- Division Demonstration ---");
        System.out.println("Operation: DIVIDE");
        System.out.println("This Quantity: " + thisQuantityDTO.getValue() + " " + thisQuantityDTO.getUnit());
        System.out.println("That Quantity: " + thatQuantityDTO.getValue() + " " + thatQuantityDTO.getUnit());
        try {
            double result = performDivision(thisQuantityDTO, thatQuantityDTO);
            System.out.println("Division Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (UnsupportedOperationException e) {
            System.out.println("Error: " + thisQuantityDTO.getUnit() + " does not support DIVIDE operations.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}