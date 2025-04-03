/**
 * Project Name: Gradebook
 * File Name: FinalGrade.java
 * Course: CSC 335 Spring 2025
 * Purpose: Represents final letter grades (A-E) with corresponding GPA values.
 *          Used for calculating student GPAs and storing course final grades.
 */

package main.model;

public enum FinalGrade {
	A(4.0), B(3.0), C(2.0), D(1.0), E(0.0);

    private final double gpaValue;
    
    /**
     * Constructs a FinalGrade enum with its GPA value.
     * @param gpaValue Numeric GPA equivalent
     */
    FinalGrade(double gpaValue) {
        this.gpaValue = gpaValue;
    }
    
    /**
     * Gets the numeric GPA value for this grade.
     * @return GPA value as double
     */
    public double getGpaValue() {
        return gpaValue;
    }
}