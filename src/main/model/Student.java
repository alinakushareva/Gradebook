/*
 * Project Name: Gradebook 
 * File Name: Student.java
 * Course: CSC 335 Spring 2025
 * Purpose: Stores all Student academic information such as assignments, grades, courses
 * 			and final grades.
 */
package main.model;

import java.util.HashMap;
import java.util.Map;

/*
 * This class represents a student and their grades.
 */
public class Student extends User {
	
	private Map<Assignment, Grade> grades;
	private Map<Course, FinalGrade> finalGrades;
	
	/*
	 * Constructor for Student class.
	 * 
	 * @param firstName - Student's first name
	 * @param lastName - Student's last name
	 * @param userName - unique identifier for the student
	 * @param password - student creates a password to protect their information
	 */
	
	public Student(String firstName, String lastName, String userName, String password) {
		
		super(userName, firstName, lastName, password, "Student");
		
		this.grades = new HashMap<Assignment, Grade>();
		this.finalGrades = new HashMap<Course, FinalGrade>();
		
	}
	
	/*
	 * adds the graded assignment to the Student
	 * @returns 
	 */
	public void addGrade(Assignment a, Grade g) {
		this.grades.put(a, g);
	}
	
	/*
	 * searches for the grade of a specific assignment
	 * @returns the Grade grade of the assignment
	 */
	public Grade getGrade(Assignment a) {
		
		for(Assignment aName : this.grades.keySet()) {
			if(aName.equals(a)) {
				return this.grades.values();
			}
		}
	}
	
	/*
	 * gets the average for the course 
	 * @returns the average of the course
	 */
	public double getAverageForCourse(Course c) {
		return c.
	}
	
	/*
	 * calculates the GPA on the finalGrade of the course
	 * @returns calculated GPA
	 */
	public double calculateGPA() {
		this.finalGrades.values().getGpaValue();
	}
	
	/*
	 * 
	 */
	public void assignFinalGrade(Course c, FinalGrade g) {
		this.finalGrades.put(c, g);
	}
	
	/*
	 * 
	 */
	public FinalGrade getFinalGrade(Course c) {
		
		for(Course course : this.finalGrades.keySet()) {
			if(course.equals(c)) {
				return this.finalGrades.values();
			}
		}
	}
}