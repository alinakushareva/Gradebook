package org.gradebook.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {
    
    private Teacher teacher;
    private Course course1, course2;
    
    @BeforeEach
    public void setUp() {
        teacher = new Teacher("teacheruser", "Test", "Teacher", "password");
        course1 = new Course("Course 1");
        course2 = new Course("Course 2");
    }
    
    @Test
    @DisplayName("Test teacher constructor and basic inheritance")
    public void testTeacherBasics() {
        assertEquals("teacheruser", teacher.getUsername());
        assertEquals("Test Teacher", teacher.getFullName());
        assertEquals("teacher", teacher.getRole());
        assertTrue(teacher.getTeachingCourses().isEmpty());
    }
    
    @Test
    @DisplayName("Test adding and getting courses")
    public void testAddGetCourses() {
        // Add first course
        teacher.addCourse(course1);
        List<Course> courses = teacher.getTeachingCourses();
        assertEquals(1, courses.size());
        assertTrue(courses.contains(course1));
        
        // Add second course
        teacher.addCourse(course2);
        courses = teacher.getTeachingCourses();
        assertEquals(2, courses.size());
        assertTrue(courses.contains(course2));
        
        // Adding same course twice should have no effect
        teacher.addCourse(course1);
        courses = teacher.getTeachingCourses();
        assertEquals(2, courses.size());
    }
    
    @Test
    @DisplayName("Test removing courses")
    public void testRemoveCourse() {
        // Add courses
        teacher.addCourse(course1);
        teacher.addCourse(course2);
        assertEquals(2, teacher.getTeachingCourses().size());
        
        // Remove a course
        teacher.removeCourse(course1);
        List<Course> courses = teacher.getTeachingCourses();
        assertEquals(1, courses.size());
        assertFalse(courses.contains(course1));
        assertTrue(courses.contains(course2));
        
        // Remove non-existent course
        teacher.removeCourse(course1); // Should not throw exception
        assertEquals(1, teacher.getTeachingCourses().size());
    }
    
    @Test
    @DisplayName("Test toString method")
    public void testToString() {
        // Test initial toString
        String result = teacher.toString();
        assertTrue(result.contains("teacheruser"));
        assertTrue(result.contains("Test Teacher"));
        
        // Add courses and test toString again
        teacher.addCourse(course1);
        teacher.addCourse(course2);
        result = teacher.toString();
        assertTrue(result.contains("courses=2"));
    }
} 