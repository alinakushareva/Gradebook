/*
 * Project Name: Gradebook 
 * File Name: Student.java
 * Course: CSC 335 Spring 2025
 * Purpose: Stores all Student academic information such as assignments, grades, courses
 * 			and final grades.
 */
package model;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


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
		
		this.grades = new HashMap<>();
		this.finalGrades = new HashMap<>();
		
	}
	
	/*
	 * adds the graded assignment to the Student
	 * @returns 
	 */
	public void addGrade(Assignment assignment, Grade grade) {
        grades.put(assignment, grade);
	}
	
	/*
	 * searches for the grade of a specific assignment
	 * @returns the Grade grade of the assignment
	 */
	public Grade getGrade(Assignment assignment) {
		return grades.get(assignment);
	}
	
	/*
	 * gets the average for the course 
	 * @returns the average of the course
	 */
	public double getAverageForCourse(Course course) {
        List<Assignment> courseAssignments = course.getAssignments();
        double totalEarned = 0;
        double totalPossible = 0;
        
        for (Assignment assignment : courseAssignments) {
            Grade grade = grades.get(assignment);
            if (grade != null && grade.getMaxPoints() > 0) {
                totalEarned += grade.getPointsReceived();
                totalPossible += grade.getMaxPoints();
            }
        }
        
        return totalPossible > 0 ? (totalEarned / totalPossible) * 100 : 0.0;
    }
	
	/*
	 * calculates the GPA on the finalGrade of the course
	 * @returns calculated GPA
	 */
	public double calculateGPA() {
        if (finalGrades.isEmpty()) return 0.0;
        
        double total = finalGrades.values().stream()
            .mapToDouble(FinalGrade::getGpaValue)
            .sum();
            
        return total / finalGrades.size();
    }
	
	/*
	 * 
	 */
	public void assignFinalGrade(Course course, FinalGrade grade) {
	    finalGrades.put(course, grade);

	}

	
	/*
	 * 
	 */
	public FinalGrade getFinalGrade(Course course) {
	    return finalGrades.get(course);
    }
	
	// Additional accessors
    public Map<Assignment, Grade> getGrades() {
        return new HashMap<>(grades);
    }

    public Map<Course, FinalGrade> getFinalGrades() {
        return new HashMap<>(finalGrades);
    }
    
    @Override
    public String toFileString() {
        return getUsername() + "," + getFirstName() + " " + getLastName() + "," + getPasswordHash() + ",student";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return this.getUsername().equals(student.getUsername());
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }
    
    

}