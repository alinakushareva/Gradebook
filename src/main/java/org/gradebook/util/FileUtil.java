package org.gradebook.util;

import org.gradebook.model.Student;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for file operations like reading and writing data.
 */
public class FileUtil {
    
    /**
     * Reads all lines from a file.
     *
     * @param filePath the path to the file
     * @return a list of lines from the file
     * @throws IOException if an error occurs during reading
     */
    public static List<String> readLines(String filePath) throws IOException {
        List<String> lines = new ArrayList<>();
        Path path = Paths.get(filePath);
        
        // Create the file if it doesn't exist
        if (!Files.exists(path)) {
            try {
                // Ensure parent directory exists
                Path parent = path.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
                Files.createFile(path);
            } catch (IOException e) {
                // If unable to create file, just log error and return empty list
                System.err.println("Unable to create file: " + filePath + ", error: " + e.getMessage());
                return lines;
            }
            return lines;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        
        return lines;
    }
    
    /**
     * Writes lines to a file.
     *
     * @param filePath the path to the file
     * @param lines    the lines to write
     * @throws IOException if an error occurs during writing
     */
    public static void writeLines(String filePath, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);
        
        // Create directories if they don't exist
        Path parent = path.getParent();
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    /**
     * Parses a CSV file containing student information and returns a list of Student objects.
     * Expected format: username,firstName,lastName,password
     *
     * @param filePath the path to the CSV file
     * @return a list of Student objects
     * @throws IOException if an error occurs during reading
     */
    public static List<Student> parseStudentCSV(String filePath) throws IOException {
        List<Student> students = new ArrayList<>();
        List<String> lines = readLines(filePath);
        
        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("#")) {
                continue; // Skip empty lines and comments
            }
            
            String[] parts = line.split(",");
            if (parts.length >= 4) {
                String username = parts[0].trim();
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();
                String password = parts[3].trim();
                
                // Hash the password
                String passwordHash = SecurityUtil.hashPassword(password);
                
                // Create and add the student
                Student student = new Student(username, firstName, lastName, passwordHash);
                students.add(student);
            }
        }
        
        return students;
    }
    
    /**
     * Checks if a file exists.
     *
     * @param filePath the path to the file
     * @return true if the file exists, false otherwise
     */
    public static boolean fileExists(String filePath) {
        return Files.exists(Paths.get(filePath));
    }
    
    /**
     * Creates a directory if it doesn't exist.
     *
     * @param dirPath the path to the directory
     * @throws IOException if an error occurs during creation
     */
    public static void createDirectoryIfNotExists(String dirPath) throws IOException {
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
    }
} 