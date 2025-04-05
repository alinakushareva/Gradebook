package org.gradebook.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a student in the gradebook system.
 * Extends the User class and adds functionality for tracking grades.
 */
public class Student extends User {
    private Map<Assignment, Grade> grades;
    private Map<Course, FinalGrade> finalGrades;
    
    /**
     * Constructor for Student class.
     *
     * @param username     unique identifier for the student
     * @param firstName    student's first name
     * @param lastName     student's last name
     * @param passwordHash encrypted password
     */
    public Student(String username, String firstName, String lastName, String passwordHash) {
        super(username, firstName, lastName, passwordHash, "student");
        this.grades = new HashMap<>();
        this.finalGrades = new HashMap<>();
    }
    
    /**
     * Adds a grade for a specific assignment.
     *
     * @param assignment the assignment
     * @param grade      the grade received
     */
    public void addGrade(Assignment assignment, Grade grade) {
        grades.put(assignment, grade);
    }
    
    /**
     * Gets the grade for a specific assignment.
     *
     * @param assignment the assignment
     * @return the grade, or null if not graded
     */
    public Grade getGrade(Assignment assignment) {
        return grades.get(assignment);
    }
    
    /**
     * Gets the average grade for a specific course.
     *
     * @param course the course
     * @return the average grade, or -1 if no grades
     */
    public double getAverageForCourse(Course course) {
        return course.calculateStudentAverage(this);
    }
    
    /**
     * Calculates the student's GPA across all courses.
     *
     * @return the GPA, or 0 if no final grades
     */
    public double calculateGPA() {
        if (finalGrades.isEmpty()) {
            return 0.0;
        }
        
        double totalPoints = 0.0;
        int courseCount = 0;
        
        for (FinalGrade grade : finalGrades.values()) {
            totalPoints += grade.getGpaValue();
            courseCount++;
        }
        
        return courseCount > 0 ? totalPoints / courseCount : 0.0;
    }
    
    /**
     * Assigns a final grade for a specific course.
     *
     * @param course the course
     * @param grade  the final grade
     */
    public void assignFinalGrade(Course course, FinalGrade grade) {
        finalGrades.put(course, grade);
    }
    
    /**
     * Gets the final grade for a specific course.
     *
     * @param course the course
     * @return the final grade, or null if not assigned
     */
    public FinalGrade getFinalGrade(Course course) {
        return finalGrades.get(course);
    }
    
    /**
     * Gets all assignment grades for this student.
     *
     * @return a map of assignments to grades
     */
    public Map<Assignment, Grade> getGrades() {
        return new HashMap<>(grades);
    }
    
    /**
     * Gets all final grades for this student.
     *
     * @return a map of courses to final grades
     */
    public Map<Course, FinalGrade> getFinalGrades() {
        return new HashMap<>(finalGrades);
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "username='" + getUsername() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", grades=" + grades.size() +
                ", finalGrades=" + finalGrades.size() +
                ", GPA=" + String.format("%.2f", calculateGPA()) +
                '}';
    }
} 