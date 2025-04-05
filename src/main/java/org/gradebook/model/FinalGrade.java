package org.gradebook.model;

/**
 * Enum representing the final letter grades that can be assigned to students.
 * Each grade has an associated GPA value.
 */
public enum FinalGrade {
    A(4.0),
    B(3.0),
    C(2.0),
    D(1.0),
    E(0.0);
    
    private final double gpaValue;
    
    /**
     * Constructor for FinalGrade enum.
     *
     * @param gpaValue the GPA value associated with this grade
     */
    FinalGrade(double gpaValue) {
        this.gpaValue = gpaValue;
    }
    
    /**
     * Gets the GPA value associated with this grade.
     *
     * @return the GPA value
     */
    public double getGpaValue() {
        return gpaValue;
    }
    
    /**
     * Converts a numeric percentage to a final letter grade.
     *
     * @param percentage the percentage (0-100)
     * @return the corresponding FinalGrade
     */
    public static FinalGrade fromPercentage(double percentage) {
        if (percentage >= 90.0) {
            return A;
        } else if (percentage >= 80.0) {
            return B;
        } else if (percentage >= 70.0) {
            return C;
        } else if (percentage >= 60.0) {
            return D;
        } else {
            return E;
        }
    }
} 