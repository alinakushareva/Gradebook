/**
 * Project Name: Gradebook
 * File Name: ImportController.java
 * Course: CSC 335 Spring 2025
 * Purpose: Handles importing student data from external files into courses.
 *          Provides functionality to validate files, parse student usernames,
 *          and add students to courses under a teacher's management.
 */
package controller;

import model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ImportController {

    private final GradebookModel model;
    private final Teacher teacher;

    /**
     * Constructor for ImportController.
     * @param model the gradebook model
     * @param teacher the current teacher user
     */
    public ImportController(GradebookModel model, Teacher teacher) {
        this.model = model;
        this.teacher = teacher;
    }

    /**
     * Imports students from a file path (CSV or TXT).
     * @param filePath the path to the file
     * @return list of parsed Student objects
     */
    public List<Student> importStudents(String filePath) {
        List<Student> imported = new ArrayList<>();
        try {
            List<String> usernames = Files.readAllLines(Paths.get(filePath));
            for (String username : usernames) {
                // Look up each user and ensure it's a student
                User u = model.getStudentByUsername(username.trim());
                if (u instanceof Student s) {
                    imported.add(s); // Add valid student
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return imported;
    }



    /**
     * Validates that the file exists and is readable.
     * @param filePath the path to the file
     * @return true if valid, false otherwise
     */
    public boolean validateFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return true;  // If we can open it, it's valid
        } catch (IOException e) {
            return false; // File missing or unreadable
        }
    }

    /**
     * Adds a list of students to the course.
     * @param students the students to add
     * @param course the course they will be added to
     */
    public void addStudentsToCourse(List<Student> students, Course course) {
        for (Student student : students) {
            // Delegate enrollment to the teacher
            teacher.enrollStudent(course, student);
        }
    }

    /**
     * High-level method that validates the file, imports students, and enrolls them into a course.
     * @param filePath the file to import
     * @param course the course to populate
     */
    public void handleImport(String filePath, Course course) {
        if (validateFile(filePath)) {
            // If file is good, load and enroll students
            List<Student> students = importStudents(filePath);
            addStudentsToCourse(students, course);
        } else {
            // Handle bad file gracefully
            System.err.println("Invalid file: " + filePath);
        }
    }
}
