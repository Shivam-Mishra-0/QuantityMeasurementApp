package com.quantitymeasurement.service;
import com.quantitymeasurement.controller.QuantityMeasurementController;
import com.quantitymeasurement.entity.QuantityDTO;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.quantitymeasurement.repository.QuantityMeasurementCacheRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class QuantityMeasurementServiceTest {

    private static final double EPSILON = 1e-6;

    private QuantityMeasurementController controller;

    @BeforeEach
    public void setUp() {
        IQuantityMeasurementRepository repository =
            QuantityMeasurementCacheRepository.getInstance();
        IQuantityMeasurementService service =
            new QuantityMeasurementServiceImpl(repository);
        controller = new QuantityMeasurementController(service);
    }

    

    @Test
    public void testCompare_Length_FeetVsInches_Equal() {
        assertTrue(controller.performComparison(
            new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES)
        ));
    }

    @Test
    public void testCompare_Length_FeetVsInches_NotEqual() {
        assertFalse(controller.performComparison(
            new QuantityDTO(1,  QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES)
        ));
    }

    @Test
    public void testCompare_Length_YardVsFeet_Equal() {
        assertTrue(controller.performComparison(
            new QuantityDTO(1, QuantityDTO.LengthUnit.YARDS),
            new QuantityDTO(3, QuantityDTO.LengthUnit.FEET)
        ));
    }

    @Test
    public void testCompare_Weight_KilogramVsGram_Equal() {
        assertTrue(controller.performComparison(
            new QuantityDTO(1,    QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(1000, QuantityDTO.WeightUnit.GRAM)
        ));
    }

    @Test
    public void testCompare_Volume_LitreVsMillilitre_Equal() {
        assertTrue(controller.performComparison(
            new QuantityDTO(1,    QuantityDTO.VolumeUnit.LITRE),
            new QuantityDTO(1000, QuantityDTO.VolumeUnit.MILLILITRE)
        ));
    }

    @Test
    public void testCompare_Temperature_CelsiusVsFahrenheit_Equal() {
        assertTrue(controller.performComparison(
            new QuantityDTO(0,  QuantityDTO.TemperatureUnit.CELSIUS),
            new QuantityDTO(32, QuantityDTO.TemperatureUnit.FAHRENHEIT)
        ));
    }

    @Test
    public void testCompare_Temperature_100C_vs_212F_Equal() {
        assertTrue(controller.performComparison(
            new QuantityDTO(100, QuantityDTO.TemperatureUnit.CELSIUS),
            new QuantityDTO(212, QuantityDTO.TemperatureUnit.FAHRENHEIT)
        ));
    }

    

    @Test
    public void testConvert_Length_InchesToYards() {
        QuantityDTO result = controller.performConversion(
            new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES),
            new QuantityDTO(0,  QuantityDTO.LengthUnit.YARDS)
        );
        assertEquals(0.666667, result.getValue(), EPSILON);
        assertEquals("YARDS", result.getUnit());
    }

    @Test
    public void testConvert_Length_FeetToInches() {
        QuantityDTO result = controller.performConversion(
            new QuantityDTO(2, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(0, QuantityDTO.LengthUnit.INCHES)
        );
        assertEquals(24.0, result.getValue(), EPSILON);
        assertEquals("INCHES", result.getUnit());
    }

    @Test
    public void testConvert_Weight_KilogramToPound() {
        QuantityDTO result = controller.performConversion(
            new QuantityDTO(1, QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(0, QuantityDTO.WeightUnit.POUND)
        );
        assertEquals(2.204624, result.getValue(), EPSILON);
        assertEquals("POUND", result.getUnit());
    }

    @Test
    public void testConvert_Volume_LitreToMillilitre() {
        QuantityDTO result = controller.performConversion(
            new QuantityDTO(1, QuantityDTO.VolumeUnit.LITRE),
            new QuantityDTO(0, QuantityDTO.VolumeUnit.MILLILITRE)
        );
        assertEquals(1000.0, result.getValue(), EPSILON);
        assertEquals("MILLILITRE", result.getUnit());
    }

    @Test
    public void testConvert_Temperature_CelsiusToFahrenheit() {
        QuantityDTO result = controller.performConversion(
            new QuantityDTO(100, QuantityDTO.TemperatureUnit.CELSIUS),
            new QuantityDTO(0,   QuantityDTO.TemperatureUnit.FAHRENHEIT)
        );
        assertEquals(212.0, result.getValue(), EPSILON);
        assertEquals("FAHRENHEIT", result.getUnit());
    }

    @Test
    public void testConvert_Temperature_FahrenheitToCelsius() {
        QuantityDTO result = controller.performConversion(
            new QuantityDTO(32, QuantityDTO.TemperatureUnit.FAHRENHEIT),
            new QuantityDTO(0,  QuantityDTO.TemperatureUnit.CELSIUS)
        );
        assertEquals(0.0, result.getValue(), EPSILON);
        assertEquals("CELSIUS", result.getUnit());
    }

    

    @Test
    public void testAdd_Length_FeetPlusInches_DefaultUnit() {
        QuantityDTO result = controller.performAddition(
            new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES)
        );
        assertEquals(4.0,   result.getValue(), EPSILON);
        assertEquals("FEET", result.getUnit());
    }

    @Test
    public void testAdd_Length_FeetPlusInches_TargetYards() {
        QuantityDTO result = controller.performAddition(
            new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES),
            new QuantityDTO(0,  QuantityDTO.LengthUnit.YARDS)
        );
        assertEquals(1.333333, result.getValue(), EPSILON);
        assertEquals("YARDS", result.getUnit());
    }

    @Test
    public void testAdd_Weight_KilogramPlusGram() {
        QuantityDTO result = controller.performAddition(
            new QuantityDTO(1,    QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(1000, QuantityDTO.WeightUnit.GRAM)
        );
        assertEquals(2.0,        result.getValue(), EPSILON);
        assertEquals("KILOGRAM", result.getUnit());
    }

    @Test
    public void testAdd_Volume_LitrePlusMillilitre() {
        QuantityDTO result = controller.performAddition(
            new QuantityDTO(1,    QuantityDTO.VolumeUnit.LITRE),
            new QuantityDTO(1000, QuantityDTO.VolumeUnit.MILLILITRE)
        );
        assertEquals(2.0,     result.getValue(), EPSILON);
        assertEquals("LITRE", result.getUnit());
    }

    

    @Test
    public void testSubtract_Length_FeetMinusInches_DefaultUnit() {
        QuantityDTO result = controller.performSubtraction(
            new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES)
        );
        assertEquals(0.0,   result.getValue(), EPSILON);
        assertEquals("FEET", result.getUnit());
    }

    @Test
    public void testSubtract_Length_FeetMinusInches_TargetYards() {
        QuantityDTO result = controller.performSubtraction(
            new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES),
            new QuantityDTO(0,  QuantityDTO.LengthUnit.YARDS)
        );
        assertEquals(0.0,    result.getValue(), EPSILON);
        assertEquals("YARDS", result.getUnit());
    }

    @Test
    public void testSubtract_Weight_KilogramMinusGram() {
        QuantityDTO result = controller.performSubtraction(
            new QuantityDTO(2,    QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(500,  QuantityDTO.WeightUnit.GRAM)
        );
        assertEquals(1.5,        result.getValue(), EPSILON);
        assertEquals("KILOGRAM", result.getUnit());
    }

    @Test
    public void testSubtract_Volume_LitreMinusMillilitre() {
        QuantityDTO result = controller.performSubtraction(
            new QuantityDTO(5,    QuantityDTO.VolumeUnit.LITRE),
            new QuantityDTO(500,  QuantityDTO.VolumeUnit.MILLILITRE)
        );
        assertEquals(4.5,     result.getValue(), EPSILON);
        assertEquals("LITRE", result.getUnit());
    }

    

    @Test
    public void testDivide_Length_FeetOverInches_Equal() {
        double result = controller.performDivision(
            new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES)
        );
        assertEquals(1.0, result, EPSILON);
    }

    @Test
    public void testDivide_Length_FeetOverFeet() {
        double result = controller.performDivision(
            new QuantityDTO(4, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(2, QuantityDTO.LengthUnit.FEET)
        );
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    public void testDivide_Weight_KilogramOverKilogram() {
        double result = controller.performDivision(
            new QuantityDTO(10, QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(5,  QuantityDTO.WeightUnit.KILOGRAM)
        );
        assertEquals(2.0, result, EPSILON);
    }

    @Test
    public void testDivide_Volume_LitreOverLitre() {
        double result = controller.performDivision(
            new QuantityDTO(10, QuantityDTO.VolumeUnit.LITRE),
            new QuantityDTO(5,  QuantityDTO.VolumeUnit.LITRE)
        );
        assertEquals(2.0, result, EPSILON);
    }

    
    @Test
    public void testService_CompareEquality_SameUnit_Success() {
        assertTrue(controller.performComparison(
            new QuantityDTO(5, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(5, QuantityDTO.LengthUnit.FEET)
        ));
    }

    
    @Test
    public void testService_CompareEquality_DifferentUnit_Success() {
        
        assertTrue(controller.performComparison(
            new QuantityDTO(1, QuantityDTO.LengthUnit.YARDS),
            new QuantityDTO(3, QuantityDTO.LengthUnit.FEET)
        ));
        
        assertTrue(controller.performComparison(
            new QuantityDTO(1,    QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(1000, QuantityDTO.WeightUnit.GRAM)
        ));
    }

    
    @Test
    public void testService_CompareEquality_CrossCategory_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
            controller.performComparison(
                new QuantityDTO(1, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1, QuantityDTO.WeightUnit.KILOGRAM)
            )
        );
    }

    
    @Test
    public void testService_Convert_Length_Success() {
        QuantityDTO result = controller.performConversion(
            new QuantityDTO(1, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(0, QuantityDTO.LengthUnit.INCHES)
        );
        assertEquals(12.0,    result.getValue(), EPSILON);
        assertEquals("INCHES", result.getUnit());
    }

    @Test
    public void testService_Convert_Temperature_Success() {
        QuantityDTO result = controller.performConversion(
            new QuantityDTO(0,  QuantityDTO.TemperatureUnit.CELSIUS),
            new QuantityDTO(0,  QuantityDTO.TemperatureUnit.FAHRENHEIT)
        );
        assertEquals(32.0,        result.getValue(), EPSILON);
        assertEquals("FAHRENHEIT", result.getUnit());
    }

    
    @Test
    public void testService_Add_Length_Success() {
        QuantityDTO result = controller.performAddition(
            new QuantityDTO(1,  QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES)
        );
        assertEquals(2.0,   result.getValue(), EPSILON);
        assertEquals("FEET", result.getUnit());
    }

    @Test
    public void testService_Add_Weight_Success() {
        QuantityDTO result = controller.performAddition(
            new QuantityDTO(1,    QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(500,  QuantityDTO.WeightUnit.GRAM)
        );
        assertEquals(1.5,        result.getValue(), EPSILON);
        assertEquals("KILOGRAM", result.getUnit());
    }

    @Test
    public void testService_Add_Volume_Success() {
        QuantityDTO result = controller.performAddition(
            new QuantityDTO(2,    QuantityDTO.VolumeUnit.LITRE),
            new QuantityDTO(500,  QuantityDTO.VolumeUnit.MILLILITRE)
        );
        assertEquals(2.5,     result.getValue(), EPSILON);
        assertEquals("LITRE", result.getUnit());
    }

    
    @Test
    public void testService_Subtract_Length_Success() {
        QuantityDTO result = controller.performSubtraction(
            new QuantityDTO(10, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(6,  QuantityDTO.LengthUnit.INCHES)
        );
        assertEquals(9.5,   result.getValue(), EPSILON);
        assertEquals("FEET", result.getUnit());
    }

    @Test
    public void testService_Subtract_Weight_Success() {
        QuantityDTO result = controller.performSubtraction(
            new QuantityDTO(2,   QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(500, QuantityDTO.WeightUnit.GRAM)
        );
        assertEquals(1.5,        result.getValue(), EPSILON);
        assertEquals("KILOGRAM", result.getUnit());
    }

    
    @Test
    public void testService_Divide_Length_Success() {
        double result = controller.performDivision(
            new QuantityDTO(10, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET)
        );
        assertEquals(5.0, result, EPSILON);
    }

    @Test
    public void testService_Divide_CrossUnit_Success() {
        
        double result = controller.performDivision(
            new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES),
            new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET)
        );
        assertEquals(1.0, result, EPSILON);
    }

    
    @Test
    public void testService_Divide_ByZero_ThrowsArithmeticException() {
        assertThrows(ArithmeticException.class, () ->
            controller.performDivision(
                new QuantityDTO(10, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(0,  QuantityDTO.LengthUnit.FEET)
            )
        );
    }

   
    @Test
    public void testLayerSeparation_ServiceCanBeCalledDirectly() {
        IQuantityMeasurementService service =
            new QuantityMeasurementServiceImpl(
                QuantityMeasurementCacheRepository.getInstance()
            );

        boolean result = service.compare(
            new QuantityDTO(1, QuantityDTO.LengthUnit.YARDS),
            new QuantityDTO(36, QuantityDTO.LengthUnit.INCHES)
        );
        assertTrue(result);
    }

    
    @Test
    public void testService_AllMeasurementCategories_CompareAndAdd() {
        
        assertTrue(controller.performComparison(
            new QuantityDTO(1,  QuantityDTO.LengthUnit.YARDS),
            new QuantityDTO(36, QuantityDTO.LengthUnit.INCHES)
        ));

        
        assertTrue(controller.performComparison(
            new QuantityDTO(1,    QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(1000, QuantityDTO.WeightUnit.GRAM)
        ));

        
        assertTrue(controller.performComparison(
            new QuantityDTO(1,    QuantityDTO.VolumeUnit.LITRE),
            new QuantityDTO(1000, QuantityDTO.VolumeUnit.MILLILITRE)
        ));

        
        assertTrue(controller.performComparison(
            new QuantityDTO(0,  QuantityDTO.TemperatureUnit.CELSIUS),
            new QuantityDTO(32, QuantityDTO.TemperatureUnit.FAHRENHEIT)
        ));
    }

    
    @Test
    public void testService_NullDTO_Compare_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
            controller.performComparison(null,
                new QuantityDTO(1, QuantityDTO.LengthUnit.FEET))
        );
    }

    @Test
    public void testService_NullDTO_Add_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
            controller.performAddition(null,
                new QuantityDTO(1, QuantityDTO.LengthUnit.FEET))
        );
    }

    @Test
    public void testService_NullDTO_Subtract_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
            controller.performSubtraction(null,
                new QuantityDTO(1, QuantityDTO.LengthUnit.FEET))
        );
    }

    @Test
    public void testService_NullDTO_Divide_ThrowsIllegalArgument() {
        assertThrows(IllegalArgumentException.class, () ->
            controller.performDivision(null,
                new QuantityDTO(1, QuantityDTO.LengthUnit.FEET))
        );
    }

    
    @Test
    public void testService_ExceptionHandling_Temperature_Add_Throws() {
        assertThrows(UnsupportedOperationException.class, () ->
            controller.performAddition(
                new QuantityDTO(100, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(50,  QuantityDTO.TemperatureUnit.CELSIUS)
            )
        );
    }

    @Test
    public void testService_ExceptionHandling_Temperature_Subtract_Throws() {
        assertThrows(UnsupportedOperationException.class, () ->
            controller.performSubtraction(
                new QuantityDTO(100, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(50,  QuantityDTO.TemperatureUnit.CELSIUS)
            )
        );
    }

    @Test
    public void testService_ExceptionHandling_Temperature_Divide_Throws() {
        assertThrows(UnsupportedOperationException.class, () ->
            controller.performDivision(
                new QuantityDTO(100, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(50,  QuantityDTO.TemperatureUnit.CELSIUS)
            )
        );
    }

    @Test
    public void testService_ExceptionHandling_CrossCategory_Add_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
            controller.performAddition(
                new QuantityDTO(1, QuantityDTO.LengthUnit.FEET),
                new QuantityDTO(1, QuantityDTO.WeightUnit.KILOGRAM)
            )
        );
    }

    
    @Test
    public void testIntegration_EndToEnd_LengthAddition() {
        QuantityDTO q1 = new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET);
        QuantityDTO q2 = new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES);
        QuantityDTO target = new QuantityDTO(0, QuantityDTO.LengthUnit.YARDS);

        
        assertTrue(controller.performComparison(q1, q2));

        
        QuantityDTO converted = controller.performConversion(q2, target);
        assertEquals("YARDS", converted.getUnit());

        
        QuantityDTO added = controller.performAddition(q1, q2);
        assertEquals(4.0, added.getValue(), EPSILON);

        
        QuantityDTO addedYards = controller.performAddition(q1, q2, target);
        assertEquals("YARDS", addedYards.getUnit());

        
        QuantityDTO subtracted = controller.performSubtraction(q1, q2);
        assertEquals(0.0, subtracted.getValue(), EPSILON);

        
        assertEquals(1.0, controller.performDivision(q1, q2), EPSILON);
    }

    
    @Test
    public void testIntegration_EndToEnd_TemperatureAddition_IsRejected() {
        assertThrows(UnsupportedOperationException.class, () ->
            controller.performAddition(
                new QuantityDTO(100, QuantityDTO.TemperatureUnit.CELSIUS),
                new QuantityDTO(50,  QuantityDTO.TemperatureUnit.CELSIUS)
            )
        );
    }

    
    @Test
    public void testIntegration_EndToEnd_TemperatureConversion_Succeeds() {
        QuantityDTO result = controller.performConversion(
            new QuantityDTO(-40, QuantityDTO.TemperatureUnit.CELSIUS),
            new QuantityDTO(0,   QuantityDTO.TemperatureUnit.FAHRENHEIT)
        );
        assertEquals(-40.0,       result.getValue(), EPSILON);
        assertEquals("FAHRENHEIT", result.getUnit());
    }

    
    @Test
    public void testService_AllUnitImplementations_Convert() {
       
        assertEquals("INCHES", controller.performConversion(
            new QuantityDTO(1, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(0, QuantityDTO.LengthUnit.INCHES)).getUnit());

        
        assertEquals("GRAM", controller.performConversion(
            new QuantityDTO(1, QuantityDTO.WeightUnit.KILOGRAM),
            new QuantityDTO(0, QuantityDTO.WeightUnit.GRAM)).getUnit());

        
        assertEquals("MILLILITRE", controller.performConversion(
            new QuantityDTO(1, QuantityDTO.VolumeUnit.LITRE),
            new QuantityDTO(0, QuantityDTO.VolumeUnit.MILLILITRE)).getUnit());

        
        assertEquals("FAHRENHEIT", controller.performConversion(
            new QuantityDTO(100, QuantityDTO.TemperatureUnit.CELSIUS),
            new QuantityDTO(0,   QuantityDTO.TemperatureUnit.FAHRENHEIT)).getUnit());
    }

    
    @Test
    public void testEntity_OperationType_Tracking_Compare() {
        int before = QuantityMeasurementCacheRepository.getInstance()
            .getAllMeasurements().size();

        controller.performComparison(
            new QuantityDTO(1, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES)
        );

        int after = QuantityMeasurementCacheRepository.getInstance()
            .getAllMeasurements().size();

        
        assertEquals(before + 1, after);

        
        String lastOp = QuantityMeasurementCacheRepository.getInstance()
            .getAllMeasurements()
            .get(after - 1)
            .operation;
        assertEquals("COMPARE", lastOp);
    }

    @Test
    public void testEntity_OperationType_Tracking_Add() {
        int before = QuantityMeasurementCacheRepository.getInstance()
            .getAllMeasurements().size();

        controller.performAddition(
            new QuantityDTO(1, QuantityDTO.LengthUnit.FEET),
            new QuantityDTO(12, QuantityDTO.LengthUnit.INCHES)
        );

        int after = QuantityMeasurementCacheRepository.getInstance()
            .getAllMeasurements().size();
        assertEquals(before + 1, after);

        String lastOp = QuantityMeasurementCacheRepository.getInstance()
            .getAllMeasurements()
            .get(after - 1)
            .operation;
        assertEquals("ADD", lastOp);
    }

    
    @Test
    public void testScalability_ExistingOperations_ProduceSameResults_AfterFullSuiteRun() {
        QuantityDTO feet = new QuantityDTO(2,  QuantityDTO.LengthUnit.FEET);
        QuantityDTO inch = new QuantityDTO(24, QuantityDTO.LengthUnit.INCHES);

        assertEquals(true,  controller.performComparison(feet, inch));
        assertEquals(4.0,   controller.performAddition(feet, inch).getValue(),    EPSILON);
        assertEquals(0.0,   controller.performSubtraction(feet, inch).getValue(), EPSILON);
        assertEquals(1.0,   controller.performDivision(feet, inch),                EPSILON);
    }
}
