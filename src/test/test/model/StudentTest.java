package test.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import main.model.Assignment;
import main.model.Grade;
import main.model.Student;

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
	

	
	
}