package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoryTest {

    private Category category;
    private Student student;
    private Assignment a1;
    private Assignment a2;
    private Assignment a3;
    private Course dummyCourse;

    @BeforeEach
    void setup() {
        category = new Category("Homework", 0.3);
        student = new Student("Alina", "Kushareva", "alina_k", "secure123");

        dummyCourse = new Course("CSC335");

        a1 = new Assignment("HW1", 10, dummyCourse);
        a2 = new Assignment("HW2", 10, dummyCourse);
        a3 = new Assignment("HW3", 10, dummyCourse);

        category.addAssignment(a1);
        category.addAssignment(a2);
        category.addAssignment(a3);

        a1.assignGrade(student, 10); // 100%
        a2.assignGrade(student, 9);  // 90%
        a3.assignGrade(student, 6);  // 60%
    }

    @Test
    void testCalculateCategoryAverage_WithNoGrades() {
        Category empty = new Category("Quizzes", 0.4);
        Student other = new Student("Bob", "Jones", "bob", "pw");
        assertEquals(0.0, empty.calculateCategoryAverage(other));
    }

    @Test
    void testGetWeight() {
        assertEquals(0.3, category.getWeight());
    }

    @Test
    void testSetDropLowestCount_Positive() {
        category.setDropLowestCount(2, dummyCourse); // Drops 60 and 90, keeps 100
        double avg = category.calculateCategoryAverage(student);
        assertEquals(100.0 * 0.3, avg, 0.01);
    }

    @Test
    void testSetDropLowestCount_Zero() {
        category.setDropLowestCount(0, dummyCourse);
        double avg = category.calculateCategoryAverage(student);
        double expected = ((100.0 + 90.0 + 60.0) / 3) * 0.3;
        assertEquals(expected, avg, 0.01);
    }

    @Test
    void testSetDropLowestCount_ExceedsAvailable() {
        category.setDropLowestCount(5, dummyCourse); // More than available
        double avg = category.calculateCategoryAverage(student);
        assertEquals(0.0, avg);
    }

    @Test
    void testIsDropped_WhenAssignmentIsDropped() {
        category.setDropLowestCount(1, dummyCourse); // Drops lowest = 60%
        assertTrue(category.isDropped(a3, student)); // a3 has 60%
    }

    @Test
    void testIsDropped_WhenAssignmentIsNotDropped() {
        category.setDropLowestCount(1, dummyCourse); // Drops lowest = 60%
        assertFalse(category.isDropped(a1, student)); // a1 has 100%
    }

    @Test
    void testIsDropped_WhenStudentHasNoGrades() {
        Student ghost = new Student("Ghost", "Unseen", "ghost", "pw");
        assertFalse(category.isDropped(a1, ghost));
    }

    @Test
    void testGetName() {
        assertEquals("Homework", category.getName());
    }

    @Test
    void testGetDropLowestCount() {
        category.setDropLowestCount(1, dummyCourse);
        assertEquals(1, category.getDropLowestCount());
    }

    @Test
    void testGetAssignments_ReturnsAllAssignments() {
        List<Assignment> assignments = category.getAssignments();
        assertEquals(3, assignments.size());
        assertTrue(assignments.contains(a1));
        assertTrue(assignments.contains(a2));
        assertTrue(assignments.contains(a3));
    }
}
