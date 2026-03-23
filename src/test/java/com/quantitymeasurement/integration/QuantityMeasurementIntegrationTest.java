package com.quantitymeasurement.integration;

import com.quantitymeasurement.QuantityMeasurementApp;
import com.quantitymeasurement.controller.QuantityMeasurementController;
import com.quantitymeasurement.entity.QuantityDTO;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class QuantityMeasurementIntegrationTest {

    private QuantityMeasurementApp app;
    private QuantityMeasurementController controller;
    private IQuantityMeasurementRepository repository;

    
    @BeforeAll
    public static void setUpTestEnvironment() {
        System.setProperty("app.env", "test");
    }

   
    @BeforeEach
    public void setUp() {
        app        = QuantityMeasurementApp.getInstance();
        controller = app.getController();
        repository = app.getRepository();
        repository.deleteAll();
    }

    
    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    
    @Test
    public void testEndToEndLengthComparison() {
        QuantityDTO q1 = new QuantityDTO(1.0,  "FEET",   "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(12.0, "INCHES", "LengthUnit");

        boolean result = controller.performComparison(q1, q2);

        assertTrue(result, "1 FOOT should equal 12 INCHES end-to-end");
    }

    
    @Test
    public void testEndToEndTemperatureConversion() {
        QuantityDTO thisDto = new QuantityDTO(0.0, "CELSIUS",    "TemperatureUnit");
        QuantityDTO thatDto = new QuantityDTO(0.0, "FAHRENHEIT", "TemperatureUnit");

        QuantityDTO resultDTO = controller.performConversion(thisDto, thatDto);

        assertNotNull(resultDTO, "Conversion result should not be null");
        assertEquals(32.0, resultDTO.getValue(), 0.01, "0°C should convert to 32°F");
    }

    
    @Test
    public void testRepositoryPersistence() {
        QuantityDTO q1 = new QuantityDTO(5.0, "FEET", "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(5.0, "FEET", "LengthUnit");

        controller.performAddition(q1, q2);

        assertTrue(repository.getAllMeasurements().size() > 0,
            "Repository should have at least 1 record after performAddition");
    }

    
    @Test
    public void testMultipleOperationsPersisted() {
        QuantityDTO q1 = new QuantityDTO(2.0,  "FEET",   "LengthUnit");
        QuantityDTO q2 = new QuantityDTO(24.0, "INCHES", "LengthUnit");

        controller.performComparison(q1, q2);
        controller.performAddition(q1, q2);
        controller.performSubtraction(q1, q2);
        controller.performDivision(q1, q2);

        assertEquals(4, repository.getTotalCount(),
            "All 4 operations should each produce one stored record");
    }

   
    @Test
    public void testWeightComparison_KilogramEqualsGram() {
        QuantityDTO kg = new QuantityDTO(1.0,    "KILOGRAM", "WeightUnit");
        QuantityDTO g  = new QuantityDTO(1000.0, "GRAM",     "WeightUnit");

        assertTrue(controller.performComparison(kg, g),
            "1 KILOGRAM should equal 1000 GRAM");
    }

    
    @Test
    public void testTemperatureArithmeticHandledGracefully() {
        QuantityDTO t1 = new QuantityDTO(10, "CELSIUS", "TemperatureUnit");
        QuantityDTO t2 = new QuantityDTO(20, "CELSIUS", "TemperatureUnit");

        
        assertDoesNotThrow(() -> controller.demonstrateAddition(t1, t2),
            "Temperature arithmetic should be handled gracefully without crashing");
    }
}