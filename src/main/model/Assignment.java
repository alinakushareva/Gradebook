package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an academic assignment within a course.
 * Tracks grades for students enrolled in the associated course.
 */
public class Assignment {
    private final String title;
    private final double maxPoints;
    private final Map<Student, Grade> studentGrades;

    public Assignment(String title, double maxPoints) {
        this.title = title;
        this.maxPoints = maxPoints;
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
        Grade grade = new Grade(points, maxPoints);
        student.addGrade(this, grade);
        studentGrades.put(student, grade);
    }

    /**
     * Retrieves a student's grade for this assignment
     * @return Grade object (returns 0/MAX if ungraded)
     */
    public Grade getGrade(Student student) {
        return studentGrades.getOrDefault(student, new Grade(0, maxPoints));
    }

    /**
     * Checks if all students in a course have been graded
     * @param courseStudents List of students enrolled in the course
     * @return true if all students have grades
     */
    public boolean isFullyGraded(List<Student> courseStudents) {
        return courseStudents.stream()
            .allMatch(studentGrades::containsKey);
    }
    
    public boolean isGraded(Student student) {
        return student.getGrade(this) != null;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public Map<Student, Grade> getStudentGrades() {
        return new HashMap<>(studentGrades);
    }
}