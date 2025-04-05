package org.gradebook.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a teacher in the gradebook system.
 * Extends the User class and adds functionality for managing courses.
 */
public class Teacher extends User {
    private List<Course> teachingCourses;
    
    /**
     * Constructor for Teacher class.
     *
     * @param username     unique identifier for the teacher
     * @param firstName    teacher's first name
     * @param lastName     teacher's last name
     * @param passwordHash encrypted password
     */
    public Teacher(String username, String firstName, String lastName, String passwordHash) {
        super(username, firstName, lastName, passwordHash, "teacher");
        this.teachingCourses = new ArrayList<>();
    }
    
    /**
     * Adds a course to the list of courses taught by this teacher.
     *
     * @param course the course to add
     */
    @Override
    public void addCourse(Course course) {
        super.addCourse(course);
        if (!teachingCourses.contains(course)) {
            teachingCourses.add(course);
        }
    }
    
    /**
     * Removes a course from the list of courses taught by this teacher.
     *
     * @param course the course to remove
     */
    public void removeCourse(Course course) {
        teachingCourses.remove(course);
        getCourses().remove(course);
    }
    
    /**
     * Gets the list of courses taught by this teacher.
     *
     * @return the list of teaching courses
     */
    public List<Course> getTeachingCourses() {
        return new ArrayList<>(teachingCourses);
    }
    
    @Override
    public String toString() {
        return "Teacher{" +
                "username='" + getUsername() + '\'' +
                ", name='" + getFullName() + '\'' +
                ", teachingCourses=" + teachingCourses.size() +
                '}';
    }
} 