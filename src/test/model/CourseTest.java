package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

        a1 = new Assignment("HW1", 10, course);
        a2 = new Assignment("HW2", 20, course);
        course.addAssignment(a1);
        course.addAssignment(a2);
        course.addStudent(student1);
        course.addStudent(student2);

        a1.assignGrade(student1, 8);
        a1.assignGrade(student2, 9);
        a2.assignGrade(student1, 18);
        a2.assignGrade(student2, 10);
    }

    @Test
    void testAddAndRemoveAssignment() {
        Assignment a3 = new Assignment("HW3", 15, course);
        course.addAssignment(a3);
        assertTrue(course.getAssignments().contains(a3));

        course.removeAssignment(a3);
        assertFalse(course.getAssignments().contains(a3));
    }


    @Test
    void testSortStudentsByAssignmentGrade() {
        List<Student> sorted = course.sortStudentsByAssignmentGrade(a1);
        assertEquals("Jake", sorted.get(0).getFirstName()); // 9 > 8
    }

    @Test
    void testSortStudentsByName() {
        Student s1 = new Student("Charlie", "Brown", "cb", "pass");
        Student s2 = new Student("Alice", "Smith", "as", "pass");
        Student s3 = new Student("Bob", "Smith", "bs", "pass");
        Course c = new Course("SortTest");
        c.addStudent(s1);
        c.addStudent(s2);
        c.addStudent(s3);

        List<Student> sorted = c.sortStudentsByName();
        assertEquals("Brown", sorted.get(0).getLastName());
        assertEquals("Alice", sorted.get(1).getFirstName());
        assertEquals("Bob", sorted.get(2).getFirstName());
    }

    @Test
    void testGetUngradedAssignments() {
        Assignment a3 = new Assignment("HW3", 10, course);
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
    void testIsCompletedTrue() {
        student1.assignFinalGrade(course, FinalGrade.B);
        assertTrue(course.isCompleted(student1));
    }

    @Test
    void testIsCompletedFalse() {
        assertFalse(course.isCompleted(student2));
    }

    @Test
    void testSetAssignmentsToDropPositiveAndNegative() {
        course.setAssignmentsToDrop(2);
        assertEquals(2, course.getAssignmentsToDrop());

        course.setAssignmentsToDrop(-5);
        assertEquals(0, course.getAssignmentsToDrop());
    }

    @Test
    void testGetCourseName() {
        assertEquals("CSC335", course.getCourseName());
    }

    @Test
    void testWeightedAverageZeroWeightReturnsZero() throws Exception {
        Student s = new Student("Zero", "Weight", "zw", "pw");
        Method method = Course.class.getDeclaredMethod("calculateWeightedAverage", Student.class);
        method.setAccessible(true);
        double result = (double) method.invoke(course, s);
        assertEquals(0.0, result, 0.01);
    }

    @Test
    void testAddCategory_SuccessfulWhenWeightIsValid() {
        Category cat = new Category("Homework", 0.5);
        assertTrue(course.addCategory(cat));
        assertTrue(course.getCategories().contains(cat));
    }

    @Test
    void testAddCategory_FailsWhenExceedingWeight() {
        Category cat1 = new Category("Cat1", 0.7);
        Category cat2 = new Category("Cat2", 0.5);
        course.addCategory(cat1);
        assertFalse(course.addCategory(cat2));
    }

    @Test
    void testRemoveCategory_SuccessAndFail() {
        Category cat = new Category("Projects", 0.5);
        course.addCategory(cat);
        assertTrue(course.removeCategory("Projects"));
        assertFalse(course.removeCategory("Nonexistent"));
    }

    @Test
    void testGetCategories() {
        Category cat = new Category("Quizzes", 0.3);
        course.addCategory(cat);
        assertTrue(course.getCategories().contains(cat));
    }

    @Test
    void testObservers_AddRemoveNotify() {
        DummyObserver obs = new DummyObserver();
        course.addObserver(obs);
        assertFalse(obs.updated);

        course.assignFinalGrade(student1, FinalGrade.A);
        assertTrue(obs.updated);

        obs.updated = false;
        course.removeObserver(obs);
        course.notifyObservers();
        assertFalse(obs.updated);
    }

    // Dummy observer for test
    static class DummyObserver implements Observer {
        boolean updated = false;
        public void update() {
            updated = true;
        }
    }
    
    @Test
    void testSetGradingMode() {
        Course course = new Course("CSC101");

        course.setGradingMode(true);

        Student student = new Student("Test", "Student", "s123", "pw");
        course.addStudent(student);

        Category category = new Category("Homework", 1.0);
        course.addCategory(category);

        Assignment a = new Assignment("HW1", 10, course);
        course.addAssignment(a);
        category.addAssignment(a);
        a.assignGrade(student, 9);

        course.setGradingMode(true);
        double weighted = course.calculateStudentAverage(student);
        assertEquals(90.0, weighted, 0.01);

        course.setGradingMode(false);
        double totalPoints = course.calculateStudentAverage(student);
        assertEquals(90.0, totalPoints, 0.01);
    }

}
