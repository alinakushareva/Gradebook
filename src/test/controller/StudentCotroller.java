
package org.gradebook.controller;

import org.gradebook.model.Assignment;
import org.gradebook.model.Course;
import org.gradebook.model.Grade;
import org.gradebook.model.GradebookModel;
import org.gradebook.model.Student;

import java.util.List;

/**
 * Controller for student-related operations.
 * Handles student view logic and interactions with the model.
 */
public class StudentController {
    private Student student;
    private GradebookModel model;
    private MainController mainController;
    
    /**
     * Constructor for StudentController class.
     *
     * @param student the student user
     * @param model   the gradebook model
     */
    public StudentController(Student student, GradebookModel model) {
        this.student = student;
        this.model = model;
    }
    
    /**
     * Sets the main controller for navigation.
     *
     * @param mainController the main controller
     */
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
    
    /**
     * Logs out the current user.
     */
    public void logout() {
        if (mainController != null) {
            mainController.logout();
        }
    }
    
    /**
     * Gets the courses the student is enrolled in.
     *
     * @return the list of courses
     */
    public List<Course> getEnrolledCourses() {
        return student.getCourses();
    }
    
    /**
     * Gets the assignments for a specific course.
     *
     * @param course the course
     * @return the list of assignments
     */
    public List<Assignment> getAssignmentsForCourse(Course course) {
        return course.getAssignments();
    }
    
    /**
     * Calculates the student's GPA.
     *
     * @return the GPA
     */
    public double calculateGPA() {
        return student.calculateGPA();
    }
    
    /**
     * Calculates the average grade for a specific course.
     *
     * @param course the course
     * @return the average grade
     */
    public double calculateAverage(Course course) {
        return student.getAverageForCourse(course);
    }
    
    /**
     * Gets the grade for a specific assignment in a course.
     *
     * @param course     the course
     * @param assignment the assignment
     * @return the grade, or null if not graded
     */
    public Grade getGrade(Course course, Assignment assignment) {
        return student.getGrade(assignment);
    }
    
    /**
     * Gets the current student.
     *
     * @return the student
     */
    public Student getStudent() {
        return student;
    }
} 
