package model;

import static org.junit.jupiter.api.Assertions.*;

<<<<<<< Updated upstream
=======
import java.util.List;

>>>>>>> Stashed changes
import org.junit.jupiter.api.Test;

class AssignmentTest {
	
<<<<<<< Updated upstream
	private static final String firstName = "Alex";
	private static final String lastName = "Russo";
	private static final String userName = "aRusso";
	private static final String password = "Wizards123";
	
	private Assignment a, b;
	private Student s;
	
	
	@BeforeEach
	void constructor_Assignment() {
		a = new Assignment("Quiz 1", 25.0);
		b = new Assignment("Exam 1", 75.0);
		s = new Student(firstName, lastName, userName, password);

	}

	@Test
	void getGradeTest() {
		
=======
	private Assignment a1, a2, a3;
	private Student s1, s2;
	@BeforeEach
	void setUp() {
		a1 = new Assignment("SA1", 25.0);
		a2 = new Assignment("Quiz 1", 20.0);
		a3 = new Assignment("Exam 1", 125.0);
		
		s1 = new Student("Audrina", "Campa", "campa1", "Password123!");
		s2 = new Student("Jack", "Sparrow", "CaptainJack", "BlackPearl123");
	}
	
	@Test
	void testIsFullyGraded() {
		a1.assignGrade(s1, 19.0);
		a2.assignGrade(s1, 4.0);
		a3.assignGrade(s1, 45.0);
		
		a1.assignGrade(s2, 24.0);
		a2.assignGrade(s2, 15.0);
		a3.assignGrade(s2, 85.0);
		
		List<Student> students = List.of(s1, s2);
		assertTrue(a1.isFullyGraded(students));
		assertTrue(a2.isFullyGraded(students));
		assertTrue(a3.isFullyGraded(students));
	}
	
	@Test
	void testIsFullyGradedFail() {
		a2.assignGrade(s2, 19.0);
		a3.assignGrade(s2, 75.0);
		
		List<Student> students = List.of(s1, s2);
		assertFalse(a2.isFullyGraded(students));
	}
	
	@Test
	void testAssignGrade() {
		a3.assignGrade(s2, 95.0);
		Grade g = a3.getGrade(s2);
		assertNotNull(g);
		assertEquals(95.0, g.getPointsReceived());
		assertEquals(125.0, g.getMaxPoints());
		assertEquals(79.17, g.getPercentage(), 0.01);
	}
	
	@Test
	void testIsGradedFalse() {
		assertFalse(a3.isGraded(s2));
	}
	
	@Test
	void testIsGradeTrue() {
		a2.assignGrade(s1, 19.50);
		assertTrue(a2.isGraded(s1));
	}
	
	@Test
	void testTitleGetters() {
		assertEquals("SA1", a1.getTitle());
		assertEquals("Quiz 1", a2.getTitle());
		assertEquals("Exam 1", a3.getTitle());
	}
	
	@Test
	void testMaxPointsGetter() {
		assertEquals(25.0, a1.getMaxPoints());
		assertEqauls(25.0, a2.getMaxPoints());
		assertEqauls(20.0, a3.getMaxPoints());
		assertEquals(20.0, a4.getMaxPoints());
		assertEquals(125.0, a5.getMaxPoints());
	}
	
	@Test
	void testAssignGradeExceedMax() {
		assertThrows(IllegalArgumentException.class, () ->{
			assignment.assignGrade(s2, 40.0);
		});
>>>>>>> Stashed changes
	}
}
