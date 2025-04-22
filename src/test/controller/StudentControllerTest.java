package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentControllerTest {

    private GradebookModel model;
    private Student student;
    private StudentController controller;
    private Course course;
    private Assignment assignment;

    @BeforeEach
    void setup() {
        model = new GradebookModel();
        student = new Student("Alina", "Kushareva", "alina_k", "pass");
        controller = new StudentController(student, model);

        course = new Course("CSC335");
        assignment = new Assignment("HW1", 10);

        course.addStudent(student);
        course.addAssignment(assignment);
        assignment.assignGrade(student, 9);
        student.assignFinalGrade(course, FinalGrade.A);
    }

    @Test
    void testGetEnrolledCourses() {
        student.getCourses().add(course);
        List<Course> enrolled = controller.getEnrolledCourses();
        assertTrue(enrolled.contains(course));
    }

    @Test
    void testGetAssignmentsForCourse() {
        List<Assignment> assignments = controller.getAssignmentsForCourse(course);
        assertTrue(assignments.contains(assignment));
    }

    @Test
    void testCalculateGPA() {
        double gpa = controller.calculateGPA();
        assertEquals(4.0, gpa);
    }

    @Test
    void testCalculateAverage() {
        double avg = controller.calculateAverage(course);
        assertEquals(90.0, avg, 0.01);
    }

    @Test
    void testGetGrade() {
        Grade grade = controller.getGrade(course, assignment);
        assertEquals(9, grade.getPointsReceived());
    }
}
