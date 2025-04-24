package controller;

import model.*;
import view.MainView;

/**
 * Handles authentication and dashboard switching.
 */
public class MainController {

    private final GradebookModel model;
    private final MainView mainView;
    private User currentUser;

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
        if (model.studentExists(username)) {
            Student student = model.getStudentByUsername(username);
            if (student.getPasswordHash().equals(password)) {
                currentUser = student;
                mainView.showStudentDashboard(student);
                return true;
            }
        }

        if (model.teacherExists(username)) {
            Teacher teacher = model.getTeacherByUsername(username);
            if (teacher.getPasswordHash().equals(password)) {
                currentUser = teacher;
                mainView.showTeacherDashboard(teacher);
                return true;
            }
        }

        return false;
    }

    /**
     * Logs out the current user and returns to the home screen.
     */
    public void logout() {
        currentUser = null;
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
