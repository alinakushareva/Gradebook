/**
 * Project Name: Gradebook
 * File Name: GradebookModel.java
 * Course: CSC 335 Spring 2025
 * 
 * Purpose: This model acts as the central data structure for managing students, teachers,
 * and courses in the gradebook application. It provides methods for interacting with
 * and retrieving data related to users and courses and supports the observer pattern.
 */
package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GradebookModel implements Subject {
    private final Map<String, Student> students;
    private final Map<String, Teacher> teachers;
    private final Map<String, Course> courses;
    private final List<Observer> observers = new ArrayList<>();

    /**
     * Constructor initializes internal data structures.
     */
    public GradebookModel() {
        this.students = new HashMap<>();
        this.teachers = new HashMap<>();
        this.courses = new HashMap<>();
    }

    /**
     * Adds a new student to the system.
     * @param s the student to add
     */
    public void addStudent(Student s) {
        students.put(s.getUsername(), s);
    }

    /**
     * Retrieves a student by their username.
     * @param username the student's username
     * @return the matching Student object or null
     */
    public Student getStudentByUsername(String username) {
        return students.get(username);
    }

    /**
     * Checks if a student with the given username exists.
     * @param username the student's username
     * @return true if the student exists
     */
    public boolean studentExists(String username) {
        return students.containsKey(username);
    }

    /**
     * Adds a new teacher to the system.
     * @param t the teacher to add
     */
    public void addTeacher(Teacher t) {
        teachers.put(t.getUsername(), t);
    }

    /**
     * Retrieves a teacher by their username.
     * @param username the teacher's username
     * @return the matching Teacher object or null
     */
    public Teacher getTeacherByUsername(String username) {
        return teachers.get(username);
    }

    /**
     * Checks if a teacher with the given username exists.
     * @param username the teacher's username
     * @return true if the teacher exists
     */
    public boolean teacherExists(String username) {
        return teachers.containsKey(username);
    }

    /**
     * Adds a new course to the system.
     * @param c the course to add
     */
    public void addCourse(Course c) {
        courses.put(c.getCourseName(), c);
        notifyObservers();  // Notify listeners 
    }

    /**
     * Retrieves a course by its name.
     * @param name the course name
     * @return the matching Course object or null
     */
    public Course getCourseByName(String name) {
        return courses.get(name);
    }

    /**
     * Returns all courses in the system.
     * @return list of all courses
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses.values());
    }
    
    /**
     * Calculates the median score for a specific assignment.
     * @param course the course the assignment belongs to
     * @param assignment the assignment to analyze
     * @return median score or 0.0 if no grades
     */
    public double calculateAssignmentMedian(Course course, Assignment assignment) {
        List<Double> scores = new ArrayList<>();

        // Collect all student scores
        for (Student student : course.getStudents()) {
            Grade grade = assignment.getGrade(student);
            if (grade != null) {
                scores.add(grade.getPointsReceived());
            }
        }

        if (scores.isEmpty()) return 0.0; // Handle no grades case

        scores.sort(Double::compareTo); // Sort scores for median

        int size = scores.size();
        if (size % 2 == 1) {
            return scores.get(size / 2);  // Odd count = middle value
        } else {
            return (scores.get(size / 2 - 1) + scores.get(size / 2)) / 2.0;
        }
    }
    
    /**
     * Returns a combined list of all users (students + teachers).
     * @return list of all users
     */
    public List<User> getAllUsers() {
        List<User> all = new ArrayList<>();
        all.addAll(students.values());
        all.addAll(teachers.values());
        return all;
    }

    /**
     * Registers an observer to the model.
     * @param o observer to add
     */
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    /**
     * Removes a previously registered observer.
     * @param o observer to remove
     */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /**
     * Notifies all registered observers of updates.
     */
    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}