package controller;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class TeacherController {

    private final GradebookModel model;
    private final Teacher teacher;

    /**
     * Constructor for TeacherController.
     * @param teacher the currently logged-in teacher
     * @param model the gradebook model instance
     */
    public TeacherController(Teacher teacher, GradebookModel model) {
        this.teacher = teacher;
        this.model = model;
    }

    /**
     * Gets all courses the teacher is currently teaching.
     * @return list of courses
     */
    public List<Course> getTeachingCourses() {
        return teacher.getTeachingCourses();
    }

    /**
     * Adds an assignment to the specified course.
     * @param course the course to add the assignment to
     * @param assignment the assignment to be added
     */
    public void addAssignment(Course course, Assignment assignment) {
        course.addAssignment(assignment);
    }

    /**
     * Assigns a grade to a student for a specific assignment.
     * @param course the course containing the assignment
     * @param student the student receiving the grade
     * @param assignment the assignment being graded
     * @param score the score awarded
     */
    public void assignGrade(Course course, Student student, Assignment assignment, double score) {
        assignment.assignGrade(student, score);
    }

    /**
     * Assigns a final grade to a student in a course.
     * @param course the course for which the grade is assigned
     * @param student the student receiving the final grade
     * @param grade the final letter grade
     */
    public void assignFinalGrade(Course course, Student student, FinalGrade grade) {
        course.assignFinalGrade(student, grade);
    }

    /**
     * Retrieves the list of students enrolled in a course.
     * @param course the course to query
     * @return list of students
     */
    public List<Student> getStudentsForCourse(Course course) {
        return course.getStudents();
    }

    /**
     * Retrieves all assignments from a course.
     * @param course the course to query
     * @return list of assignments
     */
    public List<Assignment> getAssignmentsForCourse(Course course) {
        return course.getAssignments();
    }

    /**
     * Retrieves all ungraded assignments from a course.
     * @param course the course to query
     * @return list of ungraded assignments
     */
    public List<Assignment> getUngradedAssignments(Course course) {
        List<Assignment> ungraded = new ArrayList<>();
        for (Assignment assignment : course.getAssignments()) {
            if (!assignment.isFullyGraded(course.getStudents())) {
                ungraded.add(assignment);
            }
        }
        return ungraded;
    }

    /**
     * Calculates the average score of the entire class.
     * @param course the course to calculate for
     * @return the class average score
     */
    public double calculateClassAverage(Course course) {
        return course.calculateClassAverage();
    }

    /**
     * Calculates the median score in a course.
     * @param course the course to calculate for
     * @return the class median score
     */
    public double calculateMedian(Course course) {
        List<Double> scores = new ArrayList<>();
        for (Student student : course.getStudents()) {
            scores.add(course.calculateStudentAverage(student));
        }
        scores.sort(Double::compareTo);
        int size = scores.size();
        if (size == 0) return 0.0;
        if (size % 2 == 1) return scores.get(size / 2);
        return (scores.get(size / 2 - 1) + scores.get(size / 2)) / 2.0;
    }

    /**
     * Sorts students in a course alphabetically by last name then first name.
     * @param course the course to sort within
     * @return sorted list of students
     */
    public List<Student> sortStudentsByName(Course course) {
        return course.sortStudentsByName();
    }

    /**
     * Sorts students by their grade on a specific assignment.
     * @param course the course to sort within
     * @param assignment the assignment to sort grades by
     * @return sorted list of students by grade
     */
    public List<Student> sortStudentsByAssignmentGrade(Course course, Assignment assignment) {
        return course.sortStudentsByAssignmentGrade(assignment);
    }

    /**
     * Groups students into teams of a given size and prints group info.
     * @param course the course to group within
     * @param groupSize the desired number of students per group
     */
    public void groupStudents(Course course, int groupSize) {
        List<Student> students = new ArrayList<>(course.getStudents());
        for (int i = 0; i < students.size(); i++) {
            System.out.println("Group " + (i / groupSize + 1) + ": " + students.get(i).getFullName());
        }
    }
}
