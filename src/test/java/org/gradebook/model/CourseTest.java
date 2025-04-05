package org.gradebook.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {
    
    private Course course;
    private Teacher teacher;
    private Student student1, student2;
    private Assignment assignment1, assignment2;
    
    @BeforeEach
    public void setUp() {
        teacher = new Teacher("teacheruser", "Teacher", "User", "password");
        course = new Course("Test Course");
        
        student1 = new Student("student1", "Student", "One", "password");
        student2 = new Student("student2", "Student", "Two", "password");
        
        assignment1 = new Assignment("Assignment 1", 100.0, "Homework");
        assignment2 = new Assignment("Assignment 2", 50.0, "Quiz");
    }
    
    @Test
    @DisplayName("Test course constructor and basic getters")
    public void testCourseBasics() {
        assertEquals("Test Course", course.getCourseName());
        assertTrue(course.getStudents().isEmpty());
        assertTrue(course.getAssignments().isEmpty());
    }
    
    @Test
    @DisplayName("Test adding and removing students")
    public void testAddRemoveStudents() {
        course.addStudent(student1);
        assertEquals(1, course.getStudents().size());
        assertTrue(course.getStudents().contains(student1));
        
        // Add same student again should not add duplicate
        course.addStudent(student1);
        assertEquals(1, course.getStudents().size());
        
        // Add another student
        course.addStudent(student2);
        assertEquals(2, course.getStudents().size());
        
        // Remove student
        course.removeStudent(student1);
        assertEquals(1, course.getStudents().size());
        assertFalse(course.getStudents().contains(student1));
        
        // Remove non-existent student should not cause error
        course.removeStudent(student1);
    }
    
    @Test
    @DisplayName("Test adding and getting assignments")
    public void testAssignments() {
        course.addAssignment(assignment1);
        assertEquals(1, course.getAssignments().size());
        assertTrue(course.getAssignments().contains(assignment1));
        
        course.addAssignment(assignment2);
        assertEquals(2, course.getAssignments().size());
    }
    
    @Test
    @DisplayName("Test grade assignments and calculate student average")
    public void testGradeAssignments() {
        course.addStudent(student1);
        course.addAssignment(assignment1);
        course.addAssignment(assignment2);
        
        // Initially no grades
        assertEquals(0.0, course.calculateStudentAverage(student1));
        assertTrue(course.getUngradedAssignments().contains(assignment1));
        assertTrue(course.getUngradedAssignments().contains(assignment2));
        
        // Assign grades
        assignment1.assignGrade(student1, 90.0);
        assertEquals(90.0, assignment1.getGrade(student1).getPercentage());
        
        // Student average with one grade
        assertEquals(90.0, course.calculateStudentAverage(student1));
        
        // Second assignment
        assignment2.assignGrade(student1, 40.0);
        assertEquals(80.0, assignment2.getGrade(student1).getPercentage());
        
        // Student average with both grades - weighted by max points
        // (90 * 100 + 40 * 50) / (100 + 50) = 73.33
        assertEquals(73.33, course.calculateStudentAverage(student1), 0.01);
    }
    
    @Test
    @DisplayName("Test final grade assignment")
    public void testFinalGrades() {
        course.addStudent(student1);
        course.addStudent(student2);
        
        // Initially no final grades
        assertNull(course.getFinalGrade(student1));
        assertNull(course.getFinalGrade(student2));
        
        // Assign final grades
        course.assignFinalGrade(student1, FinalGrade.A);
        assertEquals(FinalGrade.A, course.getFinalGrade(student1));
        assertNull(course.getFinalGrade(student2));
        
        // Update final grade
        course.assignFinalGrade(student1, FinalGrade.B);
        assertEquals(FinalGrade.B, course.getFinalGrade(student1));
    }
    
    @Test
    @DisplayName("Test class average calculation")
    public void testClassAverage() {
        // Empty course should return 0
        assertEquals(0.0, course.calculateClassAverage());
        
        course.addStudent(student1);
        course.addStudent(student2);
        course.addAssignment(assignment1);
        
        // No grades yet
        assertEquals(0.0, course.calculateClassAverage());
        
        // Add some grades
        assignment1.assignGrade(student1, 80.0);
        assignment1.assignGrade(student2, 90.0);
        
        // Class average should be 85.0
        assertEquals(85.0, course.calculateClassAverage());
    }
    
    @Test
    @DisplayName("Test median grade calculation")
    public void testMedianCalculation() {
        course.addStudent(student1);
        course.addStudent(student2);
        Student student3 = new Student("student3", "Student", "Three", "password");
        course.addStudent(student3);
        
        course.addAssignment(assignment1);
        
        // No grades yet
        assertEquals(0.0, course.calculateMedianForAssignment(assignment1));
        
        // Add grades
        assignment1.assignGrade(student1, 80.0);
        assignment1.assignGrade(student2, 90.0);
        assignment1.assignGrade(student3, 75.0);
        
        // Median should be the middle value (80.0)
        assertEquals(80.0, course.calculateMedianForAssignment(assignment1));
        
        // Add even number of students
        Student student4 = new Student("student4", "Student", "Four", "password");
        course.addStudent(student4);
        assignment1.assignGrade(student4, 85.0);
        
        // With even number, median is average of two middle values
        // [75, 80, 85, 90] -> (80 + 85) / 2 = 82.5
        assertEquals(82.5, course.calculateMedianForAssignment(assignment1));
    }
    
    @Test
    @DisplayName("Test sorting students by name")
    public void testSortStudentsByName() {
        course.addStudent(student2); // "Student Two"
        course.addStudent(student1); // "Student One"
        
        List<Student> sorted = course.sortStudentsByName();
        assertEquals(2, sorted.size());
        assertEquals(student1, sorted.get(0)); // "Student One" comes first
        assertEquals(student2, sorted.get(1));
    }
    
    @Test
    @DisplayName("Test student grouping")
    public void testGroupStudents() {
        Student student3 = new Student("student3", "Student", "Three", "password");
        Student student4 = new Student("student4", "Student", "Four", "password");
        Student student5 = new Student("student5", "Student", "Five", "password");
        
        course.addStudent(student1);
        course.addStudent(student2);
        course.addStudent(student3);
        course.addStudent(student4);
        course.addStudent(student5);
        
        // Create groups with size 2
        List<List<Student>> groups = new ArrayList<>();
        for (int i = 0; i < 3; i++) { // 5 students in 3 groups
            groups.add(new ArrayList<>());
        }
        
        course.groupStudents(groups);
        
        // Check that all students are assigned to groups
        int totalStudents = 0;
        for (List<Student> group : groups) {
            totalStudents += group.size();
        }
        assertEquals(5, totalStudents);
    }
} 