
package com.quantitymeasurement.entity;

import java.util.function.DoubleBinaryOperator;
import com.quantitymeasurement.unit.IMeasurable;
import com.quantitymeasurement.unit.SupportsArithmetic;


public final class Quantity<U extends IMeasurable> {

   
    private final double value;

    
    private final U unit;

    
    private static final double EPSILON = 1e-6;

    
    private static final double ROUND_SCALE = 1e6;

   
    private enum ArithmeticOperation {

        ADD((a, b) -> a + b),

        SUBTRACT((a, b) -> a - b),

        DIVIDE((a, b) -> {
            if (b == 0.0) {
                throw new ArithmeticException("Division by zero");
            }
            return a / b;
        });

        private final DoubleBinaryOperator operator;

        ArithmeticOperation(DoubleBinaryOperator operator) {
            this.operator = operator;
        }

        
        public double compute(double a, double b) {
            return operator.applyAsDouble(a, b);
        }
    }

    
    public Quantity(double value, U unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException("Value must be a finite number");
        }
        this.value = value;
        this.unit  = unit;
    }

   
    public double getValue() {
        return value;
    }

    
    public U getUnit() {
        return unit;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Quantity<?> other)) return false;
        if (this.unit.getClass() != other.unit.getClass()) return false;
        double thisBase  = unit.convertToBaseUnit(value);
        double otherBase = other.unit.convertToBaseUnit(other.value);
        return Math.abs(thisBase - otherBase) < EPSILON;
    }

    
    public Quantity<U> convertTo(U targetUnit) {
        validateTargetUnit(targetUnit);
        double baseValue  = unit.convertToBaseUnit(value);
        double converted  = targetUnit.convertFromBaseUnit(baseValue);
        return new Quantity<>(converted, targetUnit);
    }

    
    public Quantity<U> add(Quantity<? extends IMeasurable> other) {
        validateQuantity(other);
        double baseResult = performArithmetic(other, ArithmeticOperation.ADD);
        double result     = unit.convertFromBaseUnit(baseResult);
        double rounded    = Math.round(result * ROUND_SCALE) / ROUND_SCALE;
        return new Quantity<>(rounded, unit);
    }

    
    public Quantity<U> add(Quantity<? extends IMeasurable> other, U targetUnit) {
        validateQuantity(other);
        validateTargetUnit(targetUnit);
        validateArithmeticSupport(targetUnit);
        double baseResult = performArithmetic(other, ArithmeticOperation.ADD);
        double result     = targetUnit.convertFromBaseUnit(baseResult);
        double rounded    = Math.round(result * ROUND_SCALE) / ROUND_SCALE;
        return new Quantity<>(rounded, targetUnit);
    }

    
    public Quantity<U> subtract(Quantity<? extends IMeasurable> other) {
        validateQuantity(other);
        double baseResult = performArithmetic(other, ArithmeticOperation.SUBTRACT);
        double result     = unit.convertFromBaseUnit(baseResult);
        double rounded    = Math.round(result * ROUND_SCALE) / ROUND_SCALE;
        return new Quantity<>(rounded, unit);
    }

    
    public Quantity<U> subtract(Quantity<? extends IMeasurable> other, U targetUnit) {
        validateQuantity(other);
        validateTargetUnit(targetUnit);
        validateArithmeticSupport(targetUnit);
        double baseResult = performArithmetic(other, ArithmeticOperation.SUBTRACT);
        double result     = targetUnit.convertFromBaseUnit(baseResult);
        double rounded    = Math.round(result * ROUND_SCALE) / ROUND_SCALE;
        return new Quantity<>(rounded, targetUnit);
    }

   
    public double divide(Quantity<? extends IMeasurable> other) {
        validateQuantity(other);
        return performArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    
    private void validateQuantity(Quantity<? extends IMeasurable> other) {
        if (other == null) throw new IllegalArgumentException("Other quantity must not be null");
        if (this.unit == null || other.getUnit() == null)
            throw new IllegalArgumentException("Unit must not be null");
        if (!Double.isFinite(this.value) || !Double.isFinite(other.getValue()))
            throw new IllegalArgumentException("Values must be finite numbers");
        if (this.unit.getClass() != other.getUnit().getClass())
            throw new IllegalArgumentException("Cannot operate across different measurement categories");
    }

    
    private void validateTargetUnit(IMeasurable targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit must not be null");
        if (targetUnit.getClass() != this.unit.getClass())
            throw new IllegalArgumentException("Target unit must belong to same measurement category");
    }

    
    private void validateArithmeticSupport(IMeasurable unit) {
        if (!(unit instanceof SupportsArithmetic)) {
            throw new UnsupportedOperationException(
                "Arithmetic operations not supported for unit type: " + unit.getClass().getSimpleName());
        }
    }

    
    private double performArithmetic(
            Quantity<? extends IMeasurable> other, ArithmeticOperation operation) {
        validateArithmeticSupport(this.unit);
        validateArithmeticSupport(other.getUnit());
        double baseThis  = unit.convertToBaseUnit(value);
        double baseOther = other.getUnit().convertToBaseUnit(other.getValue());
        return operation.compute(baseThis, baseOther);
    }

    
    @Override
    public int hashCode() {
        long normalized = Math.round(unit.convertToBaseUnit(value) / EPSILON);
        return Long.hashCode(normalized);
    }

    
    @Override
    public String toString() {
        return String.format("%s %s",
            Double.toString(value).replace("\\.0+$", ""),
            unit.getUnitName());
    }
}
