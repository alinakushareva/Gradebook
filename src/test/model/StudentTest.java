package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

class StudentTest {

    private Student student;
    private Course course;
    private Assignment assignment;
    private Grade grade;

    @BeforeEach
    void setUp() {
        student = new Student("Alina", "Kushareva", "alina_k", "pass");
        course = new Course("CSC335");
        assignment = new Assignment("HW1", 100, course);
        grade = new Grade(90, 100);

        course.addAssignment(assignment);
        course.addStudent(student);
        student.addCourse(course);
    }

    @Test
    void testAddAndGetGrade() {
        student.addGrade(assignment, grade);
        assertEquals(90.0, student.getGrade(assignment).getPointsReceived());
    }

    @Test
    void testGetAverageForCourse() {
        student.addGrade(assignment, grade);
        assertEquals(90.0, student.getAverageForCourse(course), 0.01);
    }

    @Test
    void testAverageWithNoGrades() {
        assertEquals(0.0, student.getAverageForCourse(course), 0.01);
    }

    @Test
    void testCalculateGPA() {
        student.assignFinalGrade(course, FinalGrade.B);
        assertEquals(3.0, student.calculateGPA(), 0.01);
    }

    @Test
    void testCalculateGPAWhenEmpty() {
        assertEquals(0.0, student.calculateGPA(), 0.01);
    }

    @Test
    void testAssignAndGetFinalGrade() {
        student.assignFinalGrade(course, FinalGrade.A);
        assertEquals(FinalGrade.A, student.getFinalGrade(course));
    }

    @Test
    void testGetGradesMap() {
        student.addGrade(assignment, grade);
        Map<Assignment, Grade> map = student.getGrades();
        assertEquals(1, map.size());
        assertTrue(map.containsKey(assignment));
    }

    @Test
    void testGetFinalGradesMap() {
        student.assignFinalGrade(course, FinalGrade.B);
        Map<Course, FinalGrade> finalMap = student.getFinalGrades();
        assertEquals(1, finalMap.size());
        assertEquals(FinalGrade.B, finalMap.get(course));
    }

    @Test
    void testToFileString() {
        String expected = "alina_k,Alina Kushareva,pass,student";
        assertEquals(expected, student.toFileString());
    }

    @Test
    void testEqualsAndHashCode() {
        Student same = new Student("Alina", "Kushareva", "alina_k", "otherpass");
        Student different = new Student("Bob", "Smith", "bob_s", "pass");

        assertEquals(student, same);
        assertNotEquals(student, different);
        assertEquals(student.hashCode(), same.hashCode());
    }

    @Test
    void testUserMethodsInheritedFromAbstractUser() {
        assertEquals("alina_k", student.getUsername());
        assertEquals("Alina", student.getFirstName());
        assertEquals("Kushareva", student.getLastName());
        assertEquals("Alina Kushareva", student.getFullName());
        assertEquals("Student", student.getRole());
        assertEquals("pass", student.getPasswordHash());

        student.addCourse(course);
        List<Course> courses = student.getCourses();
        assertEquals(1, courses.size());
        assertTrue(courses.contains(course));
    }
}
