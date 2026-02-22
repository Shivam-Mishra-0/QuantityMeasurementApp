
package com.quantitymeasurement;

import java.util.Scanner;

public class QuantityMeasurementApp { 

	public static void main(String[] args) {
		Scanner sc=new Scanner(System.in);
		
		// Feet Measurement
		System.out.println("Enter First Entry (Feet) : ");
		double inputOne = sc.nextDouble();
		System.out.println("Enter Second Entry (Feet) : ");
		double inputTwo = sc.nextDouble();
		Feet f1 = new Feet(inputOne);
        Feet f2 = new Feet(inputTwo);
        System.out.println("Are equal? " + f1.equals(f2));
        
        // Inches Measurement 
        System.out.println("Enter First Entry (Inch) : ");
        double inputOneInch = sc.nextDouble();
        System.out.println("Enter Second Entry (Inch) : ");
        double inputTwoInch = sc.nextDouble();
        Inches i1 = new Inches(inputOneInch);
        Inches i2 = new Inches(inputTwoInch);
        System.out.println("Are equal? " + i1.equals(i2));
        
        
        //  Generic Length
        QuantityLength q1 = new QuantityLength(1.0, LengthUnit.FEET);
        QuantityLength q2 = new QuantityLength(12.0, LengthUnit.INCH);

        System.out.println("Comparing: " + q1 + " and " + q2);
        System.out.println("Equal: " + q1.equals(q2));

        QuantityLength q3 = new QuantityLength(1.0, LengthUnit.INCH);
        QuantityLength q4 = new QuantityLength(1.0, LengthUnit.INCH);
        
        System.out.println("Comparing: " + q3 + " and " + q4);
        System.out.println("Equal: " + q3.equals(q4));
	}
}
