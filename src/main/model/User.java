/**
 * Project Name: Gradebook
 * File Name: User.java
 * Course: CSC 335 Spring 2025
 * Purpose: Abstract base class representing a user in the gradebook system. 
 *          Provides shared functionality and attributes for Student and Teacher subclasses.
 */

package model;

import java.util.ArrayList;
import java.util.List;

public abstract class User {
	private final String username;
    private final String firstName;
    private final String lastName;
    private final String passwordHash;
    private final String role; // "student" or "teacher"
    private List<Course> courses;

    /**
     * Constructor for User class.
     * @param username Unique identifier for the user
     * @param firstName User's first name
     * @param lastName User's last name
     * @param passwordHash Hashed password for security
     * @param role User's role ("student" or "teacher")
     */
    public User(String username, String firstName, String lastName, 
               String passwordHash, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.role = role;
        this.courses = new ArrayList<>();
    }

    /**
     * Gets the user's username.
     * @return String representing the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the user's full name (first + last name).
     * @return String representing the full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Gets the user's first name 
     * @return String representing the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the user's last name 
     * @return String representing the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the user's role.
     * @return String representing the role ("student" or "teacher")
     */
    public String getRole() {
        return role;
    }

    /**
     * Adds a course to the user's course list.
     * @param course Course object to be added
     */
    public void addCourse(Course course) {
        if (course != null && !courses.contains(course)) {
            courses.add(course);
        }
    }

    /**
     * Gets all courses associated with the user.
     * @return List of Course objects
     */
    public List<Course> getCourses() {
        return new ArrayList<>(courses); // Return copy for encapsulation
    }

    /**
     * Gets the hashed password for security verification.
     * @return String representing the hashed password
     */
    public String getPasswordHash() {
        return passwordHash;
    }
}