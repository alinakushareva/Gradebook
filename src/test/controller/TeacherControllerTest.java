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
        model.addTeacher(teacher);
        student = new Student("Alina", "Kushareva", "alina_k", "pass");
        model.addStudent(student);
        course = new Course("CSC335");
        course.addStudent(student);
        teacher.addCourse(course);
        model.addCourse(course);
        controller = new TeacherController(teacher, model);
    }

    @Test
    void testGetTeachingCourses() {
        List<Course> courses = controller.getTeachingCourses();
        assertTrue(courses.contains(course));
    }

    @Test
    void testAddAssignment() {
        Assignment a = new Assignment("HW1", 10, course);
        controller.addAssignment(course, a);
        assertTrue(course.getAssignments().contains(a));
    }

    @Test
    void testAssignGrade() {
        Assignment a = new Assignment("HW1", 10, course);
        controller.addAssignment(course, a);
        controller.assignGrade(course, student, a, 9);
        assertEquals(9, a.getGrade(student).getPointsReceived());
    }

    @Test
    void testAssignFinalGrade() {
        controller.assignFinalGrade(course, student, FinalGrade.B);
        assertEquals(FinalGrade.B, course.getFinalGrade(student));
    }

    @Test
    void testGetStudentsForCourse() {
        List<Student> students = controller.getStudentsForCourse(course);
        assertTrue(students.contains(student));
    }

    @Test
    void testGetAssignmentsForCourse() {
        Assignment a = new Assignment("HW1", 10, course);
        controller.addAssignment(course, a);
        List<Assignment> assignments = controller.getAssignmentsForCourse(course);
        assertTrue(assignments.contains(a));
    }

    @Test
    void testGetUngradedAssignments() {
        Assignment a = new Assignment("HW1", 10, course);
        controller.addAssignment(course, a);
        List<Assignment> ungraded = controller.getUngradedAssignments(course);
        assertTrue(ungraded.contains(a));

        controller.assignGrade(course, student, a, 10);
        ungraded = controller.getUngradedAssignments(course);
        assertFalse(ungraded.contains(a));
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
        Assignment a = new Assignment("HW1", 10, course);
        course.addAssignment(a);
        a.assignGrade(student, 6);
        a.assignGrade(s2, 9);
        List<Student> sorted = controller.sortStudentsByAssignmentGrade(course, a);
        assertEquals(s2, sorted.get(0)); // Highest grade first
    }

    @Test
    void testGroupStudents_PrintsCorrectly() {
        Student s2 = new Student("Jake", "Smith", "jake", "pass");
        Student s3 = new Student("Eve", "Clark", "eve", "pass");
        course.addStudent(s2);
        course.addStudent(s3);

        assertDoesNotThrow(() -> controller.groupStudents(course, 2));
    }

    @Test
    void testCalculateMedian_NoStudentsReturnsZero() {
        double median = controller.calculateMedian(course);
        assertEquals(0.0, median);
    }
    
    @Test
    void testCalculateMedian_EvenNumberOfStudents() {
        course = new Course("Test Course");
        controller = new TeacherController(teacher, model);

        course.setGradingMode(false); 

        Category cat = new Category("HW", 1.0);
        course.addCategory(cat);

        Assignment a = new Assignment("A1", 10, course);
        course.addAssignment(a);
        cat.addAssignment(a);

        Student s1 = new Student("A", "One", "s1", "pw");
        Student s2 = new Student("B", "Two", "s2", "pw");

        course.addStudent(s1);
        course.addStudent(s2);

        a.assignGrade(s1, 6);  // 60%
        a.assignGrade(s2, 8);  // 80%

        double median = controller.calculateMedian(course);
        assertEquals(70.0, median, 0.01);  // (60 + 80) / 2
    }
}