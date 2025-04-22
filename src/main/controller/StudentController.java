package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class StudentController {

    private final Student student;
    private final GradebookModel model;

    /**
     * Constructor for StudentController.
     * @param student the currently logged-in student
     * @param model the gradebook model instance
     */
    public StudentController(Student student, GradebookModel model) {
        this.student = student;
        this.model = model;
    }

    /**
     * Gets all courses the student is enrolled in.
     * @return list of enrolled courses
     */
    public List<Course> getEnrolledCourses() {
        return student.getCourses();
    }

    /**
     * Gets all assignments for a given course.
     * @param course the course to retrieve assignments from
     * @return list of assignments
     */
    public List<Assignment> getAssignmentsForCourse(Course course) {
        return course.getAssignments();
    }

    /**
     * Calculates the student's GPA.
     * @return GPA value
     */
    public double calculateGPA() {
        return student.calculateGPA();
    }

    /**
     * Calculates the student's average score in a given course.
     * @param course the course to calculate average for
     * @return average score as a percentage
     */
    public double calculateAverage(Course course) {
        return student.getAverageForCourse(course);
    }

    /**
     * Gets the grade the student received for a specific assignment.
     * @param course the course the assignment belongs to
     * @param assignment the assignment to look up
     * @return the Grade object for the assignment
     */
    public Grade getGrade(Course course, Assignment assignment) {
        return student.getGrade(assignment);
    }
}