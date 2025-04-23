package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssignmentTest {

    private Assignment assignment;
    private Student student1;
    private Student student2;
    private Course course;

    @BeforeEach
    void setup() {
        assignment = new Assignment("HW1", 10);
        student1 = new Student("Alice", "Smith", "alice", "pass");
        student2 = new Student("Bob", "Jones", "bob", "pass");

        course = new Course("CSC335");
        course.addStudent(student1);
        course.addStudent(student2);
        course.addAssignment(assignment);
    }

    @Test
    void testAssignGradeAndGetGrade() {
        assignment.assignGrade(student1, 8);
        Grade grade = assignment.getGrade(student1);
        assertEquals(8, grade.getPointsReceived());
    }

    @Test
    void testAssignGradeThrowsExceptionWhenPointsExceedMax() {
        assertThrows(IllegalArgumentException.class, () -> {
            assignment.assignGrade(student1, 11);
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
        assignment.assignGrade(student1, 7);
        assignment.assignGrade(student2, 9);
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
    void testGetStudentGradesReturnsCopy() {
        assignment.assignGrade(student1, 10);
        var map = assignment.getStudentGrades();
        map.clear();
        assertFalse(assignment.getStudentGrades().isEmpty());
    }
}