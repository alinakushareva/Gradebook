/**
 * Project Name: Gradebook
 * File Name: FinalGrade.java
 * Course: CSC 335 Spring 2025
 * Purpose: Represents final letter grades (A-E) with corresponding GPA values.
 *          Used for calculating student GPAs and storing course final grades.
 */

package model;

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
    
    /**
     * Returns a FinalGrade based on a numeric percentage.
     * @param percentage Grade percentage (0–100)
     * @return FinalGrade enum (A–E)
     */
    public static FinalGrade getLetterGrade(double percentage) {
        if (percentage >= 90.0) return FinalGrade.A;
        else if (percentage >= 80.0) return FinalGrade.B;
        else if (percentage >= 70.0) return FinalGrade.C;
        else if (percentage >= 60.0) return FinalGrade.D;
        else return FinalGrade.E;
    }

}