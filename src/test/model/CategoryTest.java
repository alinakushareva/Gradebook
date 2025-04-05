package main.model;

import model.Grade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryTest {

    private Category category;
    private Student student;
    private Assignment a1;
    private Assignment a2;
    private Assignment a3;

    @BeforeEach
    void setUp() {
        category = new Category();
        student = mock(Student.class);
        a1 = mock(Assignment.class);
        a2 = mock(Assignment.class);
        a3 = mock(Assignment.class);
    }

    @Test
    void testAddAndGetAssignments() {
        category.addAssignment(a1);
        category.addAssignment(a2);

        List<Assignment> list = category.getAssignments();
        assertEquals(2, list.size());
        assertTrue(list.contains(a1));
        assertTrue(list.contains(a2));
    }

    @Test
    void testGetWeightAndName_Defaults() {
        assertEquals(0.0, category.getWeight());
        assertNull(category.getName());
    }

    @Test
    void testCalculateCategoryAverage_NoAssignments() {
        double avg = category.calculateCategoryAverage(student);
        assertEquals(0.0, avg);
    }

    @Test
    void testCalculateCategoryAverage_SomeGradesNoDrop() {
        Grade g1 = new Grade(90, 100); // 90%
        Grade g2 = new Grade(80, 100); // 80%

        when(a1.getGrade(student)).thenReturn(g1);
        when(a2.getGrade(student)).thenReturn(g2);

        category.addAssignment(a1);
        category.addAssignment(a2);

        // set dropLowestCount manually for test
        setField(category, "dropLowestCount", 0);

        double avg = category.calculateCategoryAverage(student);
        assertEquals(85.0, avg, 0.01);
    }

    @Test
    void testCalculateCategoryAverage_WithDropLowest() {
        Grade g1 = new Grade(90, 100); // 90%
        Grade g2 = new Grade(80, 100); // 80%
        Grade g3 = new Grade(70, 100); // 70%

        when(a1.getGrade(student)).thenReturn(g1);
        when(a2.getGrade(student)).thenReturn(g2);
        when(a3.getGrade(student)).thenReturn(g3);

        category.addAssignment(a1);
        category.addAssignment(a2);
        category.addAssignment(a3);

        setField(category, "dropLowestCount", 1); // drop 70

        double avg = category.calculateCategoryAverage(student);
        assertEquals(85.0, avg, 0.01); // (90 + 80) / 2
    }

    private void setField(Category cat, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = cat.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(cat, value);
        } catch (Exception e) {
            fail("Failed to set field: " + e.getMessage());
        }
    }
}

