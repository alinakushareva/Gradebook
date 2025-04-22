package controller;

import model.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    Student student = new Student(parts[1], parts[2], parts[0], parts[3]);
                    students.add(student);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return students;
    }

    /**
     * Validates that the file exists and is readable.
     * @param filePath the path to the file
     * @return true if valid, false otherwise
     */
    public boolean validateFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Adds a list of students to the course.
     * @param students the students to add
     * @param course the course they will be added to
     */
    public void addStudentsToCourse(List<Student> students, Course course) {
        for (Student student : students) {
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
            List<Student> students = importStudents(filePath);
            addStudentsToCourse(students, course);
        } else {
            System.err.println("Invalid file: " + filePath);
        }
    }
}
