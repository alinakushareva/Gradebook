package model;

import java.util.Map;

/*
 * This class represents a student and their grades.
 */
public class Student extends User{
	private Map<Assignment, Grade> grades; //Tacks grades per assignment
	private Map<Course, FinalGrade> finalGrades; //Final letter grades per course
	
	public Student() {
		
	}
	
	public void addGrade(Assignment a, Grade g) {
		
	}
	
	public Grade getGrade(Assignment a) {
		
	}
	
	public double getAvgForCourse(Course c) {
		
	}
	
	public double calculateGPA() {
		
	}
	
	public void assignFinalGrade(Course c, FinalGrade g) {
		
	}
	
	public FinalGrade getFinalGrade(Course c) {
		
	}
}
