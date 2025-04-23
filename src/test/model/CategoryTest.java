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

    @BeforeEach
    void setup() {
        category = new Category("Homework", 0.3);
        student = new Student("Alina", "Kushareva", "alina_k", "secure123");

        a1 = new Assignment("HW1", 10);
        a2 = new Assignment("HW2", 10);
        a3 = new Assignment("HW3", 10);

        category.addAssignment(a1);
        category.addAssignment(a2);
        category.addAssignment(a3);

        a1.assignGrade(student, 10); // 100%
        a2.assignGrade(student, 9);  // 90%
        a3.assignGrade(student, 6);  // 60%
    }


    @Test
    void testCalculateCategoryAverage_EmptyGrades() {
        Category emptyCat = new Category("Quizzes", 0.2);
        Student s = new Student("Jake", "Smith", "jake", "pwd");
        double avg = emptyCat.calculateCategoryAverage(s);
        assertEquals(0.0, avg);
    }

    @Test
    void testGetWeight_ReturnsCorrectValue() {
        assertEquals(0.3, category.getWeight());
    }
    
    @Test
    void testSetDropLowestCount_PositiveValue() {
        category.setDropLowestCount(2); // Drops 60 and 90, keeps 100
        double avg = category.calculateCategoryAverage(student);
        double expected = 100.0 * 0.3;
        assertEquals(expected, avg, 0.01);
    }


    @Test
    void testSetDropLowestCount_ZeroValue() {
        category.setDropLowestCount(0);
        double avg = category.calculateCategoryAverage(student);
        double expected = (100.0 + 90.0 + 60.0) / 3 * 0.3;
        assertEquals(expected, avg, 0.01);
    }
    
    @Test
    void testDropLowestCountGreaterThanGrades() {
        category.setDropLowestCount(5); // More than number of assignments
        double avg = category.calculateCategoryAverage(student);
        assertEquals(0.0, avg); // All grades dropped
    }

    @Test
    void testGetName_ReturnsCorrectName() {
        assertEquals("Homework", category.getName());
    }

    @Test
    void testGetDropLowestCount_ReturnsCorrectValue() {
        category.setDropLowestCount(1);
        assertEquals(1, category.getDropLowestCount());
    }

    @Test
    void testGetAssignments_ReturnsCorrectList() {
        List<Assignment> list = category.getAssignments();
        assertEquals(3, list.size());
        assertTrue(list.contains(a1));
        assertTrue(list.contains(a2));
        assertTrue(list.contains(a3));
    }

}
