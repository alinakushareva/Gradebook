package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GradeTest {
    private static final double VALID_POINTS = 85.0;
    private static final double VALID_MAX = 100.0;

    // --- Constructor Tests ---
    @Test
    void constructor_AcceptsValidGrade() {
        Grade grade = new Grade(VALID_POINTS, VALID_MAX);
        assertEquals(VALID_POINTS, grade.getPointsReceived());
    }

    @Test
    void constructor_RejectsNegativePoints() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Grade(-1.0, VALID_MAX));
    }

    @Test
    void constructor_RejectsPointsOverMax() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Grade(VALID_MAX + 1.0, VALID_MAX));
    }

    @Test
    void constructor_RejectsZeroMaxPoints() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Grade(VALID_POINTS, 0.0));
    }

    @Test
    void constructor_RejectsNegativeMaxPoints() {
        assertThrows(IllegalArgumentException.class, 
            () -> new Grade(VALID_POINTS, -100.0));
    }

    // --- getPercentage() Tests ---
    @Test
    void getPercentage_CalculatesPerfectScore() {
        Grade grade = new Grade(VALID_MAX, VALID_MAX);
        assertEquals(100.0, grade.getPercentage());
    }

    @Test
    void getPercentage_CalculatesFailingGrade() {
        Grade grade = new Grade(0.0, VALID_MAX);
        assertEquals(0.0, grade.getPercentage());
    }

    @Test
    void getPercentage_CalculatesPartialCredit() {
        Grade grade = new Grade(75.5, VALID_MAX);
        assertEquals(75.5, grade.getPercentage());
    }

    // --- getPointsReceived() Tests ---
    @Test
    void getPointsReceived_ReturnsExactValue() {
        Grade grade = new Grade(VALID_POINTS, VALID_MAX);
        assertEquals(VALID_POINTS, grade.getPointsReceived());
    }

    // --- getMaxPoints() Tests ---
    @Test
    void getMaxPoints_ReturnsExactValue() {
        Grade grade = new Grade(VALID_POINTS, VALID_MAX);
        assertEquals(VALID_MAX, grade.getMaxPoints());
    }
}