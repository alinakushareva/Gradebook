package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssignmentTest {

    private Assignment assignment;
    private Student student1;
    private Student student2;
    private Course course;

    @BeforeEach
    void setup() {
        course = new Course("CSC335");
        student1 = new Student("Alice", "Smith", "alice", "pass");
        student2 = new Student("Bob", "Jones", "bob", "pass");

        course.addStudent(student1);
        course.addStudent(student2);
        assignment = new Assignment("HW1", 10, course);
        course.addAssignment(assignment);
    }

    @Test
    void testAssignGradeAndGetGrade() {
        assignment.assignGrade(student1, 8);
        Grade grade = assignment.getGrade(student1);
        assertEquals(8.0, grade.getPointsReceived());
    }

    @Test
    void testAssignGradeThrowsExceptionWhenPointsExceedMax() {
        assertThrows(IllegalArgumentException.class, () -> {
            assignment.assignGrade(student1, 11); // maxPoints is 10
        });
    }

    @Test
    void testIsGradedReturnsTrueAfterGrading() {
        assignment.assignGrade(student1, 9);
        assertTrue(assignment.isGraded(student1));
    }

    @Test
    void testIsGradedReturnsFalseIfNotGraded() {
        assertFalse(assignment.isGraded(student2));
    }

    @Test
    void testIsFullyGradedReturnsFalseInitially() {
        assertFalse(assignment.isFullyGraded(course.getStudents()));
    }

    @Test
    void testIsFullyGradedReturnsTrueAfterAllGraded() {
        assignment.assignGrade(student1, 9);
        assignment.assignGrade(student2, 10);
        assertTrue(assignment.isFullyGraded(course.getStudents()));
    }

    @Test
    void testGetTitle() {
        assertEquals("HW1", assignment.getTitle());
    }

    @Test
    void testGetMaxPoints() {
        assertEquals(10.0, assignment.getMaxPoints());
    }

    @Test
    void testGetCourse() {
        assertEquals(course, assignment.getCourse());
    }

    @Test
    void testGetStudentGradesReturnsCopy() {
        assignment.assignGrade(student1, 10);
        Map<Student, Grade> copy = assignment.getStudentGrades();
        copy.clear(); // Should not affect internal map
        assertFalse(assignment.getStudentGrades().isEmpty());
    }

    @Test
    void testConstructorThrowsIfCourseIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Assignment("HW2", 20, null);
        });
    }
}
