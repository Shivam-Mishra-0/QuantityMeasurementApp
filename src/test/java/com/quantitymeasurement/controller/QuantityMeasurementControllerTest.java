package com.quantitymeasurement.controller;
import com.quantitymeasurement.entity.QuantityDTO;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.quantitymeasurement.repository.QuantityMeasurementCacheRepository;
import com.quantitymeasurement.service.IQuantityMeasurementService;
import com.quantitymeasurement.service.QuantityMeasurementServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class QuantityMeasurementControllerTest {

    private static final double EPSILON = 1e-6;

    private QuantityMeasurementController controller;

    
    private QuantityDTO twoFeet;
    private QuantityDTO twentyFourInches;
    private QuantityDTO zeroYards;

    @BeforeEach
    public void setUp() {
        IQuantityMeasurementRepository repository =
            QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service =
            new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);

        twoFeet         = new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET);
        twentyFourInches = new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES);
        zeroYards        = new QuantityDTO(0,  QuantityDTO.LengthUnit.YARDS);
    }

    
    @Test
    public void testLayerSeparation_ControllerIndependence_StubService() {
        IQuantityMeasurementService stub = new IQuantityMeasurementService() {
            public boolean compare(QuantityDTO a, QuantityDTO b)  { return true; }
            public QuantityDTO convert(QuantityDTO a, QuantityDTO b) { return a; }
            public QuantityDTO add(QuantityDTO a, QuantityDTO b)  { return a; }
            public QuantityDTO add(QuantityDTO a, QuantityDTO b, QuantityDTO t) { return a; }
            public QuantityDTO subtract(QuantityDTO a, QuantityDTO b) { return a; }
            public QuantityDTO subtract(QuantityDTO a, QuantityDTO b, QuantityDTO t) { return a; }
            public double divide(QuantityDTO a, QuantityDTO b)    { return 42.0; }
        };

        QuantityMeasurementController stubController =
            new QuantityMeasurementController(stub);

        assertTrue(stubController.performComparison(twoFeet, twentyFourInches));
        assertEquals(42.0, stubController.performDivision(twoFeet, twentyFourInches), EPSILON);
    }

    
    @Test
    public void testController_NullService_OperationThrows() {
        QuantityMeasurementController nullServiceController =
            new QuantityMeasurementController(null);
        assertThrows(NullPointerException.class,
            () -> nullServiceController.performComparison(twoFeet, twentyFourInches));
    }

    
    @Test
    public void testPerformComparison_Equal_ReturnsTrue() {
        assertTrue(controller.performComparison(twoFeet, twentyFourInches));
    }

    @Test
    public void testPerformComparison_NotEqual_ReturnsFalse() {
        QuantityDTO oneFoot = new QuantityDTO(1, QuantityDTO.LengthUnit.FEET);
        assertFalse(controller.performComparison(oneFoot, twentyFourInches));
    }

   
    @Test
    public void testPerformConversion_InchesToYards_CorrectResult() {
        QuantityDTO result = controller.performConversion(twentyFourInches, zeroYards);
        assertEquals(0.666667, result.getValue(), EPSILON);
        assertEquals("YARDS",  result.getUnit());
    }

    @Test
    public void testPerformConversion_FeetToInches_CorrectResult() {
        QuantityDTO result = controller.performConversion(
            twoFeet,
            new QuantityDTO(0, QuantityDTO.LengthUnit.INCHES)
        );
        assertEquals(24.0,    result.getValue(), EPSILON);
        assertEquals("INCHES", result.getUnit());
    }

    @Test
    public void testPerformConversion_Temperature_CelsiusToFahrenheit() {
        QuantityDTO result = controller.performConversion(
            new QuantityDTO(100, QuantityDTO.TemperatureUnit.CELSIUS),
            new QuantityDTO(0,   QuantityDTO.TemperatureUnit.FAHRENHEIT)
        );
        assertEquals(212.0,       result.getValue(), EPSILON);
        assertEquals("FAHRENHEIT", result.getUnit());
    }

    
    @Test
    public void testPerformAddition_TwoOperands_DefaultUnit() {
        QuantityDTO result = controller.performAddition(twoFeet, twentyFourInches);
        assertEquals(4.0,   result.getValue(), EPSILON);
        assertEquals("FEET", result.getUnit());
    }

    @Test
    public void testPerformAddition_ThreeOperands_ExplicitTargetUnit() {
        QuantityDTO result = controller.performAddition(twoFeet, twentyFourInches, zeroYards);
        assertEquals(1.333333, result.getValue(), EPSILON);
        assertEquals("YARDS",  result.getUnit());
    }

    @Test
    public void testPerformAddition_Weight_KilogramPlusGram() {
        QuantityDTO result = controller.performAddition(
            new QuantityDTO(1,    QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(1000, QuantityDTO.WeightUnit.GRAM)
        );
        assertEquals(2.0,        result.getValue(), EPSILON);
        assertEquals("KILOGRAM", result.getUnit());
    }

    @Test
    public void testPerformAddition_Volume_LitrePlusMillilitre() {
        QuantityDTO result = controller.performAddition(
            new QuantityDTO(1,    QuantityDTO.VolumeUnit.LITRE),
            new QuantityDTO(1000, QuantityDTO.VolumeUnit.MILLILITRE)
        );
        assertEquals(2.0,     result.getValue(), EPSILON);
        assertEquals("LITRE", result.getUnit());
    }

    

    @Test
    public void testPerformSubtraction_TwoOperands_DefaultUnit() {
        QuantityDTO result = controller.performSubtraction(twoFeet, twentyFourInches);
        assertEquals(0.0,   result.getValue(), EPSILON);
        assertEquals("FEET", result.getUnit());
    }

    @Test
    public void testPerformSubtraction_ThreeOperands_ExplicitTargetUnit() {
        QuantityDTO result = controller.performSubtraction(
            new QuantityDTO(10, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(6,  QuantityDTO.LengthUnit.INCHES),
            new QuantityDTO(0,  QuantityDTO.LengthUnit.FEET)
        );
        assertEquals(9.5,   result.getValue(), EPSILON);
        assertEquals("FEET", result.getUnit());
    }

    
    @Test
    public void testPerformDivision_EqualQuantities_ReturnsOne() {
        double result = controller.performDivision(twoFeet, twentyFourInches);
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    public void testPerformDivision_FourFeetOverTwoFeet_ReturnsTwo() {
        double result = controller.performDivision(
            new QuantityDTO(4, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(2, QuantityDTO.LengthUnit.FEET)
        );
        assertEquals(2.0, result, EPSILON);
    }

    
    @Test
    public void testAllOperations_RouteCorrectly() {
        assertTrue(controller.performComparison(twoFeet, twentyFourInches));

        QuantityDTO converted = controller.performConversion(twentyFourInches, zeroYards);
        assertEquals("YARDS", converted.getUnit());

        QuantityDTO added = controller.performAddition(twoFeet, twentyFourInches);
        assertEquals(4.0, added.getValue(), EPSILON);

        QuantityDTO addedYards = controller.performAddition(twoFeet, twentyFourInches, zeroYards);
        assertEquals("YARDS", addedYards.getUnit());

        QuantityDTO subtracted = controller.performSubtraction(twoFeet, twentyFourInches);
        assertEquals(0.0, subtracted.getValue(), EPSILON);

        assertEquals(1.0, controller.performDivision(twoFeet, twentyFourInches), EPSILON);
    }

    
    @Test
    public void testDataFlow_InputPassedThrough_OutputReturnedUnmodified() {
        QuantityDTO result = controller.performAddition(
            new QuantityDTO(1,  QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES)
        );
        
        assertEquals(2.0,   result.getValue(), EPSILON);
        assertEquals("FEET", result.getUnit());
        assertEquals("LengthUnit", result.getMeasurementType());
    }

    

    @Test
    public void testBackwardCompatibility_UC1_CompareEqualLengths() {
        assertTrue(controller.performComparison(twoFeet, twentyFourInches));
    }

    @Test
    public void testBackwardCompatibility_UC5_ConvertInchesToYards() {
        QuantityDTO result = controller.performConversion(twentyFourInches, zeroYards);
        assertEquals(0.666667, result.getValue(), EPSILON);
    }

    @Test
    public void testBackwardCompatibility_UC6_AddFeetAndInches() {
        QuantityDTO result = controller.performAddition(twoFeet, twentyFourInches);
        assertEquals(4.0, result.getValue(), EPSILON);
    }

    @Test
    public void testBackwardCompatibility_UC7_AddWithTargetUnit() {
        QuantityDTO result = controller.performAddition(twoFeet, twentyFourInches, zeroYards);
        assertEquals(1.333333, result.getValue(), EPSILON);
        assertEquals("YARDS", result.getUnit());
    }

    @Test
    public void testBackwardCompatibility_SubtractFeetMinusInches() {
        QuantityDTO result = controller.performSubtraction(
            new QuantityDTO(10, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(6,  QuantityDTO.LengthUnit.INCHES)
        );
        assertEquals(9.5, result.getValue(), EPSILON);
    }

    @Test
    public void testBackwardCompatibility_DivideEqualQuantities() {
        assertEquals(1.0,
            controller.performDivision(twoFeet, twentyFourInches), EPSILON);
    }
}
