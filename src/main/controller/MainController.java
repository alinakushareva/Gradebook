/**
 * Project Name: Gradebook
 * File Name: MainController.java
 * Course: CSC 335 Spring 2025
 * Purpose: Handles user authentication, login routing, and logout.
 *          Connects the GradebookModel to the appropriate student or teacher dashboard in the view.
 */
package controller;

import model.*;
import view.MainView;


public class MainController {

    private final GradebookModel model;
    private final MainView mainView;
    private User currentUser;
    
    /**
     * Constructs a MainController with model and view references.
     * @param model the gradebook model to interact with
     * @param mainView the main UI entry point
     */
    public MainController(GradebookModel model, MainView mainView) {
        this.model = model;
        this.mainView = mainView;
    }

    /**
     * Attempts to log in a user and routes them to the appropriate dashboard.
     * 
     * @param username The entered username
     * @param password The entered password
     * @return true if login is successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        // Check if the user is a student
    	if (model.studentExists(username)) {
            Student student = model.getStudentByUsername(username);
            if (student.getPasswordHash().equals(password)) {
                currentUser = student;
                // Load student dashboard
                mainView.showStudentDashboard(student);
                return true;
            }
        }

        // Check if the user is a teacher
        if (model.teacherExists(username)) {
            Teacher teacher = model.getTeacherByUsername(username);
            if (teacher.getPasswordHash().equals(password)) {
                currentUser = teacher;
                // Load teacher dashboard
                mainView.showTeacherDashboard(teacher);
                return true;
            }
        }
        // Invalid credentials
        return false;
    }

    /**
     * Logs out the current user and returns to the home screen.
     */
    public void logout() {
        currentUser = null; // Clear session
        mainView.showHomeScreen();
    }

    /**
     * Gets the currently logged-in user (if any).
     * 
     * @return User object or null
     */
    public User getCurrentUser() {
        return currentUser;
    }
}
