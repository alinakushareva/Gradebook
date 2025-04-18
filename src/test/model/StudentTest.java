package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class StudentTest {
	
	private Student student;
	private Course course;
	private Assignment a1, a2;
	
	@BeforeEach
	void setUp() {
		student = new Student("Elizabeth", "Swann", "eSwann1807", "Pirates000");
		course = new Course("World History");
		
		a1 = new Assignment("Quiz 1", 25.0);
		a2 = new Assignment("Homework 1", 25.0);
		
		course.addAssignment(a1);
		course.addAssignment(a2);
	}
	
	@Test
	void testAddGrade() {
		Grade g = new Grade(15.0, 25.0);
		student.addGrade(a1, g);
		
		Grade getGrade = student.getGrade(a1);
		assertNotNull(getGrade);
		assertEquals(15.0, getGrade.getPointsReceived());
		assertEquals(25.0, getGrade.getMaxPoints());
	}
	
	@Test
	void testGetGradeNotAssigned() {
		assertNull(student.getGrade(a1));
	}
	
	@Test
	void testGetAvgForCourse() {
		student.addGrade(a1, new Grade(25.0, 25.0)); //100%
		student.addGrade(a2, new Grade(15.0, 25.0)); //60%
		
		double avg = student.getAverageForCourse(course);
		assertEquals(80.0, avg, 0.1);
	}
	
	@Test
	void testGetAvgForCourseFail() {
		double avg = student.getAverageForCourse(course);
		assertEquals(0.0, avg);
	}
	
	@Test
	void testAssignAndGetFinalGrade() {
		student.assignFinalGrade(course, FinalGrade.A);
		assertEquals(FinalGrade.A, student.getFinalGrade(course));
	}
	
	@Test
	void testGetFinalFail() {
		assertNull(student.getFinalGrade(course));
	}
	
	@Test
	void testCalculateGPA() {
		Course course2 = new Course("U.S. History");
		
		student.assignFinalGrade(course, FinalGrade.B);
		student.assignFinalGrade(course2, FinalGrade.A);
		
		assertEquals(3.5, student.calculateGPA(), 0.01);
	}
	
	@Test
	void testCalculateGPAFail() {
		assertEquals(0.0, student.calculateGPA());
	}
	
	@Test
	void testGetGrades() {
		student.addGrade(a1, new Grade(20.0, 25.0));
		Map<Assignment, Grade> grades = student.getGrades();
		assertEquals(1, grades.size());
		grade.clear(); // ensure original is not modified
		assertEquals(1, student.getGrades().size());
	}
	
	@Test
	void testGetFinalGrades() {
		student.assignFinalGrade(course, FinalGrade.A);
		Map<Course, FinalGrade> finalG = student.getFinalGrades();
		assertEquals(1, finalG.size());
		finalG.clear(); // ensure original is not modified
		assertEquals(1, student.getFinalGrades().size());
	}
}