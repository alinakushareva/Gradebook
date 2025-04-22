package main.model;

import model.Grade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import model.*;

/**
 * Tests the Category class, including grade averaging and drop-lowest logic.
 */
public class CategoryTest {

    private Category category;
    private Assignment assignment1;
    private Assignment assignment2;
    private Student student;

    @BeforeEach
    public void setUp() {
        category = new Category("Quizzes", 0.4);
        assignment1 = new Assignment("Quiz 1", 10);
        assignment2 = new Assignment("Quiz 2", 10);
        student = new Student("Tom", "Hanks", "tom01", "hashPw");
    }

    @Test
    public void testCategoryWeight() {
        // Confirms the weight is set correctly
        assertEquals(0.4, category.getWeight(), 0.01);
    }

    @Test
    public void testAddAssignmentsAndAverageCalculation() {
        // Tests average score calculation across multiple assignments
        category.addAssignment(assignment1);
        category.addAssignment(assignment2);
        assignment1.assignGrade(student, 8);
        assignment2.assignGrade(student, 6);
        double expectedWeighted = ((8 + 6) / 2.0) * 0.4;
        assertEquals(expectedWeighted, category.calculateCategoryAverage(student), 0.01);
    }

    @Test
    public void testDropLowestGrade() {
        // Ensures the lowest grade is dropped when drop count is set
        category.addAssignment(assignment1);
        category.addAssignment(assignment2);
        assignment1.assignGrade(student, 4);
        assignment2.assignGrade(student, 10);
        category.setDropLowestCount(1);
        double expected = 10.0 * 0.4;
        assertEquals(expected, category.calculateCategoryAverage(student), 0.01);
    }

    @Test
    public void testNoGradesReturnsZero() {
        // Verifies average returns 0 when no grades are present
        assertEquals(0.0, category.calculateCategoryAverage(student), 0.01);
    }
}
