package org.gradebook.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Grade class.
 */
public class GradeTest {
    
    @Test
    @DisplayName("Test basic grade construction")
    public void testGradeConstruction() {
        Grade grade = new Grade(85.0, 100.0);
        
        assertEquals(85.0, grade.getPointsReceived());
        assertEquals(100.0, grade.getMaxPoints());
        assertEquals(85.0, grade.getPercentage());
    }
    
    @ParameterizedTest
    @DisplayName("Test percentage calculation with various values")
    @CsvSource({
        "85, 100, 85.0",
        "42.5, 50, 85.0",
        "10, 10, 100.0",
        "0, 100, 0.0",
        "0, 0, 0.0"  // Edge case: division by zero
    })
    public void testPercentageCalculation(double points, double maxPoints, double expectedPercentage) {
        Grade grade = new Grade(points, maxPoints);
        assertEquals(expectedPercentage, grade.getPercentage(), 0.01);
    }
    
    @Test
    @DisplayName("Test setting points received")
    public void testSetPointsReceived() {
        Grade grade = new Grade(80.0, 100.0);
        assertEquals(80.0, grade.getPercentage());
        
        grade.setPointsReceived(90.0);
        assertEquals(90.0, grade.getPointsReceived());
        assertEquals(90.0, grade.getPercentage());
    }
    
    @Test
    @DisplayName("Test setting max points")
    public void testSetMaxPoints() {
        Grade grade = new Grade(80.0, 100.0);
        assertEquals(80.0, grade.getPercentage());
        
        grade.setMaxPoints(200.0);
        assertEquals(200.0, grade.getMaxPoints());
        assertEquals(40.0, grade.getPercentage(), 0.01);  // 80/200 = 40%
    }
    
    @Test
    @DisplayName("Test toString format")
    public void testToString() {
        Grade grade = new Grade(85.0, 100.0);
        String result = grade.toString();
        
        assertTrue(result.contains("85.0"));
        assertTrue(result.contains("100.0"));
        assertTrue(result.contains("85.0%"));
    }
    
    @Test
    @DisplayName("Test handling of invalid inputs")
    public void testInvalidInputs() {
        // Negative points
        Grade grade1 = new Grade(-10.0, 100.0);
        assertEquals(-10.0, grade1.getPercentage(), 0.01);
        
        // Negative max points
        Grade grade2 = new Grade(50.0, -100.0);
        assertEquals(-50.0, grade2.getPercentage(), 0.01);
        
        // Points greater than max
        Grade grade3 = new Grade(110.0, 100.0);
        assertEquals(110.0, grade3.getPercentage(), 0.01);
    }
} 