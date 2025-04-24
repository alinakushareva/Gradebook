/**
 * Project Name: Gradebook
 * File Name: LoginView.java
 * Course: CSC 335 Spring 2025
 *
 * Purpose:
 * This class builds the login user interface for the gradebook system.
 * It validates login credentials for both students and teachers and
 * redirects them to the appropriate dashboard using the MainView.
 *  * AI generated class!
 */
package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import model.*;

public class LoginView {
    private Scene scene;

    /**
     * Constructs the login view interface.
     *
     * @param model the GradebookModel for validating user credentials
     * @param mainView reference to the main view for switching scenes
     */
    public LoginView(GradebookModel model, MainView mainView) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // Username input field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        // Password input field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Login button and status message
        Button loginButton = new Button("Login");
        Label status = new Label();

        /**
         * Handles the login logic when the button is clicked.
         * Checks credentials and switches dashboard based on role.
         */
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Attempt login as student
            if (model.studentExists(username)) {
                Student student = model.getStudentByUsername(username);
                if (student.getPasswordHash().equals(password)) {
                	// Redirect to student dashboard
                    mainView.showStudentDashboard(student);
                    return;
                }
            }
            // Attempt login as teacher
            if (model.teacherExists(username)) {
                Teacher teacher = model.getTeacherByUsername(username);
                if (teacher.getPasswordHash().equals(password)) {
                	// Redirect to teacher dashboard
                    mainView.showTeacherDashboard(teacher);
                    return;
                }
            }
            status.setText("Invalid login."); // Invalid credentials message

        });
        // Add all components to layout
        root.getChildren().addAll(new Label("Login"), usernameField, passwordField, loginButton, status);
        scene = new Scene(root, 400, 300);
    }

    /**
     * Returns the scene for this view.
     *
     * @return the login UI scene
     */
    public Scene getScene() {
        return scene;
    }
}