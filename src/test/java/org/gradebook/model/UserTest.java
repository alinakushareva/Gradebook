package org.gradebook.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the User class.
 */
public class UserTest {
    private Student student;
    private Teacher teacher;
    private Course course;
    
    @BeforeEach
    public void setUp() {
        student = new Student("student1", "John", "Doe", "password123");
        teacher = new Teacher("teacher1", "Jane", "Smith", "password456");
        course = new Course("Math 101");
    }
    
    @Test
    @DisplayName("Test user construction")
    public void testUserConstruction() {
        assertEquals("student1", student.getUsername());
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("John Doe", student.getFullName());
        assertEquals("student", student.getRole());
        
        assertEquals("teacher1", teacher.getUsername());
        assertEquals("Jane", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
        assertEquals("Jane Smith", teacher.getFullName());
        assertEquals("teacher", teacher.getRole());
    }
    
    @Test
    @DisplayName("Test adding courses to users")
    public void testAddCourse() {
        // Initially, no courses
        assertEquals(0, student.getCourses().size());
        assertEquals(0, teacher.getCourses().size());
        
        // Add course to student
        student.addCourse(course);
        assertEquals(1, student.getCourses().size());
        assertTrue(student.getCourses().contains(course));
        
        // Add same course again (should not duplicate)
        student.addCourse(course);
        assertEquals(1, student.getCourses().size());
        
        // Add course to teacher
        teacher.addCourse(course);
        assertEquals(1, teacher.getCourses().size());
        assertTrue(teacher.getCourses().contains(course));
        assertEquals(1, teacher.getTeachingCourses().size());
        assertTrue(teacher.getTeachingCourses().contains(course));
    }
    
    @Test
    @DisplayName("Test user equality")
    public void testUserEquality() {
        Student sameStudent = new Student("student1", "Different", "Name", "differentPassword");
        Student differentStudent = new Student("student2", "John", "Doe", "password123");
        
        // Users with same username should be equal
        assertEquals(student, sameStudent);
        assertEquals(student.hashCode(), sameStudent.hashCode());
        
        // Users with different usernames should not be equal
        assertNotEquals(student, differentStudent);
        assertNotEquals(student.hashCode(), differentStudent.hashCode());
        
        // Different types should not be equal
        assertNotEquals(student, teacher);
    }
    
    @Test
    @DisplayName("Test defensive copying of collections")
    public void testDefensiveCopying() {
        // Add a course
        student.addCourse(course);
        
        // Get courses and try to modify the returned list
        List<Course> courses = student.getCourses();
        assertEquals(1, courses.size());
        
        // Try to modify the returned list (should not affect original)
        Course anotherCourse = new Course("Physics 101");
        courses.add(anotherCourse);
        
        // The original list in the student should be unchanged
        assertEquals(1, student.getCourses().size());
        assertFalse(student.getCourses().contains(anotherCourse));
    }
    
    @Test
    @DisplayName("Test toString method")
    public void testToString() {
        String studentString = student.toString();
        assertTrue(studentString.contains("student1"));
        assertTrue(studentString.contains("John"));
        assertTrue(studentString.contains("Doe"));
        assertTrue(studentString.contains("student"));
        
        String teacherString = teacher.toString();
        assertTrue(teacherString.contains("teacher1"));
        assertTrue(teacherString.contains("Jane"));
        assertTrue(teacherString.contains("Smith"));
        assertTrue(teacherString.contains("teacher"));
    }
} 