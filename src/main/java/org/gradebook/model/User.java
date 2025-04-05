package org.gradebook.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract class representing a user in the system.
 * This is the parent class for Student and Teacher.
 */
public abstract class User {
    private String username;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private List<Course> courses;
    private String role;
    
    /**
     * Constructor for User class.
     *
     * @param username     unique identifier for the user
     * @param firstName    user's first name
     * @param lastName     user's last name
     * @param passwordHash encrypted password
     * @param role         user role (either "student" or "teacher")
     */
    public User(String username, String firstName, String lastName, String passwordHash, String role) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.role = role;
        this.courses = new ArrayList<>();
    }
    
    /**
     * Gets the username of the user.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Gets the full name of the user (first name + last name).
     *
     * @return the full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    /**
     * Gets the first name of the user.
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Gets the last name of the user.
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Gets the password hash of the user.
     *
     * @return the password hash
     */
    public String getPasswordHash() {
        return passwordHash;
    }
    
    /**
     * Gets the role of the user.
     *
     * @return the role
     */
    public String getRole() {
        return role;
    }
    
    /**
     * Adds a course to the user's course list.
     *
     * @param course the course to add
     */
    public void addCourse(Course course) {
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }
    
    /**
     * Gets the list of courses the user is associated with.
     *
     * @return the list of courses
     */
    public List<Course> getCourses() {
        return new ArrayList<>(courses);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
} 