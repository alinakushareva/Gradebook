package controller;

import model.*;
import java.util.List;
import java.util.stream.Collectors;

public class TeacherController {
    private final Teacher teacher;
    public TeacherController(Teacher teacher, GradebookModel model) {
        this.teacher = teacher;
    }

    public List<Course> getTeachingCourses() {
        return teacher.getTeachingCourses();
    }

    public void addAssignment(Course course, Assignment assignment) {
        course.addAssignment(assignment);
    }

    public void assignGrade(Course course, Student student, Assignment assignment, double score) {
        assignment.assignGrade(student, score);
    }

    public void assignFinalGrade(Course course, Student student, FinalGrade grade) {
        course.assignFinalGrade(student, grade);
        student.assignFinalGrade(course, grade);
    }

    public List<Student> getStudentsForCourse(Course course) {
        return course.getStudents();
    }

    public List<Assignment> getAssignmentsForCourse(Course course) {
        return course.getAssignments();
    }

    public List<Assignment> getUngradedAssignments(Course course) {
        return course.getUngradedAssignments();
    }

    public double calculateClassAverage(Course course) {
        return course.calculateClassAverage();
    }

    public double calculateMedian(Course course) {
        List<Double> grades = course.getStudents().stream()
            .map(course::calculateStudentAverage)
            .sorted()
            .collect(Collectors.toList());

        int n = grades.size();
        if (n == 0) return 0.0;
        return (n % 2 == 1) ? grades.get(n / 2)
                           : (grades.get(n / 2 - 1) + grades.get(n / 2)) / 2.0;
    }

    public List<Student> sortStudentsByName(Course course) {
        return course.sortStudentsByName();
    }

    public List<Student> sortStudentsByAssignmentGrade(Course course, Assignment assignment) {
        return course.sortStudentsByAssignmentGrade(assignment);
    }

    // Optional placeholder
    public void groupStudents(Course course, int groupSize) {
        // This can be implemented if grouping logic is needed
    }
} 