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

    // --- Student operations ---
    @Test
    void testAddAndGetStudent() {
        model.addStudent(student);
        assertEquals(student, model.getStudentByUsername("alina_k"));
    }

    @Test
    void testStudentExistsAndDoesNotExist() {
        assertFalse(model.studentExists("ghost"));
        model.addStudent(student);
        assertTrue(model.studentExists("alina_k"));
    }

    // --- Teacher operations ---
    @Test
    void testAddAndGetTeacher() {
        model.addTeacher(teacher);
        assertEquals(teacher, model.getTeacherByUsername("john_doe"));
    }

    @Test
    void testTeacherExistsAndDoesNotExist() {
        assertFalse(model.teacherExists("unknown"));
        model.addTeacher(teacher);
        assertTrue(model.teacherExists("john_doe"));
    }

    // --- Course operations ---
    @Test
    void testAddAndGetCourse() {
        model.addCourse(course);
        assertEquals(course, model.getCourseByName("CSC335"));
    }

    @Test
    void testGetAllCourses() {
        Course c2 = new Course("MATH122");
        model.addCourse(course);
        model.addCourse(c2);

        List<Course> all = model.getAllCourses();
        assertTrue(all.contains(course));
        assertTrue(all.contains(c2));
        assertEquals(2, all.size());
    }

    // --- Median calculations ---
    @Test
    void testCalculateAssignmentMedian_NoGrades() {
        model.addCourse(course);
        Assignment a = new Assignment("Test", 100, course);
        course.addAssignment(a);

        assertEquals(0.0, model.calculateAssignmentMedian(course, a));
    }

    @Test
    void testCalculateAssignmentMedian_Odd() {
        model.addCourse(course);
        Assignment a = new Assignment("Midterm", 100, course);
        course.addAssignment(a);

        Student s1 = new Student("A", "A", "a1", "123");
        Student s2 = new Student("B", "B", "b2", "123");
        Student s3 = new Student("C", "C", "c3", "123");

        course.addStudent(s1);
        course.addStudent(s2);
        course.addStudent(s3);

        a.assignGrade(s1, 60);
        a.assignGrade(s2, 85);
        a.assignGrade(s3, 100);

        assertEquals(85.0, model.calculateAssignmentMedian(course, a));
    }

    @Test
    void testCalculateAssignmentMedian_Even() {
        model.addCourse(course);
        Assignment a = new Assignment("Final", 100, course);
        course.addAssignment(a);

        Student s1 = new Student("A", "A", "a1", "123");
        Student s2 = new Student("B", "B", "b2", "123");
        Student s3 = new Student("C", "C", "c3", "123");
        Student s4 = new Student("D", "D", "d4", "123");

        course.addStudent(s1);
        course.addStudent(s2);
        course.addStudent(s3);
        course.addStudent(s4);

        a.assignGrade(s1, 70);
        a.assignGrade(s2, 80);
        a.assignGrade(s3, 90);
        a.assignGrade(s4, 100);

        assertEquals(85.0, model.calculateAssignmentMedian(course, a));
    }

    // --- All users (students + teachers) ---
    @Test
    void testGetAllUsers() {
        model.addStudent(student);
        model.addTeacher(teacher);

        List<User> allUsers = model.getAllUsers();
        assertTrue(allUsers.contains(student));
        assertTrue(allUsers.contains(teacher));
        assertEquals(2, allUsers.size());
    }

    // --- Observer pattern ---
    static class DummyObserver implements Observer {
        boolean updated = false;
        @Override
        public void update() {
            updated = true;
        }
    }

    @Test
    void testObserverNotifications() {
        DummyObserver observer = new DummyObserver();
        model.addObserver(observer);

        model.addCourse(course);  // should notify
        assertTrue(observer.updated);

        model.removeObserver(observer);
        observer.updated = false;
        model.notifyObservers();  // should NOT notify now
        assertFalse(observer.updated);
    }
}
