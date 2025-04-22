package model;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseTest {

    private Course course;
    private Student student1;
    private Student student2;
    private Assignment a1;
    private Assignment a2;

    @BeforeEach
    void setup() {
        course = new Course("CSC335");
        student1 = new Student("Alina", "Kushareva", "alina_k", "pass");
        student2 = new Student("Jake", "Smith", "jake_s", "pass");

        a1 = new Assignment("HW1", 10);
        a2 = new Assignment("HW2", 20);
        a1.assignGrade(student1, 8);
        a1.assignGrade(student2, 9);
        a2.assignGrade(student1, 18);
        a2.assignGrade(student2, 10);

        course.addAssignment(a1);
        course.addAssignment(a2);

        course.addStudent(student1);
        course.addStudent(student2);
    }

    @Test
    void testAddStudent_AddsCorrectly() {
        List<Student> students = course.getStudents();
        assertTrue(students.contains(student1));
        assertTrue(students.contains(student2));
    }


    @Test
    void testAddAssignment_AddsSuccessfully() {
        Assignment a3 = new Assignment("HW3", 15);
        course.addAssignment(a3);
        assertTrue(course.getAssignments().contains(a3));
    }

    @Test
    void testCalculateTotalPointsAverage() {
        course.setGradingMode(false);
        double avg = course.calculateStudentAverage(student1);
        double expected = ((8 + 18) / 30.0) * 100;
        assertEquals(expected, avg, 0.01);
    }

    @Test
    void testCalculateClassAverage() {
        course.setGradingMode(false);
        double avg1 = ((8 + 18) / 30.0) * 100;
        double avg2 = ((9 + 10) / 30.0) * 100;
        double expected = (avg1 + avg2) / 2;
        assertEquals(expected, course.calculateClassAverage(), 0.01);
    }

    @Test
    void testSortStudentsByAssignmentGrade() {
        List<Student> sorted = course.sortStudentsByAssignmentGrade(a1);
        assertEquals("Jake", sorted.get(0).getFirstName()); // 9 > 8
    }

    @Test
    void testGetUngradedAssignments_NoneUngraded() {
        assertTrue(course.getUngradedAssignments().isEmpty());
    }

    @Test
    void testGetUngradedAssignments_HasUngraded() {
        Assignment a3 = new Assignment("HW3", 10);
        course.addAssignment(a3);
        a3.assignGrade(student1, 10); // student2 ungraded
        assertEquals(1, course.getUngradedAssignments().size());
    }

    @Test
    void testAssignAndGetFinalGrade() {
        course.assignFinalGrade(student1, FinalGrade.A);
        assertEquals(FinalGrade.A, course.getFinalGrade(student1));
    }

    @Test
    void testIsCompleted_TrueIfFinalGradeAssigned() {
        student1.assignFinalGrade(course, FinalGrade.B);
        assertTrue(course.isCompleted(student1));
    }

    @Test
    void testIsCompleted_FalseIfNoFinalGrade() {
        assertFalse(course.isCompleted(student2));
    }
    
    @Test
    void testSetAssignmentsToDrop_PositiveNumber() {
        course.setAssignmentsToDrop(2);
        assertDoesNotThrow(() -> course.setAssignmentsToDrop(2));
    }

    @Test
    void testSetAssignmentsToDrop_NegativeNumberDefaultsToZero() {
        course.setAssignmentsToDrop(-5);
        assertDoesNotThrow(() -> course.setAssignmentsToDrop(-5));
    }
    
    @Test
    void testGetCourseName_ReturnsCorrectName() {
        assertEquals("CSC335", course.getCourseName());
    }
    
    @Test
    void testCalculateWeightedAverage_UsingReflection() throws Exception {
        Student student = new Student("Anna", "Lee", "anna", "pwd");
        Category category = new Category("Homework", 1.0);
        Assignment hw = new Assignment("HW1", 10);
        category.addAssignment(hw);
        hw.assignGrade(student, 9); // 90%

        course.addAssignment(hw);

        // Inject category list using reflection
        var categoriesField = Course.class.getDeclaredField("categories");
        categoriesField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Category> categories = (List<Category>) categoriesField.get(course);
        categories.add(category);

        // Call the private method via reflection
        Method method = Course.class.getDeclaredMethod("calculateWeightedAverage", Student.class);
        method.setAccessible(true);
        double result = (double) method.invoke(course, student);

        assertEquals(90.0, result, 0.01);
    }
    
    @Test
    void testSortStudentsByName_SortsByLastThenFirstName() {
        Student s1 = new Student("Charlie", "Brown", "cb", "pass");
        Student s2 = new Student("Alice", "Smith", "as", "pass");
        Student s3 = new Student("Bob", "Smith", "bs", "pass");

        Course course = new Course("SortingCourse");
        course.addStudent(s1);
        course.addStudent(s2);
        course.addStudent(s3);

        List<Student> sorted = course.sortStudentsByName();

        assertEquals("Brown", sorted.get(0).getLastName());
        assertEquals("Alice", sorted.get(1).getFirstName());
        assertEquals("Bob", sorted.get(2).getFirstName());
    }

}