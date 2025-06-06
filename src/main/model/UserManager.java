/**
 * Project Name: Gradebook
 * File Name: UserManager.java
 * Course: CSC 335 Spring 2025
 * 
 * Purpose: Manages user-related operations including registration, login, 
 * and persistent storage. This class handles password security, 
 * user validation, and communication with file utilities.
 */
package model;

import util.FileUtil;
import util.SecurityUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;
    private final String userFilePath;
    private List<Course> courses = new ArrayList<>();

    
    /**
     * Constructor for UserManager class.
     *
     * @param userFilePath the file path for storing user data
     */
    public UserManager(String userFilePath) {
        this.userFilePath = userFilePath;
        this.users = new ArrayList<>();
        loadUsersFromFile();
    }
    
    /**
     * Registers a new user in the system.
     *
     * @param username  the username
     * @param firstName the first name
     * @param lastName  the last name
     * @param password  the plain text password (to be hashed)
     * @param role      the role ("student" or "teacher")
     * @return the created user, or null if registration failed
     */
    public void registerUser(String username, String firstName, String lastName, String password, String role) {
        // Validate username and password format
		if (!SecurityUtil.isValidUsername(username)) {
			throw new IllegalArgumentException("Username must be 4-20 alphanumeric characters");
		}
		if (!SecurityUtil.isValidPassword(password)) {
			throw new IllegalArgumentException("Password must have 8+ chars with: " + "1 digit, 1 lowercase, 1 uppercase, 1 special character");
		}
		if (findUserByUsername(username) != null) {
			throw new IllegalArgumentException("Username already exists");
		}
		
        // Hash password and instantiate correct user type
		String passwordHash = SecurityUtil.hashPassword(password);
		User newUser = switch (role.toLowerCase()) {
		
		case "student" -> new Student(firstName, lastName, username, passwordHash);
		case "teacher" -> new Teacher(username, firstName, lastName, passwordHash);default -> throw new IllegalArgumentException("Invalid role: " + role);
		};
		
        // Add user to list and save to file
		users.add(newUser);
		saveUsersToFile();
		}

    
    
    
    /**
     * Attempts to log in a user with the given credentials.
     *
     * @param username the username
     * @param password the plain text password
     * @return the user if login successful, null otherwise
     */
    public User login(String username, String password) {
        User user = findUserByUsername(username);
        if (user == null) return null;
        // Check password against stored hash
        return SecurityUtil.checkPassword(password, user.getPasswordHash()) ? user : null;
    }
    
    /**
     * Loads users from the file.
     */
    public void loadUsersFromFile() {
        try {
            users = FileUtil.loadUsers(userFilePath); // Parse users from file
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
            users = new ArrayList<>();
        }
    }
    
    /**
     * Saves users to the file.
     */
    public void saveUsersToFile() {
        FileUtil.saveUsers(users, userFilePath);
    }
    
    /**
     * Finds user by username (case-sensitive)
     * @return User object or null if not found
     */
    public User findUserByUsername(String username) {
        return users.stream()
            .filter(u -> u.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Gets all users in the system.
     *
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }  
    
    /**
     * Adds a course to internal course tracking.
     * 
     * @param course The course to add
     */
    public void addCourse(Course course) {
        courses.add(course);
    }
    

    /**
     * Retrieves a list of all stored courses.
     * 
     * @return List of courses
     */
    public List<Course> getAllCourses() {
        return new ArrayList<>(courses);
    }
} 