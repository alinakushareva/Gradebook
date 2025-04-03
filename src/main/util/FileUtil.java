/**
 * Project Name: Gradebook
 * File Name: FileUtil.java
 * Course: CSC 335 Spring 2025
Purpose: Provides file I/O operations for the Gradebook system including user data persistence,
 *          student imports, and basic file operations. Handles all file-related tasks using
 *          standardized formats for data storage.
 */
package util;

import model.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class FileUtil {

	/**
     * Saves a list of users to a text file in CSV format
     * @param users List of User objects to save
     * @param filePath Destination file path
     * @throws IOException if file operations fail
     */
    public static void saveUsers(List<User> users, String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        // Convert each user to CSV format: username,fullName,passwordHash,role
        for (User user : users) {
            lines.add(String.join(",",
                user.getUsername(),
                user.getFullName(),
                user.getPasswordHash(),
                user.getRole().toLowerCase()
            ));
        }
        Files.write(Paths.get(filePath), lines);
    }

    /**
     * Loads users from a text file and creates User objects
     * @param filePath Source file path
     * @return List of reconstructed User objects
     * @throws IOException if file operations fail
     */
    public static List<User> loadUsers(String filePath) throws IOException {
        List<User> users = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        
        // Process each line in the file
        for (String line : lines) {
            if (line.trim().isEmpty()) continue; // Skip empty lines
            
            String[] parts = line.split(",");
            if (parts.length != 4) continue; // Skip empty lines

            // Extract and clean fields
            String username = parts[0].trim();
            String fullName = parts[1].trim();
            String passwordHash = parts[2].trim();
            String role = parts[3].trim().toLowerCase();

            // Split full name into first and last
            String[] nameParts = fullName.split(" ");
            String firstName = nameParts[0];
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            // Creating appropriate user type
            if (role.equals("student")) {
                users.add(new Student(firstName, lastName, username, passwordHash));
            } else if (role.equals("teacher")) {
                users.add(new Teacher(firstName, lastName, username, passwordHash));
            }
        }
        return users;
    }

    /**
     * Imports students from a CSV file
     * @param filePath Path to CSV file (format: firstName,lastName,username,password)
     * @return List of created Student objects
     * @throws IOException if file operations fail
     */
    public static List<Student> parseStudentCSV(String filePath) throws IOException {
        List<Student> students = new ArrayList<>();
        for (String line : Files.readAllLines(Paths.get(filePath))) {
            if (line.trim().isEmpty()) continue; // Skip empty lines
            
            String[] parts = line.split(",");
            if (parts.length != 4) continue; // Skip incomplete entries

            // Create student with hashed password
            students.add(new Student(
                parts[0].trim(),
                parts[1].trim(),
                parts[2].trim(),
                SecurityUtil.hashPassword(parts[3].trim())
            ));
        }
        return students;
    }

    /**
     * Reads all lines from a text file
     * @param filePath File to read
     * @return List of lines
     * @throws IOException if file operations fail
     */
    public static List<String> readLines(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
    
    /**
     * Writes lines to a text file
     * @param filePath Destination file
     * @param lines Lines to write
     * @throws IOException if file operations fail
     */
    public static void writeLines(String filePath, List<String> lines) throws IOException {
        Files.write(Paths.get(filePath), lines);
    }
}