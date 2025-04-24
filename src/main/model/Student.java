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
	
	/**
     * Adds a grade for a specific assignment.
     *
     * @param assignment the assignment graded
     * @param grade the grade received
     */
	public void addGrade(Assignment assignment, Grade grade) {
        grades.put(assignment, grade);
	}
	
	 /**
     * Gets the grade for a specific assignment.
     *
     * @param assignment the assignment
     * @return the Grade or null if not found
     */
	public Grade getGrade(Assignment assignment) {
		return grades.get(assignment);
	}
	
	 /**
     * Calculates the student's average for a specific course.
     *
     * @param course the course to evaluate
     * @return percentage average across assignments
     */
	public double getAverageForCourse(Course course) {
        List<Assignment> courseAssignments = course.getAssignments();
        double totalEarned = 0;
        double totalPossible = 0;
        
        // Iterate over course assignments
        for (Assignment assignment : courseAssignments) {
            Grade grade = grades.get(assignment);
            if (grade != null && grade.getMaxPoints() > 0) {
                totalEarned += grade.getPointsReceived(); // Sum up earned points
                totalPossible += grade.getMaxPoints(); // Sum up possible points
            }
        }
        
        return totalPossible > 0 ? (totalEarned / totalPossible) * 100 : 0.0;
    }
	
	/**
     * Calculates the GPA based on final grades.
     *
     * @return GPA value as double
     */
	public double calculateGPA() {
        if (finalGrades.isEmpty()) return 0.0;
        
        // Sum GPA values of all final grades
        double total = finalGrades.values().stream()
            .mapToDouble(FinalGrade::getGpaValue)
            .sum();
            
        return total / finalGrades.size(); // Compute average
    }
	
	/**
     * Assigns a final grade for a course.
     *
     * @param course the course
     * @param grade  the final grade to assign
     */
	public void assignFinalGrade(Course course, FinalGrade grade) {
	    finalGrades.put(course, grade);

	}

	
	/**
     * Retrieves the final grade for a specific course.
     *
     * @param course the course
     * @return the final grade or null
     */
	public FinalGrade getFinalGrade(Course course) {
	    return finalGrades.get(course);
    }
	
	/**
     * Gets a defensive copy of all assignment grades.
     *
     * @return Map of Assignment to Grade
     */
    public Map<Assignment, Grade> getGrades() {
        return new HashMap<>(grades);
    }

    /**
     * Gets a defensive copy of all final grades.
     *
     * @return Map of Course to FinalGrade
     */
    public Map<Course, FinalGrade> getFinalGrades() {
        return new HashMap<>(finalGrades);
    }
    
    /**
     * Formats the student for file saving.
     *
     * @return CSV-style string for file storage
     */
    @Override
    public String toFileString() {
        return getUsername() + "," + getFirstName() + " " + getLastName() + "," + getPasswordHash() + ",student";
    }
    
    /**
     * Checks object equality by username.
     *
     * @param obj the object to compare
     * @return true if usernames match
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return this.getUsername().equals(student.getUsername());
    }

    /**
     * Computes hash code based on username.
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }
}