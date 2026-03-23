package com.quantitymeasurement.entity;


public class QuantityDTO {

    
    public interface IMeasurableUnit {
        public String getUnitName();
        public String getMeasurementType();
    }

    
    public enum LengthUnit implements IMeasurableUnit {
        FEET,
        INCHES,
        YARDS,
        CENTIMETERS;

        
        @Override
        public String getUnitName() {
            return this.name();
        }

        
        @Override
        public String getMeasurementType() {
            return this.getClass().getSimpleName();
        }
    }

    
    public enum VolumeUnit implements IMeasurableUnit {
        LITRE,
        MILLILITRE,
        GALLON;

        
        @Override
        public String getUnitName() {
            return this.name();
        }

        
        @Override
        public String getMeasurementType() {
            return this.getClass().getSimpleName();
        }
    }

    
    public enum WeightUnit implements IMeasurableUnit {
        KILOGRAM,
        GRAM,
        POUND;

        
        @Override
        public String getUnitName() {
            return this.name();
        }

        
        @Override
        public String getMeasurementType() {
            return this.getClass().getSimpleName();
        }
    }

    
    public enum TemperatureUnit implements IMeasurableUnit {
        CELSIUS, FAHRENHEIT, KELVIN;

        
        @Override
        public String getUnitName() {
            return this.name();
        }

        
        @Override
        public String getMeasurementType() {
            return this.getClass().getSimpleName();
        }
    }

    
    public double value;

    
    public String unit;

    
    public String measurementType;

    
    public QuantityDTO(double value, IMeasurableUnit unit) {
        this.value = value;
        this.unit = unit.getUnitName();
        this.measurementType = unit.getMeasurementType();
    }

    
    public QuantityDTO(double value, String unit, String measurementType) {
        this.value = value;
        this.unit = unit;
        this.measurementType = measurementType;
    }

    
    public double getValue() {
        return value;
    }

    
    public String getUnit() {
        return unit;
    }

    
    public String getMeasurementType() {
        return measurementType;
    }

    
    @Override
    public String toString() {
        return String.format("%s %s", Double.toString(value).replace("\\.0+$", ""), unit);
    }
}
