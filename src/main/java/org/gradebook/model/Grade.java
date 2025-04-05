package org.gradebook.model;

/**
 * Represents a grade for an assignment.
 * Contains the points received and the maximum possible points.
 */
public class Grade {
    private double pointsReceived;
    private double maxPoints;
    
    /**
     * Constructor for Grade class.
     *
     * @param pointsReceived the points received
     * @param maxPoints      the maximum possible points
     */
    public Grade(double pointsReceived, double maxPoints) {
        this.pointsReceived = pointsReceived;
        this.maxPoints = maxPoints;
    }
    
    /**
     * Gets the percentage score (points received / max points).
     *
     * @return the percentage (0.0 to 100.0)
     */
    public double getPercentage() {
        if (maxPoints == 0) {
            return 0.0;
        }
        return (pointsReceived / maxPoints) * 100.0;
    }
    
    /**
     * Gets the points received.
     *
     * @return the points received
     */
    public double getPointsReceived() {
        return pointsReceived;
    }
    
    /**
     * Sets the points received.
     *
     * @param pointsReceived the points to set
     */
    public void setPointsReceived(double pointsReceived) {
        this.pointsReceived = pointsReceived;
    }
    
    /**
     * Gets the maximum possible points.
     *
     * @return the maximum points
     */
    public double getMaxPoints() {
        return maxPoints;
    }
    
    /**
     * Sets the maximum possible points.
     *
     * @param maxPoints the maximum points to set
     */
    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }
    
    @Override
    public String toString() {
        return String.format("%.1f/%.1f (%.1f%%)", 
                pointsReceived, maxPoints, getPercentage());
    }
} 