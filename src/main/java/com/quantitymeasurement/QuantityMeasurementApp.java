
package com.quantitymeasurement;

public class QuantityMeasurementApp {

	// Static method to demonstrate Weight equality
	public static boolean demonstrateWeightEquality(Weight weight1, Weight weight2) {
		return weight1.equals(weight2);
	}
		
	// Static method to demonstrate weight comparison using values and units
	public static boolean demonstrateWeightComparison(double value1, WeightUnit unit1,
		double value2, WeightUnit unit2) {
		Weight w1 = new Weight(value1, unit1);
		Weight w2 = new Weight(value2, unit2);
		boolean result = w1.equals(w2);
		System.out.println(w1 + " == " + w2 + " ? " + result);
		return result;
	}
		
	// Static method to demonstrate conversion using raw values
	public static Weight demonstrateWeightConversion(double value, WeightUnit fromUnit,
		WeightUnit toUnit) {
		Weight source = new Weight(value, fromUnit);
		Weight converted = source.convertTo(toUnit);
		System.out.println(source + " -> " + converted);
		return converted;
	}

	// Static method to demonstrate conversion using existing Weight object
	public static Weight demonstrateWeightConversion(Weight weight, WeightUnit toUnit) {
	    Weight converted = weight.convertTo(toUnit);
	    System.out.println(weight + " -> " + converted);
	    return converted;
	}
		
	// Static method to demonstrate addition of two Weight objects
	public static Weight demonstrateWeightAddition(Weight weight1, Weight weight2) {
	    Weight sum = weight1.add(weight2);
	    System.out.println(weight1 + " + " + weight2 + " = " + sum);
	    return sum;
	}
		
	// Static method to demonstrate addition with target unit
	public static Weight demonstrateWeightAddition(Weight weight1, Weight weight2, WeightUnit targetUnit) {
	    Weight sum = weight1.add(weight2, targetUnit);
	    System.out.println(weight1 + " + " + weight2 + " in " + targetUnit + " = " + sum);
	    return sum;
	}
		
		
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
		demonstrateWeightComparison(1.0, WeightUnit.KILOGRAM, 1000.0, WeightUnit.GRAM);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightComparison(2.204624, WeightUnit.POUND, 1.0, WeightUnit.KILOGRAM);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightComparison(453.592, WeightUnit.GRAM, 1.0, WeightUnit.POUND);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightComparison(1.0, WeightUnit.KILOGRAM, 1.0, WeightUnit.KILOGRAM);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightComparison(2.0, WeightUnit.POUND, 2.0, WeightUnit.POUND);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightComparison(500.0, WeightUnit.GRAM, 0.5, WeightUnit.KILOGRAM);
		System.out.println("|--------------------------------------------------------------|");
		
		demonstrateWeightConversion(1.0, WeightUnit.KILOGRAM, WeightUnit.GRAM);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightConversion(2.0, WeightUnit.POUND, WeightUnit.KILOGRAM);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightConversion(500.0, WeightUnit.GRAM, WeightUnit.POUND);
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightConversion(0.0, WeightUnit.KILOGRAM, WeightUnit.GRAM);
		System.out.println("|--------------------------------------------------------------|");
		
		demonstrateWeightConversion(
	        new Weight(-1.0, WeightUnit.KILOGRAM),
	        WeightUnit.GRAM
        );
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightAddition(
	        new Weight(1.0, WeightUnit.KILOGRAM),
	        new Weight(2.0, WeightUnit.KILOGRAM)
        );
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightAddition(
	        new Weight(1.0, WeightUnit.KILOGRAM),
	        new Weight(1000.0, WeightUnit.GRAM)
        );
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightAddition(
	        new Weight(500.0, WeightUnit.GRAM),
	        new Weight(0.5, WeightUnit.KILOGRAM)
        );
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightAddition(
	        new Weight(1.0, WeightUnit.KILOGRAM),
	        new Weight(1000.0, WeightUnit.GRAM),
	        WeightUnit.GRAM
        );
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightAddition(
	        new Weight(1.0, WeightUnit.POUND),
	        new Weight(453.592, WeightUnit.GRAM),
	        WeightUnit.POUND
        );
		System.out.println("|--------------------------------------------------------------|");
		demonstrateWeightAddition(
	        new Weight(2.0, WeightUnit.KILOGRAM),
	        new Weight(4.0, WeightUnit.POUND),
	        WeightUnit.KILOGRAM
        );
		System.out.println("|--------------------------------------------------------------|");
		
		System.out.println("Weight vs Length equality: " +
		    new Weight(1.0, WeightUnit.KILOGRAM)
		    .equals(new Length(1.0, LengthUnit.FEET))
		);
		System.out.println("|--------------------------------------------------------------|");
		
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
