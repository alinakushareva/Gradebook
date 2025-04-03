/**
 * Project Name: Gradebook  
 * File Name: Grade.java  
 * Course: CSC 335 Spring 2025  
 * Purpose: Represents a grade for an assignment, storing points earned and max possible points.  
 *          Provides percentage calculation and access to grade components.  
 */
package model;

public class Grade {
    private final double pointsReceived;
    private final double maxPoints;

    /**
     * Constructs a Grade object.
     * @param pointsReceived Points earned by the student (must be ≥ 0 and ≤ maxPoints)
     * @param maxPoints Maximum possible points for the assignment (must be > 0)
     * @throws IllegalArgumentException if values are invalid
     */
    public Grade(double pointsReceived, double maxPoints) {
        if (maxPoints <= 0) {
            throw new IllegalArgumentException("Max points must be positive");
        }
        if (pointsReceived < 0 || pointsReceived > maxPoints) {
            throw new IllegalArgumentException("Points received must be between 0 and maxPoints");
        }
        this.pointsReceived = pointsReceived;
        this.maxPoints = maxPoints;
    }

    /**
     * Calculates the percentage score.
     * @return Percentage (0.0 to 100.0)
     */
    public double getPercentage() {
        return (pointsReceived / maxPoints) * 100.0;
    }

    /**
     * Gets the raw points earned.
     * @return Points received (always ≤ maxPoints)
     */
    public double getPointsReceived() {
        return pointsReceived;
    }

    /**
     * Gets the maximum possible points.
     * @return Max points for the assignment
     */
    public double getMaxPoints() {
        return maxPoints;
    }
}