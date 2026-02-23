
package com.quantitymeasurement;

public class QuantityMeasurementApp {

	// Static method to demonstrate Length equality
	public static boolean demonstrateLengthEquality(Length l1, Length l2) {
		return l1.equals(l2);
	}

	// Static method to demonstrate extended unit comparisons
	public static boolean demonstrateLengthComparison(double value1, LengthUnit unit1, 
													  double value2, LengthUnit unit2) {
		Length l1 = new Length(value1, unit1);
		Length l2 = new Length(value2, unit2);
		boolean result = l1.equals(l2);
		System.out.println(l1 + " == " + l2 + " ? " + result);
		return result;
	}

	// Static method to demonstrate conversion using raw values
	public static Length demonstrateLengthConversion(double value, LengthUnit fromUnit, 
																   LengthUnit toUnit) { 
		Length source = new Length(value, fromUnit); 
		Length converted = source.convertTo(toUnit); 
		System.out.println(source + " -> " + converted); 
		return converted; 
	}
	
	// Static method to demonstrate conversion using an existing Length object
	public static Length demonstrateLengthConversion(Length length, LengthUnit toUnit) { 
		Length converted = length.convertTo(toUnit); 
		System.out.println(length + " -> " + converted); 
		return converted; 
	}
	
	// Static method to demonstrate addition of two Length objects
	public static Length demonstrateLengthAddition(Length length1, Length length2) {
	    Length sum = length1.add(length2); // use Length.add() from UC6
	    System.out.println(length1 + " + " + length2 + " = " + sum);
	    return sum;
	}
	
	// Static method to demonstrate addition of two Length objects into target unit
    public static Length demonstrateLengthAddition(Length length1, Length length2, LengthUnit targetUnit) {
        Length sum = length1.add(length2, targetUnit);
        System.out.println(length1 + " + " + length2 + " in " + targetUnit + " = " + sum);
        return sum;
    }
	
	// Main method
	public static void main(String[] args) {
		demonstrateLengthComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCHES);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthComparison(1.0, LengthUnit.YARDS, 36.0, LengthUnit.INCHES);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthComparison(100.0, LengthUnit.CENTIMETERS, 39.3701, LengthUnit.INCHES);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthComparison(3.0, LengthUnit.FEET, 1.0, LengthUnit.YARDS);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthComparison(30.48, LengthUnit.CENTIMETERS, 1.0, LengthUnit.FEET);
		System.out.println("|--------------------------------------------------------------|");
		
		demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCHES); 
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET); 
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthConversion(36.0, LengthUnit.INCHES, LengthUnit.YARDS); 
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthConversion(30.48, LengthUnit.CENTIMETERS,LengthUnit.FEET); 
		System.out.println("|--------------------------------------------------------------|");
		
		demonstrateLengthConversion(new Length(-1.0, LengthUnit.FEET), LengthUnit.INCHES);
		System.out.println("|--------------------------------------------------------------|");

		demonstrateLengthAddition(
			new Length(1.0, LengthUnit.FEET),
			new Length(12.0, LengthUnit.INCHES)
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(12.0, LengthUnit.INCHES),
			new Length(1.0, LengthUnit.FEET)
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(1.0, LengthUnit.YARDS),
			new Length(3.0, LengthUnit.FEET)
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(2.54, LengthUnit.CENTIMETERS),
			new Length(1.0, LengthUnit.INCHES)
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(5.0, LengthUnit.FEET),
			new Length(0.0, LengthUnit.INCHES)
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(5.0, LengthUnit.FEET),
			new Length(-2.0, LengthUnit.FEET)
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
		    new Length(1.0, LengthUnit.FEET),
		    new Length(12.0, LengthUnit.INCHES),
		    LengthUnit.FEET
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(1.0, LengthUnit.FEET),                                  
			new Length(12.0, LengthUnit.INCHES),
			LengthUnit.INCHES
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(1.0, LengthUnit.FEET),
	        new Length(12.0, LengthUnit.INCHES),
		    LengthUnit.YARDS
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
		    new Length(1.0, LengthUnit.YARDS),
			new Length(3.0, LengthUnit.FEET),
		    LengthUnit.YARDS
		); 
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(36.0, LengthUnit.INCHES),
			new Length(1.0, LengthUnit.YARDS),
			LengthUnit.FEET
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(2.54, LengthUnit.CENTIMETERS),
			new Length(1.0, LengthUnit.INCHES),
			LengthUnit.CENTIMETERS
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(5.0, LengthUnit.FEET),
			new Length(0.0, LengthUnit.INCHES),
			LengthUnit.YARDS
		);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateLengthAddition(
			new Length(5.0, LengthUnit.FEET),
			new Length(-2.0, LengthUnit.FEET),
			LengthUnit.INCHES
		);
		System.out.println("|--------------------------------------------------------------|");
	}
}
