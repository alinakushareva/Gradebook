package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AssignmentTest {
	
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
	}
}
