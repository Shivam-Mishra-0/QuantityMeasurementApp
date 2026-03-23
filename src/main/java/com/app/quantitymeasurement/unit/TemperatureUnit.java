package com.app.quantitymeasurement.unit;

import java.util.function.Function;


public enum TemperatureUnit implements IMeasurable {

    CELSIUS(
        v -> v,                          
        v -> v                           
    ),
    FAHRENHEIT(
        v -> (v - 32.0) * 5.0 / 9.0,    
        v -> v * 9.0 / 5.0 + 32.0       
    ),
    KELVIN(
        v -> v - 273.15,                 
        v -> v + 273.15                  
    );

    
    private final Function<Double, Double> toBase;
    private final Function<Double, Double> fromBase;

    TemperatureUnit(Function<Double, Double> toBase, Function<Double, Double> fromBase) {
        this.toBase   = toBase;
        this.fromBase = fromBase;
    }

    @Override
    public double convertToBaseUnit(double value) {
        return toBase.apply(value);
    }

    @Override
    public double convertFromBaseUnit(double baseValue) {
        return fromBase.apply(baseValue);
    }

    public double convertTo(double value, TemperatureUnit target) {
        return target.convertFromBaseUnit(convertToBaseUnit(value));
    }

    @Override
    public String getUnitName() { return this.name(); }

    @Override
    public String getMeasurementType() { return this.getClass().getSimpleName(); }

    @Override
    public IMeasurable getUnitInstance(String unitName) {
        for (TemperatureUnit unit : TemperatureUnit.values()) {
            if (unit.getUnitName().equalsIgnoreCase(unitName)) return unit;
        }
        throw new IllegalArgumentException("Invalid temperature unit: " + unitName);
    }
}
