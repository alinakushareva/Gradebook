package org.gradebook.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentTest {
    
    private Assignment assignment;
    private Student student1, student2, student3;
    
    @BeforeEach
    public void setUp() {
        assignment = new Assignment("Test Assignment", 100.0, "Homework");
        student1 = new Student("student1", "Student", "One", "password");
        student2 = new Student("student2", "Student", "Two", "password");
        student3 = new Student("student3", "Student", "Three", "password");
    }
    
    @Test
    @DisplayName("Test assignment constructor and basic getters")
    public void testAssignmentBasics() {
        assertEquals("Test Assignment", assignment.getTitle());
        assertEquals(100.0, assignment.getMaxPoints());
        assertEquals("Homework", assignment.getCategory());
        
        // Test constructor without category
        Assignment noCategory = new Assignment("No Category", 50.0, null);
        assertEquals("No Category", noCategory.getTitle());
        assertEquals(50.0, noCategory.getMaxPoints());
        assertNull(noCategory.getCategory());
    }
    
    @Test
    @DisplayName("Test title setter")
    public void testSetTitle() {
        assignment.setTitle("New Title");
        assertEquals("New Title", assignment.getTitle());
        
        // Test empty title
        assignment.setTitle("");
        assertEquals("New Title", assignment.getTitle()); // Should not change
        
        assignment.setTitle(null);
        assertEquals("New Title", assignment.getTitle()); // Should not change
    }
    
    @Test
    @DisplayName("Test max points setter")
    public void testSetMaxPoints() {
        assignment.setMaxPoints(200.0);
        assertEquals(200.0, assignment.getMaxPoints());
        
        // Test invalid max points
        assignment.setMaxPoints(0.0);
        assertEquals(200.0, assignment.getMaxPoints()); // Should not change
        
        assignment.setMaxPoints(-10.0);
        assertEquals(200.0, assignment.getMaxPoints()); // Should not change
    }
    
    @Test
    @DisplayName("Test category setter")
    public void testSetCategory() {
        assignment.setCategory("Project");
        assertEquals("Project", assignment.getCategory());
        
        // Test empty category
        assignment.setCategory("");
        assertNull(assignment.getCategory()); // Should be set to null
        
        // Test null category
        assignment.setCategory(null);
        assertNull(assignment.getCategory());
    }
    
    @Test
    @DisplayName("Test grade assignment")
    public void testAssignGrade() {
        // No grades initially
        assertNull(assignment.getGrade(student1));
        assertFalse(assignment.isGraded(student1));
        
        // Assign a grade
        assignment.assignGrade(student1, 85.0);
        Grade grade = assignment.getGrade(student1);
        assertNotNull(grade);
        assertEquals(85.0, grade.getPointsReceived());
        assertEquals(100.0, grade.getMaxPoints());
        assertEquals(85.0, grade.getPercentage());
        assertTrue(assignment.isGraded(student1));
        
        // Update the grade
        assignment.assignGrade(student1, 90.0);
        grade = assignment.getGrade(student1);
        assertEquals(90.0, grade.getPointsReceived());
        assertEquals(90.0, grade.getPercentage());
        
        // Test invalid score
        assignment.assignGrade(student1, -10.0);
        grade = assignment.getGrade(student1);
        assertEquals(90.0, grade.getPointsReceived()); // Should not change
        
        assignment.assignGrade(student1, 110.0);
        grade = assignment.getGrade(student1);
        assertEquals(90.0, grade.getPointsReceived()); // Should not change
    }
    
    @Test
    @DisplayName("Test get all grades")
    public void testGetAllGrades() {
        // No grades initially
        assertFalse(assignment.isGraded(student1));
        assertFalse(assignment.isGraded(student2));
        
        // Assign grades to students
        assignment.assignGrade(student1, 85.0);
        assignment.assignGrade(student2, 90.0);
        
        // 验证grading状态和成绩值
        assertTrue(assignment.isGraded(student1));
        assertTrue(assignment.isGraded(student2));
        assertEquals(85.0, assignment.getGrade(student1).getPointsReceived());
        assertEquals(90.0, assignment.getGrade(student2).getPointsReceived());
    }
    
    @Test
    @DisplayName("Test calculate average")
    public void testCalculateAverage() {
        // No grades initially
        assertEquals(0.0, assignment.getAverage());
        
        // Assign one grade
        assignment.assignGrade(student1, 80.0);
        assertEquals(80.0, assignment.getAverage());
        
        // Assign more grades
        assignment.assignGrade(student2, 90.0);
        assignment.assignGrade(student3, 70.0);
        
        // Average should be (80 + 90 + 70) / 3 = 80.0
        assertEquals(80.0, assignment.getAverage());
    }
    
    @Test
    @DisplayName("Test toString method")
    public void testToString() {
        // Test with full toString output
        assertTrue(assignment.toString().contains("Test Assignment"));
        assertTrue(assignment.toString().contains("100.0"));
        assertTrue(assignment.toString().contains("Homework"));
        
        Assignment noCategory = new Assignment("No Category", 50.0, null);
        assertTrue(noCategory.toString().contains("No Category"));
        assertTrue(noCategory.toString().contains("50.0"));
    }
} 