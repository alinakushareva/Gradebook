package org.gradebook.controller;

import org.gradebook.model.Assignment;
import org.gradebook.model.Category;
import org.gradebook.model.Course;
import org.gradebook.model.FinalGrade;
import org.gradebook.model.GradebookModel;
import org.gradebook.model.Student;
import org.gradebook.model.Teacher;
import org.gradebook.model.Grade;

import java.util.List;
import java.util.ArrayList;

/**
 * Controller for teacher-related operations.
 * Handles teacher view logic and interactions with the model.
 */
public class TeacherController {
    private Teacher teacher;
    private GradebookModel model;
    private MainController mainController;
    
    /**
     * Constructor for TeacherController class.
     *
     * @param teacher the teacher user
     * @param model   the gradebook model
     */
    public TeacherController(Teacher teacher, GradebookModel model) {
        this.teacher = teacher;
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
     * Gets the courses the teacher is teaching.
     *
     * @return the list of courses
     */
    public List<Course> getTeachingCourses() {
        return teacher.getTeachingCourses();
    }
    
    /**
     * Adds an assignment to a course.
     *
     * @param course     the course
     * @param title      the assignment title
     * @param maxPoints  the maximum points
     * @param category   the category (can be null)
     * @return the created assignment
     */
    public Assignment addAssignment(Course course, String title, double maxPoints, String category) {
        if (course == null || title == null || title.isEmpty() || maxPoints <= 0) {
            return null;
        }
        
        Assignment assignment = new Assignment(title, maxPoints, category);
        course.addAssignment(assignment);
        
        // Save data after adding assignment
        model.saveAllData();
        
        return assignment;
    }
    
    /**
     * Assigns a grade to a student for an assignment.
     *
     * @param course     the course
     * @param student    the student
     * @param assignment the assignment
     * @param score      the score
     */
    public void assignGrade(Course course, Student student, Assignment assignment, double score) {
        assignment.assignGrade(student, score);
        // Save data after assigning grade
        model.saveAllData();
    }
    
    /**
     * Assigns a final grade to a student for a course.
     *
     * @param course  the course
     * @param student the student
     * @param grade   the final grade
     */
    public void assignFinalGrade(Course course, Student student, FinalGrade grade) {
        course.assignFinalGrade(student, grade);
        // Save data after assigning final grade
        model.saveAllData();
    }
    
    /**
     * Gets the students enrolled in a course.
     *
     * @param course the course
     * @return the list of students
     */
    public List<Student> getStudentsForCourse(Course course) {
        return course.getStudents();
    }
    
    /**
     * Gets the assignments for a course.
     *
     * @param course the course
     * @return the list of assignments
     */
    public List<Assignment> getAssignmentsForCourse(Course course) {
        return course.getAssignments();
    }
    
    /**
     * Gets the ungraded assignments for a course.
     *
     * @param course the course
     * @return the list of ungraded assignments
     */
    public List<Assignment> getUngradedAssignments(Course course) {
        return course.getUngradedAssignments();
    }
    
    /**
     * Calculates the class average for a course.
     *
     * @param course the course
     * @return the class average
     */
    public double calculateClassAverage(Course course) {
        return course.calculateClassAverage();
    }
    
    /**
     * Calculates the median grade for a specific assignment.
     *
     * @param course     the course
     * @param assignment the assignment
     * @return the median grade
     */
    public double calculateMedian(Course course, Assignment assignment) {
        return course.calculateMedianForAssignment(assignment);
    }
    
    /**
     * Sorts students by name for a course.
     *
     * @param course the course
     * @return the sorted list of students
     */
    public List<Student> sortStudentsByName(Course course) {
        return course.sortStudentsByName();
    }
    
    /**
     * Sorts students in a course by their grades on a specific assignment.
     * Students with higher grades appear first in the returned list.
     *
     * @param course The course containing the students and assignment
     * @param assignment The assignment to use for sorting
     * @return A list of students sorted by their grades on the assignment (descending)
     */
    public List<Student> sortStudentsByAssignmentGrade(Course course, Assignment assignment) {
        if (course == null || assignment == null) {
            throw new IllegalArgumentException("Course and assignment cannot be null");
        }

        List<Student> students = new ArrayList<>(course.getStudents());
        
        // Sort the students by their grade on the given assignment
        students.sort((s1, s2) -> {
            Grade grade1 = assignment.getGrade(s1);
            Grade grade2 = assignment.getGrade(s2);
            
            // Handle case where one or both students don't have a grade
            if (grade1 == null && grade2 == null) {
                return 0;
            } else if (grade1 == null) {
                return 1; // Students without grades appear last
            } else if (grade2 == null) {
                return -1;
            }
            
            // Compare by percentage (descending order)
            return Double.compare(grade2.getPercentage(), grade1.getPercentage());
        });
        
        return students;
    }
    
    /**
     * Creates groups of students for a course.
     *
     * @param course    the course
     * @param groupSize the desired size of each group
     * @return a list of student groups
     */
    public List<List<Student>> groupStudents(Course course, int groupSize) {
        int numberOfStudents = course.getStudents().size();
        int numberOfGroups = (int) Math.ceil((double) numberOfStudents / groupSize);
        
        List<List<Student>> groups = new java.util.ArrayList<>(numberOfGroups);
        for (int i = 0; i < numberOfGroups; i++) {
            groups.add(new java.util.ArrayList<>());
        }
        
        course.groupStudents(groups);
        return groups;
    }
    
    /**
     * Creates a new course and assigns the teacher to it.
     *
     * @param courseName the course name
     * @return the created course
     */
    public Course createCourse(String courseName) {
        Course course = model.createCourse(courseName, teacher);
        
        // Save data after creating course
        if (course != null) {
            model.saveAllData();
        }
        
        return course;
    }
    
    /**
     * Gets the current teacher.
     *
     * @return the teacher
     */
    public Teacher getTeacher() {
        return teacher;
    }
    
    /**
     * Adds a student to a course by username.
     *
     * @param username the student's username
     * @param course   the course to add the student to
     * @return true if successful, false if student not found or already enrolled
     */
    public boolean addStudentToCourse(String username, Course course) {
        Student student = model.getStudentByUsername(username);
        
        if (student == null) {
            return false; // Student not found
        }
        
        if (course.getStudents().contains(student)) {
            return false; // Student already enrolled
        }
        
        course.addStudent(student);
        
        // Save data after adding student to course
        model.saveAllData();
        
        return true;
    }
    
    /**
     * Removes a student from a course.
     *
     * @param student the student to remove
     * @param course  the course to remove the student from
     * @return true if successful, false otherwise
     */
    public boolean removeStudentFromCourse(Student student, Course course) {
        if (student == null || course == null) {
            return false;
        }
        
        if (!course.getStudents().contains(student)) {
            return false; // Student is not in the course
        }
        
        course.removeStudent(student);
        
        // Save data after removing student from course
        model.saveAllData();
        
        return true;
    }
    
    /**
     * Updates an existing assignment in a course.
     *
     * @param course         the course containing the assignment
     * @param assignment     the assignment to update
     * @param title          the new title
     * @param maxPoints      the new maximum points
     * @param category       the new category (can be null)
     * @return true if successful, false otherwise
     */
    public boolean updateAssignment(Course course, Assignment assignment, String title, 
                                   double maxPoints, String category) {
        if (course == null || assignment == null || title == null || title.isEmpty() || maxPoints <= 0) {
            return false;
        }
        
        // Update the assignment properties
        assignment.setTitle(title);
        assignment.setMaxPoints(maxPoints);
        assignment.setCategory(category);
        
        // Save data after updating assignment
        model.saveAllData();
        
        return true;
    }
} 
