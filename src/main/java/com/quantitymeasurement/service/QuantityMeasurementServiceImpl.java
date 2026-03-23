package com.quantitymeasurement.service;

import java.util.function.DoubleBinaryOperator;
import java.util.logging.Logger;

import com.quantitymeasurement.entity.QuantityDTO;
import com.quantitymeasurement.entity.QuantityDTO.IMeasurableUnit;
import com.quantitymeasurement.entity.QuantityMeasurementEntity;
import com.quantitymeasurement.entity.QuantityModel;
import com.quantitymeasurement.unit.IMeasurable;
import com.quantitymeasurement.repository.IQuantityMeasurementRepository;


public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    
    private static final Logger logger = Logger.getLogger(
        QuantityMeasurementServiceImpl.class.getName()
    );

   
    private IQuantityMeasurementRepository repository;

   
    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
        logger.info("QuantityMeasurementServiceImpl initialized with repository: "
            + repository.getClass().getSimpleName());
    }

   
    private enum Operation {
        COMPARE, CONVERT, ADD, SUBTRACT, DIVIDE
    }

   
    private enum ArithmeticOperation {
        ADD, SUBTRACT, DIVIDE
    }

    
    @Override
    public boolean compare(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        QuantityModel<IMeasurable> q1 = getQuantityModel(thisQuantityDTO);
        QuantityModel<IMeasurable> q2 = getQuantityModel(thatQuantityDTO);

        
        if (!q1.getUnit().getMeasurementType().equals(q2.getUnit().getMeasurementType())) {
            throw new IllegalArgumentException(
                "Cannot compare different measurement categories");
        }

        boolean result = compareBaseValues(q1, q2);

        repository.save(new QuantityMeasurementEntity(
            q1, q2, Operation.COMPARE.name(), result ? "Equal" : "Not Equal"
        ));

        logger.fine("COMPARE: " + q1 + " vs " + q2 + " => " + result);
        return result;
    }

   
    private <U extends IMeasurable> boolean compareBaseValues(
            QuantityModel<U> q1, QuantityModel<U> q2) {
        double base1 = q1.getUnit().convertToBaseUnit(q1.getValue());
        double base2 = q2.getUnit().convertToBaseUnit(q2.getValue());
        return Double.compare(base1, base2) == 0;
    }

    
    @Override
    public QuantityDTO convert(QuantityDTO thisQuantityDTO, QuantityDTO targetUnitDTO) {
        QuantityModel<IMeasurable> source = getQuantityModel(thisQuantityDTO);
        QuantityModel<IMeasurable> target = getQuantityModel(targetUnitDTO);

        double result;
        if (source.getUnit() instanceof com.quantitymeasurement.unit.TemperatureUnit) {
            
            result = convertTemperatureUnit(source, target.getUnit());
        } else {
            double baseValue = source.getUnit().convertToBaseUnit(source.getValue());
            result = target.getUnit().convertFromBaseUnit(baseValue);
        }

        QuantityModel<IMeasurable> resultModel = new QuantityModel<>(result, target.getUnit());

        repository.save(new QuantityMeasurementEntity(
            source, source, Operation.CONVERT.name(), resultModel
        ));

        logger.fine("CONVERT: " + source + " => " + resultModel);
        return getQuantityDTO(resultModel);
    }

    
    private <U extends IMeasurable> double convertTemperatureUnit(
            QuantityModel<U> source, U targetUnit) {
        double base = source.getUnit().convertToBaseUnit(source.getValue());
        return targetUnit.convertFromBaseUnit(base);
    }

    
    @Override
    public QuantityDTO add(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return add(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }

    
    @Override
    public QuantityDTO add(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO) {

        QuantityModel<IMeasurable> q1     = getQuantityModel(thisQuantityDTO);
        QuantityModel<IMeasurable> q2     = getQuantityModel(thatQuantityDTO);
        QuantityModel<IMeasurable> target = getQuantityModel(targetUnitDTO);

        validateArithmeticOperands(q1, q2, target.getUnit(), true);

        double resultBase = performArithmetic(q1, q2, ArithmeticOperation.ADD);
        double result     = target.getUnit().convertFromBaseUnit(resultBase);

        QuantityModel<IMeasurable> resultModel = new QuantityModel<>(result, target.getUnit());

        repository.save(new QuantityMeasurementEntity(
            q1, q2, Operation.ADD.name(), resultModel
        ));

        logger.fine("ADD: " + q1 + " + " + q2 + " => " + resultModel);
        return getQuantityDTO(resultModel);
    }

    
    @Override
    public QuantityDTO subtract(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        return subtract(thisQuantityDTO, thatQuantityDTO, thisQuantityDTO);
    }

    
    @Override
    public QuantityDTO subtract(
            QuantityDTO thisQuantityDTO,
            QuantityDTO thatQuantityDTO,
            QuantityDTO targetUnitDTO) {

        QuantityModel<IMeasurable> q1     = getQuantityModel(thisQuantityDTO);
        QuantityModel<IMeasurable> q2     = getQuantityModel(thatQuantityDTO);
        QuantityModel<IMeasurable> target = getQuantityModel(targetUnitDTO);

        validateArithmeticOperands(q1, q2, target.getUnit(), true);

        double resultBase = performArithmetic(q1, q2, ArithmeticOperation.SUBTRACT);
        double result     = target.getUnit().convertFromBaseUnit(resultBase);

        QuantityModel<IMeasurable> resultModel = new QuantityModel<>(result, target.getUnit());

        repository.save(new QuantityMeasurementEntity(
            q1, q2, Operation.SUBTRACT.name(), resultModel
        ));

        logger.fine("SUBTRACT: " + q1 + " - " + q2 + " => " + resultModel);
        return getQuantityDTO(resultModel);
    }

   
    @Override
    public double divide(QuantityDTO thisQuantityDTO, QuantityDTO thatQuantityDTO) {
        QuantityModel<IMeasurable> q1 = getQuantityModel(thisQuantityDTO);
        QuantityModel<IMeasurable> q2 = getQuantityModel(thatQuantityDTO);

        validateArithmeticOperands(q1, q2, null, false);

        double result = performArithmetic(q1, q2, ArithmeticOperation.DIVIDE);

        repository.save(new QuantityMeasurementEntity(
            q1, q2, Operation.DIVIDE.name(), String.valueOf(result)
        ));

        logger.fine("DIVIDE: " + q1 + " / " + q2 + " => " + result);
        return result;
    }

    private QuantityModel<IMeasurable> getQuantityModel(QuantityDTO quantity) {
        if (quantity == null)
            throw new IllegalArgumentException("QuantityDTO cannot be null");

        IMeasurable unit = getModelUnit(quantity.getMeasurementType(), quantity.getUnit());
        return new QuantityModel<>(quantity.getValue(), unit);
    }

    
    private QuantityDTO getQuantityDTO(QuantityModel<IMeasurable> quantity) {
        if (quantity == null)
            throw new IllegalArgumentException("QuantityModel cannot be null");

        IMeasurableUnit dtoUnit = getDTOUnit(
            quantity.getUnit().getMeasurementType(),
            quantity.getUnit().getUnitName()
        );
        return new QuantityDTO(quantity.getValue(), dtoUnit);
    }

    
    private IMeasurable getModelUnit(String measurementType, String unit) {
        switch (measurementType) {
            case "LengthUnit":
                return com.quantitymeasurement.unit.LengthUnit.valueOf(unit);
            case "WeightUnit":
                return com.quantitymeasurement.unit.WeightUnit.valueOf(unit);
            case "VolumeUnit":
                return com.quantitymeasurement.unit.VolumeUnit.valueOf(unit);
            case "TemperatureUnit":
                return com.quantitymeasurement.unit.TemperatureUnit.valueOf(unit);
            default:
                throw new IllegalArgumentException("Unsupported measurement type: " + measurementType);
        }
    }

    
    private IMeasurableUnit getDTOUnit(String measurementType, String unit) {
        switch (measurementType) {
            case "LengthUnit":
                return QuantityDTO.LengthUnit.valueOf(unit);
            case "WeightUnit":
                return QuantityDTO.WeightUnit.valueOf(unit);
            case "VolumeUnit":
                return QuantityDTO.VolumeUnit.valueOf(unit);
            case "TemperatureUnit":
                return QuantityDTO.TemperatureUnit.valueOf(unit);
            default:
                throw new IllegalArgumentException("Unsupported measurement type: " + measurementType);
        }
    }

   
    private <U extends IMeasurable> void validateArithmeticOperands(
            QuantityModel<U> q1,
            QuantityModel<U> q2,
            U targetUnit,
            boolean targetRequired) {

        if (q1 == null || q2 == null)
            throw new IllegalArgumentException("Operands cannot be null");

        String type1 = q1.getUnit().getMeasurementType();
        String type2 = q2.getUnit().getMeasurementType();

        if (!type1.equals(type2)) {
            throw new IllegalArgumentException(
                "Cannot perform arithmetic on different measurement categories");
        }

        
        if (type1.equals("TemperatureUnit")) {
            throw new UnsupportedOperationException(
                "Arithmetic operations are not supported for temperature units");
        }

        if (targetRequired && targetUnit == null)
            throw new IllegalArgumentException("Target unit is required");
    }

    
    private <U extends IMeasurable> double performArithmetic(
            QuantityModel<U> q1,
            QuantityModel<U> q2,
            ArithmeticOperation operation) {

        double base1 = q1.getUnit().convertToBaseUnit(q1.getValue());
        double base2 = q2.getUnit().convertToBaseUnit(q2.getValue());

        if (operation == ArithmeticOperation.DIVIDE && base2 == 0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }

        DoubleBinaryOperator op;
        switch (operation) {
            case ADD:      op = (a, b) -> a + b; break;
            case SUBTRACT: op = (a, b) -> a - b; break;
            case DIVIDE:   op = (a, b) -> a / b; break;
            default: throw new IllegalArgumentException("Invalid arithmetic operation");
        }
        return op.applyAsDouble(base1, base2);
    }
}
