package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GradebookModelTest {

    private GradebookModel model;
    private Student student;
    private Teacher teacher;
    private Course course;

    @BeforeEach
    void setUp() {
        model = new GradebookModel();
        student = new Student("Alina", "Kushareva", "alina_k", "pass");
        teacher = new Teacher("john_doe", "John", "Doe", "pass");
        course = new Course("CSC335");
    }

    @Test
    void testAddAndGetStudent() {
        model.addStudent(student);
        Student result = model.getStudentByUsername("alina_k");
        assertEquals(student, result);
    }

    @Test
    void testStudentExists() {
        model.addStudent(student);
        assertTrue(model.studentExists("alina_k"));
    }

    @Test
    void testStudentDoesNotExist() {
        assertFalse(model.studentExists("ghost"));
    }

    @Test
    void testAddAndGetTeacher() {
        model.addTeacher(teacher);
        Teacher result = model.getTeacherByUsername("john_doe");
        assertEquals(teacher, result);
    }

    @Test
    void testTeacherExists() {
        model.addTeacher(teacher);
        assertTrue(model.teacherExists("john_doe"));
    }

    @Test
    void testTeacherDoesNotExist() {
        assertFalse(model.teacherExists("unknown_teacher"));
    }

    @Test
    void testAddAndGetCourse() {
        model.addCourse(course);
        Course result = model.getCourseByName("CSC335");
        assertEquals(course, result);
    }

    @Test
    void testGetAllCourses() {
        Course course2 = new Course("MATH122");
        model.addCourse(course);
        model.addCourse(course2);
        List<Course> allCourses = model.getAllCourses();
        assertTrue(allCourses.contains(course));
        assertTrue(allCourses.contains(course2));
        assertEquals(2, allCourses.size());
    }
    
    @Test
    void testCalculateAssignmentMedian_NoGrades() {
        model.addCourse(course);
        Assignment a = new Assignment("Midterm", 100);
        course.addAssignment(a);

        double median = model.calculateAssignmentMedian(course, a);
        assertEquals(0.0, median);
    }

    @Test
    void testCalculateAssignmentMedian_OddNumberOfGrades() {
        model.addCourse(course);
        Assignment a = new Assignment("Midterm", 100);
        course.addAssignment(a);

        Student s1 = new Student("A", "A", "a1", "123");
        Student s2 = new Student("B", "B", "b2", "123");
        Student s3 = new Student("C", "C", "c3", "123");

        course.addStudent(s1);
        course.addStudent(s2);
        course.addStudent(s3);

        a.assignGrade(s1, 70);
        a.assignGrade(s2, 85);
        a.assignGrade(s3, 90);

        double median = model.calculateAssignmentMedian(course, a);
        assertEquals(85.0, median);
    }

    @Test
    void testCalculateAssignmentMedian_EvenNumberOfGrades() {
        model.addCourse(course);
        Assignment a = new Assignment("Quiz", 100);
        course.addAssignment(a);

        Student s1 = new Student("A", "A", "a1", "123");
        Student s2 = new Student("B", "B", "b2", "123");
        Student s3 = new Student("C", "C", "c3", "123");
        Student s4 = new Student("D", "D", "d4", "123");

        course.addStudent(s1);
        course.addStudent(s2);
        course.addStudent(s3);
        course.addStudent(s4);

        a.assignGrade(s1, 60);
        a.assignGrade(s2, 70);
        a.assignGrade(s3, 80);
        a.assignGrade(s4, 90);

        double median = model.calculateAssignmentMedian(course, a);
        assertEquals(75.0, median); // (70 + 80) / 2
    }

}
