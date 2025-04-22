package controller;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the functionality of the TeacherController,
 * including course management, assignment grading, and student sorting.
 */
public class TeacherControllerTest {

    private Teacher teacher;
    private Student student1;
    private Student student2;
    private Course course;
    private Assignment assignment1;
    private GradebookModel model;
    private TeacherController controller;

    @BeforeEach
    public void setUp() {
        model = new GradebookModel();
        teacher = new Teacher("t001", "Grace", "Hopper", "passHash");
        student1 = new Student("Ada", "Lovelace", "s001", "pw1");
        student2 = new Student("Alan", "Turing", "s002", "pw2");

        course = new Course("CS101");
        teacher.addCourse(course);

        assignment1 = new Assignment("Midterm", 100);
        course.addStudent(student1);
        course.addStudent(student2);

        model.addTeacher(teacher);
        model.addStudent(student1);
        model.addStudent(student2);
        model.addCourse(course);

        controller = new TeacherController(teacher, model);
    }

    @Test
    public void testGetTeachingCourses() {
        // Verifies teacher's courses are retrieved correctly
        List<Course> courses = controller.getTeachingCourses();
        assertEquals(1, courses.size());
        assertEquals("CS101", courses.get(0).getCourseName());
    }

    @Test
    public void testAddAssignmentToCourse() {
        // Adds an assignment and checks it exists in course
        controller.addAssignment(course, assignment1);
        assertTrue(course.getAssignments().contains(assignment1));
    }

    @Test
    public void testAssignGrade() {
        // Assigns a grade and verifies the student receives it
        controller.addAssignment(course, assignment1);
        controller.assignGrade(course, student1, assignment1, 85.0);
        assertEquals(85.0, assignment1.getGrade(student1).getPointsReceived(), 0.01);
    }

    @Test
    public void testAssignFinalGrade() {
        // Sets and checks final letter grade
        controller.assignFinalGrade(course, student1, FinalGrade.B);
        assertEquals(FinalGrade.B, course.getFinalGrade(student1));
    }

    @Test
    public void testGetStudentsForCourse() {
        // Verifies all students in course are retrieved
        List<Student> students = controller.getStudentsForCourse(course);
        assertEquals(2, students.size());
        assertTrue(students.contains(student1));
        assertTrue(students.contains(student2));
    }

    @Test
    public void testGetAssignmentsForCourse() {
        // Checks assignment retrieval after adding
        controller.addAssignment(course, assignment1);
        List<Assignment> assignments = controller.getAssignmentsForCourse(course);
        assertEquals(1, assignments.size());
    }

    @Test
    public void testGetUngradedAssignments() {
        // Detects ungraded assignments correctly
        controller.addAssignment(course, assignment1);
        List<Assignment> ungraded = controller.getUngradedAssignments(course);
        assertEquals(1, ungraded.size());
        controller.assignGrade(course, student1, assignment1, 95);
        controller.assignGrade(course, student2, assignment1, 92);
        assertEquals(0, controller.getUngradedAssignments(course).size

