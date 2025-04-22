package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StudentTest {

    private Student student;
    private Assignment assignment1;
    private Assignment assignment2;
    private Course course;

    @BeforeEach
    void setup() {
        student = new Student("Alina", "Kushareva", "alina_k", "secure123");

        assignment1 = new Assignment("HW1", 10);
        assignment2 = new Assignment("HW2", 20);

        Grade grade1 = new Grade(8, 10);  // 80%
        Grade grade2 = new Grade(18, 20); // 90%

        student.addGrade(assignment1, grade1);
        student.addGrade(assignment2, grade2);

        course = new Course("CSC335");
        course.addAssignment(assignment1);
        course.addAssignment(assignment2);
    }

    @Test
    void testAddAndGetGrade() {
        Grade retrieved = student.getGrade(assignment1);
        assertEquals(8, retrieved.getPointsReceived());
    }

    @Test
    void testGetAverageForCourse() {
        double average = student.getAverageForCourse(course);
        assertEquals(86.67, average, 0.01); // weighted average of HW1 and HW2
    }

    @Test
    void testGetAverageForCourse_NoGrades() {
        Student emptyStudent = new Student("New", "Student", "new_s", "pass");
        double avg = emptyStudent.getAverageForCourse(course);
        assertEquals(0.0, avg);
    }

    @Test
    void testAssignAndGetFinalGrade() {
        student.assignFinalGrade(course, FinalGrade.B);
        FinalGrade grade = student.getFinalGrade(course);
        assertEquals(FinalGrade.B, grade);
    }

    @Test
    void testCalculateGPA_withGrades() {
        student.assignFinalGrade(course, FinalGrade.A);
        double gpa = student.calculateGPA();
        assertEquals(4.0, gpa);
    }

    @Test
    void testCalculateGPA_noGrades() {
        Student newStudent = new Student("A", "B", "x", "y");
        assertEquals(0.0, newStudent.calculateGPA());
    }

    @Test
    void testGetGrades_ReturnsCopy() {
        var grades = student.getGrades();
        grades.clear();
        assertFalse(student.getGrades().isEmpty());
    }

    @Test
    void testGetFinalGrades_ReturnsCopy() {
        student.assignFinalGrade(course, FinalGrade.C);
        var map = student.getFinalGrades();
        map.clear();
        assertFalse(student.getFinalGrades().isEmpty());
    }
}
