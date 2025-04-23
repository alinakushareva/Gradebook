package controller;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherControllerTest {

    private GradebookModel model;
    private Teacher teacher;
    private TeacherController controller;
    private Course course;
    private Student student;

    @BeforeEach
    void setup() {
        model = new GradebookModel();
        teacher = new Teacher("jdoe", "John", "Doe", "pass");
        controller = new TeacherController(teacher, model);
        course = new Course("CSC335");
        student = new Student("Alina", "Kushareva", "alina_k", "pass");
        teacher.addCourse(course);
        course.addStudent(student);
    }

    @Test
    void testGetTeachingCourses() {
        List<Course> courses = controller.getTeachingCourses();
        assertTrue(courses.contains(course));
    }

    @Test
    void testAddAssignment() {
        Assignment a = new Assignment("HW1", 10);
        controller.addAssignment(course, a);
        assertTrue(course.getAssignments().contains(a));
    }

    @Test
    void testAssignGrade() {
        Assignment a = new Assignment("HW1", 10);
        controller.addAssignment(course, a);
        controller.assignGrade(course, student, a, 9);
        assertEquals(9, a.getGrade(student).getPointsReceived());
    }

    @Test
    void testAssignFinalGrade() {
        controller.assignFinalGrade(course, student, FinalGrade.A);
        assertEquals(FinalGrade.A, course.getFinalGrade(student));
    }

    @Test
    void testGetStudentsForCourse() {
        List<Student> students = controller.getStudentsForCourse(course);
        assertTrue(students.contains(student));
    }

    @Test
    void testGetAssignmentsForCourse() {
        Assignment a = new Assignment("HW1", 10);
        course.addAssignment(a);
        List<Assignment> assignments = controller.getAssignmentsForCourse(course);
        assertTrue(assignments.contains(a));
    }

    @Test
    void testGetUngradedAssignments() {
        Assignment a = new Assignment("HW1", 10);
        controller.addAssignment(course, a);
        List<Assignment> ungraded = controller.getUngradedAssignments(course);
        assertTrue(ungraded.contains(a));
    }

    @Test
    void testCalculateClassAverage() {
        Assignment a = new Assignment("HW1", 10);
        controller.addAssignment(course, a);
        controller.assignGrade(course, student, a, 8);
        double avg = controller.calculateClassAverage(course);
        assertEquals(80.0, avg, 0.01);
    }

    @Test
    void testCalculateMedian() {
        Student s2 = new Student("Jake", "Smith", "jake", "pass");
        course.addStudent(s2);
        Assignment a = new Assignment("HW1", 10);
        course.addAssignment(a);
        a.assignGrade(student, 8);
        a.assignGrade(s2, 6);
        double median = controller.calculateMedian(course);
        assertEquals(70.0, median, 0.01);
    }

    @Test
    void testSortStudentsByName() {
        Student s2 = new Student("Zack", "Anderson", "zack", "pass");
        course.addStudent(s2);
        List<Student> sorted = controller.sortStudentsByName(course);
        assertEquals("Anderson", sorted.get(0).getLastName());
    }

    @Test
    void testSortStudentsByAssignmentGrade() {
        Student s2 = new Student("Jake", "Smith", "jake", "pass");
        course.addStudent(s2);
        Assignment a = new Assignment("HW1", 10);
        course.addAssignment(a);
        a.assignGrade(student, 7);
        a.assignGrade(s2, 9);
        List<Student> sorted = controller.sortStudentsByAssignmentGrade(course, a);
        assertEquals(s2, sorted.get(0));
    }

    @Test
    void testGroupStudents_PrintsCorrectly() {
        Student s2 = new Student("Jake", "Smith", "jake", "pass");
        Student s3 = new Student("Eve", "Clark", "eve", "pass");
        course.addStudent(s2);
        course.addStudent(s3);
        controller.groupStudents(course, 2);
        assertEquals(3, course.getStudents().size());
    }
}
