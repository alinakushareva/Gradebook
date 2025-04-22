package org.gradebook.controller;

import org.gradebook.model.GradebookModel;
import org.gradebook.model.Student;
import org.gradebook.model.Teacher;
import org.gradebook.model.User;
import org.gradebook.view.LoginView;
import org.gradebook.view.MainView;
import org.gradebook.view.RegistrationView;
import org.gradebook.view.StudentDashboardView;
import org.gradebook.view.TeacherDashboardView;

/**
 * The main controller for the gradebook application.
 * Manages user authentication, navigation, and session data.
 */
public class MainController {
    private GradebookModel model;
    private MainView mainView;
    private User currentUser;
    private LoginView loginView;
    private RegistrationView registrationView;
    private StudentDashboardView studentDashboardView;
    private TeacherDashboardView teacherDashboardView;
    
    /**
     * Constructor for MainController class.
     *
     * @param model the gradebook model
     * @param view  the main view
     */
    public MainController(GradebookModel model, MainView view) {
        this.model = model;
        this.mainView = view;
        
        // Load all data from files
        loadAllData();
        
        // Initialize views
        initializeViews();
    }
    
    /**
     * Initializes all views and registers them with the main view.
     */
    private void initializeViews() {
        // Create the login view
        loginView = new LoginView(this);
        mainView.registerScene("login", loginView.getScene());
        
        // Create the registration view
        registrationView = new RegistrationView(this);
        mainView.registerScene("registration", registrationView.getScene());
    }
    
    /**
     * Shows the login screen.
     */
    public void showLoginScreen() {
        mainView.setScene("login");
    }
    
    /**
     * Shows the registration screen.
     */
    public void showRegistrationScreen() {
        mainView.setScene("registration");
    }
    
    /**
     * Registers a new user with the given information.
     *
     * @param username  the username
     * @param firstName the first name
     * @param lastName  the last name
     * @param password  the password
     * @param role      the role (student or teacher)
     * @return true if registration succeeds, false otherwise
     */
    public boolean registerUser(String username, String firstName, String lastName, String password, String role) {
        User user = model.registerUser(username, firstName, lastName, password, role);
        
        if (user != null) {
            registrationView.displayError("Registration successful. Please login.");
            return true;
        } else {
            registrationView.displayError("Registration failed. Username may already exist or inputs are invalid.");
            return false;
        }
    }
    
    /**
     * Attempts to authenticate a user with the given credentials.
     *
     * @param username the username
     * @param password the password
     * @return true if authentication succeeds, false otherwise
     */
    public boolean authenticate(String username, String password) {
        User user = model.login(username, password);
        
        if (user != null) {
            currentUser = user;
            switchToDashboard();
            return true;
        }
        
        return false;
    }
    
    /**
     * Switches to the appropriate dashboard based on the user's role.
     */
    public void switchToDashboard() {
        if (currentUser instanceof Student) {
            switchToStudentDashboard();
        } else if (currentUser instanceof Teacher) {
            switchToTeacherDashboard();
        }
    }
    
    /**
     * Switches to the student dashboard.
     */
    private void switchToStudentDashboard() {
        if (studentDashboardView == null) {
            StudentController studentController = new StudentController((Student) currentUser, model);
            studentController.setMainController(this);
            studentDashboardView = new StudentDashboardView(studentController);
            mainView.registerScene("studentDashboard", studentDashboardView.getScene());
        }
        
        mainView.setScene("studentDashboard");
    }
    
    /**
     * Switches to the teacher dashboard.
     */
    private void switchToTeacherDashboard() {
        if (teacherDashboardView == null) {
            TeacherController teacherController = new TeacherController((Teacher) currentUser, model);
            teacherController.setMainController(this);
            teacherDashboardView = new TeacherDashboardView(teacherController);
            mainView.registerScene("teacherDashboard", teacherDashboardView.getScene());
        }
        
        mainView.setScene("teacherDashboard");
    }
    
    /**
     * Logs out the current user.
     */
    public void logout() {
        // Save all data before logout
        saveAllData();
        
        // Clear the current user
        currentUser = null;
        
        // Show the login screen
        showLoginScreen();
    }
    
    /**
     * Exits the application.
     */
    public void exitApplication() {
        // Save all data before exit
        saveAllData();
        
        // Close the application
        javafx.application.Platform.exit();
    }
    
    /**
     * Loads all data from files.
     */
    private void loadAllData() {
        model.loadAllData();
    }
    
    /**
     * Saves all data to files.
     */
    private void saveAllData() {
        model.saveAllData();
    }
    
    /**
     * Gets the current user.
     *
     * @return the current user
     */
    public User getCurrentUser() {
        return currentUser;
    }
} 
