/**
 * Project Name: Gradebook
 * File Name: Assignment.java
 * Course: CSC 335 Spring 2025
 * Purpose: Represents an academic assignment in a course.
 *          Stores the maximum possible points and a mapping of student grades.
 *          Supports grading, retrieval, and grading status checks.
 */
package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Assignment {
    private final String title;
    private final double maxPoints;
    private final Map<Student, Grade> studentGrades;
    private final Course course;

    /**
     * Constructs a new assignment tied to a specific course.
     * @param title assignment title
     * @param maxPoints maximum possible score
     * @param course course the assignment belongs to
     */
    public Assignment(String title, double maxPoints, Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        this.title = title;
        this.maxPoints = maxPoints;
        this.course = course;
        this.studentGrades = new HashMap<>();
    }

    /**
     * Assigns a grade to a student for this assignment
     * @param student The student to grade
     * @param points Points earned (must be ≤ maxPoints)
     * @throws IllegalArgumentException if points exceed maxPoints
     */
    public void assignGrade(Student student, double points) {
        if(points > maxPoints) {
            throw new IllegalArgumentException("Points exceed maximum");
        }
        // Create and store grade
        Grade grade = new Grade(points, maxPoints);
        student.addGrade(this, grade);
        studentGrades.put(student, grade);
        course.notifyObservers(); // Notify observers after grade change
    }

    /**
     * Retrieves the student's grade for this assignment.
     * @param student the student to check
     * @return Grade object or null if not graded
     */
    public Grade getGrade(Student student) {
        return studentGrades.get(student);
    }

    /**
     * Checks if all students in a course have been graded
     * @param courseStudents List of students enrolled in the course
     * @return true if all students have grades
     */
    public boolean isFullyGraded(List<Student> courseStudents) {
        return courseStudents.stream().allMatch(studentGrades::containsKey);
    }
    
    /**
     * Checks whether a specific student has been graded.
     * @param student the student to check
     * @return true if the student has a grade for this assignment
     */
    public boolean isGraded(Student student) {
        return studentGrades.containsKey(student);
    }

    /**
     * Gets the title of this assignment.
     * @return the assignment title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the maximum points for this assignment.
     * @return maximum points possible
     */
    public double getMaxPoints() {
        return maxPoints;
    }

    /**
     * Gets a defensive copy of the student grade map.
     * @return map of students to grades
     */
    public Map<Student, Grade> getStudentGrades() {
        return new HashMap<>(studentGrades);
    }

    /**
     * Returns the course this assignment belongs to.
     * @return the associated course
     */
    public Course getCourse() {
        return course;
    }
}
