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
    private final Course course;

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
        Grade grade = new Grade(points, maxPoints);
        student.addGrade(this, grade);
        studentGrades.put(student, grade);
        course.notifyObservers(); // Notify observers after grade change
    }

    /**
     * Retrieves a student's grade for this assignment
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

    public boolean isGraded(Student student) {
        return studentGrades.containsKey(student);
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

    public Course getCourse() {
        return course;
    }
}
