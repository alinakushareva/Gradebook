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
        model.addStudent(student);  // optional depending on usage
        controller = new StudentController(student, model);

        course = new Course("CSC335");
        assignment = new Assignment("HW1", 10, course);

        course.addStudent(student);
        course.addAssignment(assignment);
        assignment.assignGrade(student, 9);  // 9/10 → 90%
        student.assignFinalGrade(course, FinalGrade.A);
        student.getCourses().add(course);
    }

    @Test
    void testGetEnrolledCourses() {
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

    @Test
    void testGetGradeReturnsNullIfUnassigned() {
        Assignment newAssignment = new Assignment("HW2", 10, course);
        Grade grade = controller.getGrade(course, newAssignment);  // not assigned
        assertNull(grade);
    }

    @Test
    void testCalculateAverageWhenNoGrades() {
        Course emptyCourse = new Course("EmptyCourse");
        emptyCourse.addStudent(student);
        student.getCourses().add(emptyCourse);
        double avg = controller.calculateAverage(emptyCourse);
        assertEquals(0.0, avg, 0.01);
    }

    @Test
    void testCalculateGPAWhenNoFinalGrades() {
        Student newStudent = new Student("Alex", "Test", "alex", "pw");
        StudentController newController = new StudentController(newStudent, model);
        assertEquals(0.0, newController.calculateGPA(), 0.01);
    }
}
