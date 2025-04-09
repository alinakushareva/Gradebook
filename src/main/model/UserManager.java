package org.gradebook.model;

import org.gradebook.util.FileUtil;
import org.gradebook.util.SecurityUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages user-related operations such as registration, login, and storage.
 */
public class UserManager {
    private Map<String, User> users;
    private final String userFilePath;
    
    /**
     * Constructor for UserManager class.
     *
     * @param userFilePath the file path for storing user data
     */
    public UserManager(String userFilePath) {
        this.userFilePath = userFilePath;
        this.users = new HashMap<>();
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
    public User registerUser(String username, String firstName, String lastName, String password, String role) {
        // Check if username already exists
        if (users.containsKey(username)) {
            return null;
        }
        
        // Validate username and password
        if (!SecurityUtil.isValidUsername(username) || !SecurityUtil.isValidPassword(password)) {
            return null;
        }
        
        // Hash the password
        String hashedPassword = SecurityUtil.hashPassword(password);
        
        // Create new user
        User newUser;
        if ("teacher".equalsIgnoreCase(role)) {
            newUser = new Teacher(username, firstName, lastName, hashedPassword);
        } else if ("student".equalsIgnoreCase(role)) {
            newUser = new Student(username, firstName, lastName, hashedPassword);
        } else {
            return null; // Invalid role
        }
        
        // Add user to collection
        users.put(username, newUser);
        
        // Save to file
        try {
            saveUsersToFile();
        } catch (IOException e) {
            System.err.println("Error saving user to file: " + e.getMessage());
        }
        
        return newUser;
    }
    
    /**
     * Registers a new user in the system.
     * This method is primarily for implementing the registration functionality.
     *
     * @param username     unique identifier for the user
     * @param firstName    user's first name
     * @param lastName     user's last name
     * @param password     plain text password (will be hashed)
     * @param role         user role (either "student" or "teacher")
     * @return the created user, or null if registration failed
     */
    public User registerNewUser(String username, String firstName, String lastName, String password, String role) {
        // Check if username already exists
        if (users.containsKey(username)) {
            return null;
        }
        
        // Validate username and password
        if (!SecurityUtil.isValidUsername(username) || !SecurityUtil.isValidPassword(password)) {
            return null;
        }
        
        // Hash the password
        String passwordHash = SecurityUtil.hashPassword(password);
        
        // Create new user
        User newUser;
        if ("student".equalsIgnoreCase(role)) {
            newUser = new Student(username, firstName, lastName, passwordHash);
        } else if ("teacher".equalsIgnoreCase(role)) {
            newUser = new Teacher(username, firstName, lastName, passwordHash);
        } else {
            return null; // Invalid role
        }
        
        // Add user to collection
        users.put(username, newUser);
        
        // Save to file
        try {
            saveUsersToFile();
        } catch (IOException e) {
            System.err.println("Error saving user to file: " + e.getMessage());
        }
        
        return newUser;
    }
    
    /**
     * Attempts to log in a user with the given credentials.
     *
     * @param username the username
     * @param password the plain text password
     * @return the user if login successful, null otherwise
     */
    public User login(String username, String password) {
        User user = users.get(username);
        
        if (user != null) {
            String passwordHash = user.getPasswordHash();
            if (SecurityUtil.validatePassword(password, passwordHash)) {
                return user;
            }
        }
        
        return null; // Login failed
    }
    
    /**
     * Loads users from the file.
     */
    public void loadUsersFromFile() {
        try {
            List<String> lines = FileUtil.readLines(userFilePath);
            users.clear();
            
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String username = parts[0];
                    String firstName = parts[1];
                    String lastName = parts[2];
                    String passwordHash = parts[3];
                    String role = parts[4];
                    
                    User user;
                    if ("student".equalsIgnoreCase(role)) {
                        user = new Student(username, firstName, lastName, passwordHash);
                    } else if ("teacher".equalsIgnoreCase(role)) {
                        user = new Teacher(username, firstName, lastName, passwordHash);
                    } else {
                        continue; // Skip invalid roles
                    }
                    
                    users.put(username, user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users from file: " + e.getMessage());
        }
    }
    
    /**
     * Saves users to the file.
     *
     * @throws IOException if an error occurs during file writing
     */
    public void saveUsersToFile() throws IOException {
        List<String> lines = new ArrayList<>();
        
        for (User user : users.values()) {
            String line = String.format("%s,%s,%s,%s,%s",
                    user.getUsername(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getPasswordHash(),
                    user.getRole());
            lines.add(line);
        }
        
        FileUtil.writeLines(userFilePath, lines);
    }
    
    /**
     * Finds a user by username.
     *
     * @param username the username to search for
     * @return the user, or null if not found
     */
    public User findUserByUsername(String username) {
        return users.get(username);
    }
    
    /**
     * Gets all users in the system.
     *
     * @return a list of all users
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    /**
     * Gets all students in the system.
     *
     * @return a list of all students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        for (User user : users.values()) {
            if (user instanceof Student) {
                students.add((Student) user);
            }
        }
        return students;
    }
    
    /**
     * Gets all teachers in the system.
     *
     * @return a list of all teachers
     */
    public List<Teacher> getAllTeachers() {
        List<Teacher> teachers = new ArrayList<>();
        for (User user : users.values()) {
            if (user instanceof Teacher) {
                teachers.add((Teacher) user);
            }
        }
        return teachers;
    }
} 
