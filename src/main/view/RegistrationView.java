/**
 * Project Name: Gradebook
 * File Name: RegistrationView.java
 * Course: CSC 335 Spring 2025
 *
 * Purpose: This view handles user registration within the Gradebook system.
 * It collects user input, validates the data, and updates the GradebookModel accordingly.
 * Supports both student and teacher registration.
 *  * AI generated class!
 */
package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import model.*;
import util.FileUtil;
import util.SecurityUtil;

public class RegistrationView {

    private Scene scene;

    /**
     * Constructor for RegistrationView
     * 
     * @param model the GradebookModel containing users and courses
     * @param mainView reference to the MainView for screen transitions
     */
    public RegistrationView(GradebookModel model, MainView mainView) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        // Input fields
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        // Password guidelines
        Label passwordHint = new Label("Password must be 8+ characters with uppercase, lowercase, digit, and special character.");
        passwordHint.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");

        // Role selection
        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton studentBtn = new RadioButton("Student");
        RadioButton teacherBtn = new RadioButton("Teacher");
        studentBtn.setToggleGroup(roleGroup);
        teacherBtn.setToggleGroup(roleGroup);
        studentBtn.setSelected(true);

        // Buttons
        Button registerButton = new Button("Register");
        Button goToLoginBtn = new Button("Go to Login");
        goToLoginBtn.setVisible(false);

        Label status = new Label(); // Feedback message

        // Handle registration logic
        registerButton.setOnAction(e -> {
            String first = firstNameField.getText().trim();
            String last = lastNameField.getText().trim();
            String user = usernameField.getText().trim();
            String pass = passwordField.getText().trim();

            // Validate input fields
            if (first.isEmpty() || last.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                status.setText("All fields are required.");
                return;
            }

            // Validate password strength
            if (!SecurityUtil.isValidPassword(pass)) {
                status.setText("Password does not meet complexity requirements.");
                return;
            }

            // Ensure username is unique
            if (model.studentExists(user) || model.teacherExists(user)) {
                status.setText("Username already exists.");
                return;
            }

            // Register based on selected role
            if (studentBtn.isSelected()) {
                Student s = new Student(first, last, user, pass);
                model.addStudent(s);
                FileUtil.saveUsers(model.getAllUsers(), "users.txt");
                status.setText("Student registered.");
            } else if (teacherBtn.isSelected()) {
                Teacher t = new Teacher(user, first, last, pass);
                model.addTeacher(t);
                FileUtil.saveUsers(model.getAllUsers(), "users.txt");
                status.setText("Teacher registered.");
            } else {
                status.setText("Please select a role.");
                return;
            }
 
            goToLoginBtn.setVisible(true); // Show login button after success
        });

        // Navigate to login view
        goToLoginBtn.setOnAction(e -> mainView.showLoginScreen());

        // Add all UI elements to layout
        root.getChildren().addAll(
            new Label("Register"),
            firstNameField, lastNameField,
            usernameField, passwordField, passwordHint,
            new Label("Role:"), studentBtn, teacherBtn,
            registerButton, goToLoginBtn, status
        );

        // Set the scene
        this.scene = new Scene(root, 420, 470);
    }

    /**
     * Returns the JavaFX Scene for rendering.
     *
     * @return the registration view scene
     */
    public Scene getScene() {
        return scene;
    }
}
