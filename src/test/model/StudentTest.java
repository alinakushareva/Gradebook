package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class StudentTest {
	
	// Test data matching your Student class
	private static final String firstName = "Alex";
	private static final String lastName = "Russo";
	private static final String userName = "aRusso";
	private static final String password = "Wizards123";
	
	private Student s;
	private Assignment a;
	private Grade g;
	
	@BeforeEach
	void setUp() {
		s = new Student(userName, firstName, lastName, password);
	}
	
	@Test
	void constructor_test() {
		assertEquals(userName, s.getUsername());
		assertEquals(password, s.getPasswordHash());
		assertEquals("Alex Russo", s.getFullName());
		assertEquals("Student", s.getRole());
		
	}

	@Test
	void addGradeTest() {
		g = new Grade(20.0, 20.0);
		a = new Assignment("Quiz 1", 20.0);
		
		s.addGrade(a,g);
		assertEquals("Quiz 1", )
	}
	
}