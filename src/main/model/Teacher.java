package main.model;

/*
 * Project Name: Gradebook
 * File Name: User.java
 * Course: CSC 335 Spring 2025
 * Purpose: Represents a teacher in the gradebook system.
 * Extends User and adds functionality for managing courses.
 */
package main.model;

import java.util.ArrayList;
import java.util.List;

public class Teacher extends User {
    private List<Course> teachingCourses;

    /*
     * Constructor for Teacher class.
     * @param username Unique identifier for the teacher
     * @param firstName Teacher's first name
     * @param lastName Teacher's last name
     * @param passwordHash Hashed password for security
     */
    public Teacher(String username, String firstName, String lastName, String passwordHash) {
        super(username, firstName, lastName, passwordHash, "teacher");
        this.teachingCourses = new ArrayList<>();
    }

    /*
     * Adds a course to the teacher's teaching list.
     * @param course Course to be added
     */
    public void addCourse(Course course) {
        if (course != null && !teachingCourses.contains(course)) {
            teachingCourses.add(course);
            super.addCourse(course); // Ensure it’s also added to the user's courses
        }
    }

    /*
     * Removes a course from the teacher's teaching list.
     * @param course Course to be removed
     */
    public void removeCourse(Course course) {
        teachingCourses.remove(course);
    }

    /*
     * Gets the list of courses the teacher is teaching.
     * @return List of Course objects
     */
    public List<Course> getTeachingCourses() {
        return new ArrayList<>(teachingCourses); // Return a copy for encapsulation
    }
}
