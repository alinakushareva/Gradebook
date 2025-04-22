package test.model;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the functionality of the Teacher class,
 * including course management and student enrollment.
 */
public class TeacherTest {

    private Teacher teacher;
    private Course course1;
    private Course course2;

    @BeforeEach
    public void setUp() {
        teacher = new Teacher("teach001", "Alice", "Smith", "hashedPassword123");
        course1 = new Course("Math101");
        course2 = new Course("Physics101");
    }

    @Test
    public void testTeacherInitialization() {
        // Verifies that the constructor correctly assigns basic fields
        assertEquals("teach001", teacher.getUsername());
        assertEquals("Alice", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
        assertEquals("teacher", teacher.getRole());
        assertEquals("Alice Smith", teacher.getFullName());
    }

    @Test
    public void testAddAndGetTeachingCourses() {
        // Ensures courses are added and retrieved correctly
        teacher.addCourse(course1);
        teacher.addCourse(course2);
        List<Course> courses = teacher.getTeachingCourses();
        assertEquals(2, courses.size());
        assertTrue(courses.contains(course1));
        assertTrue(courses.contains(course2));
    }

    @Test
    public void testAvoidDuplicateCourses() {
        // Ensures duplicate courses are not added
        teacher.addCourse(course1);
        teacher.addCourse(course1);
        assertEquals(1, teacher.getTeachingCourses().size());
    }

    @Test
    public void testRemoveCourse() {
        // Checks that removing a course works correctly
        teacher.addCourse(course1);
        teacher.removeCourse(course1);
        assertFalse(teacher.getTeachingCourses().contains(course1));
    }

    @Test
    public void testCreateCourse() {
        // Confirms that new course creation adds the course to the list
        teacher.createCourse("Chem101");
        assertTrue(teacher.getTeachingCourses().stream()
            .anyMatch(c -> c.getCourseName().equals("Chem101")));
    }

    @Test
    public void testEnrollStudent() {
        // Tests that enrolling a student adds them to the course
        Student student = new Student("John", "Doe", "john001", "pwHash");
        teacher.enrollStudent(course1, student);
        assertTrue(course1.getStudents().contains(student));
    }
}
