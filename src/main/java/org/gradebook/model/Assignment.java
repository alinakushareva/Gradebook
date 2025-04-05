package org.gradebook.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents an assignment in a course.
 * Contains information such as title, maximum points, and the category it belongs to.
 */
public class Assignment {
    private String title;
    private double maxPoints;
    private String category;
    private Map<Student, Grade> studentGrades;
    
    /**
     * Constructor for Assignment class.
     *
     * @param title     the title of the assignment
     * @param maxPoints the maximum points possible
     * @param category  the category this assignment belongs to (optional)
     */
    public Assignment(String title, double maxPoints, String category) {
        this.title = title;
        this.maxPoints = maxPoints;
        this.category = category;
        this.studentGrades = new HashMap<>();
    }
    
    /**
     * Constructor for Assignment class without a category.
     *
     * @param title     the title of the assignment
     * @param maxPoints the maximum points possible
     */
    public Assignment(String title, double maxPoints) {
        this(title, maxPoints, null);
    }
    
    /**
     * Gets the title of the assignment.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Sets the title of the assignment.
     *
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Gets the maximum points possible for this assignment.
     *
     * @return the maximum points
     */
    public double getMaxPoints() {
        return maxPoints;
    }
    
    /**
     * Sets the maximum points possible for this assignment.
     *
     * @param maxPoints the maximum points to set
     */
    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }
    
    /**
     * Gets the category this assignment belongs to.
     *
     * @return the category name, or null if none
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Sets the category this assignment belongs to.
     *
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
    /**
     * Assigns a grade to a student for this assignment.
     *
     * @param student       the student
     * @param pointsReceived the points the student received
     */
    public void assignGrade(Student student, double pointsReceived) {
        Grade grade = new Grade(pointsReceived, maxPoints);
        studentGrades.put(student, grade);
        student.addGrade(this, grade);
    }
    
    /**
     * Gets the grade for a specific student.
     *
     * @param student the student
     * @return the grade, or null if not graded
     */
    public Grade getGrade(Student student) {
        return studentGrades.get(student);
    }
    
    /**
     * Checks if a student has been graded for this assignment.
     *
     * @param student the student
     * @return true if graded, false otherwise
     */
    public boolean isGraded(Student student) {
        return studentGrades.containsKey(student);
    }
    
    /**
     * Calculates the average grade for this assignment.
     *
     * @return the average grade, or 0 if no grades
     */
    public double getAverage() {
        if (studentGrades.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (Grade grade : studentGrades.values()) {
            sum += grade.getPercentage();
        }
        
        return sum / studentGrades.size();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assignment that = (Assignment) o;
        return Objects.equals(title, that.title);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
    
    @Override
    public String toString() {
        return "Assignment{" +
                "title='" + title + '\'' +
                ", maxPoints=" + maxPoints +
                ", category='" + category + '\'' +
                ", gradedCount=" + studentGrades.size() +
                '}';
    }
} 