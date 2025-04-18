package test.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import model.Student;

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
<<<<<<< Updated upstream
	
	
}
=======
	void constructor_setUsername() {
		assertEquals(userName,s.getUsername());
	}
	
	@Test
	void constructor_sestPasswordHash() {
		assertEquals(password, s.getPasswordHash());
	}
	
	@Test
	void constructor_ReturnsFullName() {
		assertEquals("Alex Russo", s.getFullName());
	}
	
	@Test
	void constructor_role() {
		asserEquals("Student", s.getRole());
	}
	
	
	@Test
	void testAddGrade() {		
		g = new Grade(20.0,20.0);
		a = new Assignment("Quiz 1", 20.0);
		
		s.addGrade(a, g);
		
		
	}
}
>>>>>>> Stashed changes
