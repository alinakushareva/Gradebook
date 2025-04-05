package main.model;

import model.Assignment;
import model.Grade;
import model.FinalGrade;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseTest {

    private Course course;
    private Student student;
    private Assignment assignment;

    @BeforeEach
    void setUp() {
        course = new Course("Java");
        student = mock(Student.class);
        assignment = mock(Assignment.class);
        when(student.getFullName()).thenReturn("Alice Smith");
    }

    @Test
    void testAddStudent() {
        course.addStudent(student);
        assertEquals(1, course.sortStudentsByName().size());
        verify(student).addCourse(course);
    }

    @Test
    void testRemoveStudent() {
        course.addStudent(student);
        course.removeStudent(student);
        assertEquals(0, course.sortStudentsByName().size());
    }

    @Test
    void testAddAssignment() {
        course.addAssignment(assignment);
        assertEquals(1, course.getAssignments().size());
    }

    @Test
    void testAssignFinalGrade() {
        course.addStudent(student);
        course.assignFinalGrade(student, FinalGrade.A);
        verify(student).assignFinalGrade(course, FinalGrade.A);
    }

    @Test
    void testGetUngradedAssignments() {
        course.addStudent(student);
        course.addAssignment(assignment);
        when(assignment.isGraded(student)).thenReturn(false);
        List<Assignment> ungraded = course.getUngradedAssignments();
        assertTrue(ungraded.contains(assignment));
    }

    @Test
    void testCalculateMedianForAssignment() {
        Student s1 = mock(Student.class);
        Student s2 = mock(Student.class);
        when(assignment.getGrade(s1)).thenReturn(new Grade(8, 10));
        when(assignment.getGrade(s2)).thenReturn(new Grade(6, 10));

        course.addStudent(s1);
        course.addStudent(s2);
        course.addAssignment(assignment);

        double median = course.calculateMedianForAssignment(assignment);
        assertEquals(70.0, median); // (60 + 80) / 2
    }

    @Test
    void testCalculateStudentAverage() {
        Grade grade = new Grade(8, 10);
        when(assignment.getGrade(student)).thenReturn(grade);
        course.addStudent(student);
        course.addAssignment(assignment);
        assertEquals(80.0, course.calculateStudentAverage(student));
    }

    @Test
    void testSortStudentsByName() {
        Student s2 = mock(Student.class);
        when(s2.getFullName()).thenReturn("Bob Brown");

        course.addStudent(student);
        course.addStudent(s2);

        List<Student> sorted = course.sortStudentsByName();
        assertEquals("Alice Smith", sorted.get(0).getFullName());
    }

    @Test
    void testSortStudentsByGrade() {
        Student s1 = mock(Student.class);
        Student s2 = mock(Student.class);
        when(assignment.getGrade(s1)).thenReturn(new Grade(10, 10)); // 100%
        when(assignment.getGrade(s2)).thenReturn(new Grade(5, 10));  // 50%

        course.addStudent(s1);
        course.addStudent(s2);

        List<Student> sorted = course.sortStudentsByGrade(assignment);
        assertEquals(s1, sorted.get(0));
    }

    @Test
    void testCalculateClassAverage() {
        Student s1 = mock(Student.class);
        Student s2 = mock(Student.class);
        when(s1.calculateGPA()).thenReturn(4.0);
        when(s2.calculateGPA()).thenReturn(3.0);

        course.addStudent(s1);
        course.addStudent(s2);

        double average = course.calculateClassAverage();
        assertEquals(3.5, average);
    }
}

