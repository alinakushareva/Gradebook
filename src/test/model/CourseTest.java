package main.model;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * This class tests the Course class for student and assignment handling,
 * grading logic, and sorting features.
 */
public class CourseTest {

    private Course course;
    private Student student1;
    private Assignment assignment1;

    @BeforeEach
    public void setUp() {
        course = new Course("Biology101");
        student1 = new Student("Anna", "Lee", "anna01", "pass123");
        assignment1 = new Assignment("Quiz 1", 20);
    }

    @Test
    public void testCourseInitialization() {
        // Checks course name and initial student list
        assertEquals("Biology101", course.getCourseName());
        assertTrue(course.getStudents().isEmpty());
    }

    @Test
    public void testAddStudent() {
        // Ensures adding a student works and avoids duplicates
        course.addStudent(student1);
        course.addStudent(student1);
        assertEquals(1, course.getStudents().size());
    }

    @Test
    public void testRemoveStudent() {
        // Confirms removing a student works
        course.addStudent(student1);
        course.removeStudent(student1);
        assertFalse(course.getStudents().contains(student1));
    }

    @Test
    public void testAddAssignment() {
        // Tests that assignments can be added and duplicates ignored
        course.addAssignment(assignment1);
        course.addAssignment(assignment1);
        assertEquals(1, course.getAssignments().size());
    }

    @Test
    public void testCalculateTotalPointsAverage() {
        // Validates average calculation based on raw points
        course.addStudent(student1);
        course.addAssignment(assignment1);
        assignment1.assignGrade(student1, 18); // 90%
        assertEquals(90.0, course.calculateStudentAverage(student1), 0.01);
    }

    @Test
    public void testSortStudentsByName() {
        // Checks that student sorting by last/first name is correct
        Student sA = new Student("Anna", "B", "a1", "pw");
        Student sB = new Student("Bella", "A", "b2", "pw");
        course.addStudent(sA);
        course.addStudent(sB);
        List<Student> sorted = course.sortStudentsByName();
        assertEquals("Bella", sorted.get(0).getFirstName());
    }

    @Test
    public void testGetUngradedAssignments() {
        // Ensures ungraded assignments are correctly identified
        course.addStudent(student1);
        course.addAssignment(assignment1);
        assertEquals(1, course.getUngradedAssignments().size());
        assignment1.assignGrade(student1, 10);
        assertEquals(0, course.getUngradedAssignments().size());
    }
}

