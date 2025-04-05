package org.gradebook.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import org.gradebook.controller.MainController;

/**
 * The registration view for the gradebook application.
 * Provides UI for user registration.
 */
public class RegistrationView {
    private TextField usernameField;
    private TextField firstNameField;
    private TextField lastNameField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private ComboBox<String> roleComboBox;
    private Button registerButton;
    private Button backButton;
    private Label errorLabel;
    private MainController controller;
    private Scene scene;
    
    /**
     * Constructor for RegistrationView class.
     *
     * @param controller the main controller
     */
    public RegistrationView(MainController controller) {
        this.controller = controller;
        createRegistrationScene();
    }
    
    /**
     * Creates the registration scene.
     */
    private void createRegistrationScene() {
        // Create the root layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // Title
        Text sceneTitle = new Text("User Registration");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);
        
        // Rules description
        Text rulesText = new Text("Registration Rules:");
        rulesText.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        Text usernameRules = new Text("• Username: 3-20 characters, only letters, numbers and underscore");
        Text passwordRules = new Text("• Password: at least 8 characters, must include uppercase, lowercase, numbers and special characters");
        
        VBox rulesBox = new VBox(5);
        rulesBox.getChildren().addAll(rulesText, usernameRules, passwordRules);
        grid.add(rulesBox, 0, 1, 2, 1);
        
        // Username label and field
        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 2);
        
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        usernameField.setTooltip(new Tooltip("3-20 characters, only letters, numbers and underscore"));
        grid.add(usernameField, 1, 2);
        
        // First name label and field
        Label firstNameLabel = new Label("First Name:");
        grid.add(firstNameLabel, 0, 3);
        
        firstNameField = new TextField();
        firstNameField.setPromptText("Enter first name");
        grid.add(firstNameField, 1, 3);
        
        // Last name label and field
        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 4);
        
        lastNameField = new TextField();
        lastNameField.setPromptText("Enter last name");
        grid.add(lastNameField, 1, 4);
        
        // Password label and field
        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 5);
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setTooltip(new Tooltip("At least 8 characters, must include uppercase, lowercase, numbers and special characters"));
        grid.add(passwordField, 1, 5);
        
        // Confirm password label and field
        Label confirmPasswordLabel = new Label("Confirm Password:");
        grid.add(confirmPasswordLabel, 0, 6);
        
        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Re-enter password");
        grid.add(confirmPasswordField, 1, 6);
        
        // Role label and combo box
        Label roleLabel = new Label("Role:");
        grid.add(roleLabel, 0, 7);
        
        roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Student", "Teacher");
        roleComboBox.setValue("Student");
        grid.add(roleComboBox, 1, 7);
        
        // Error label
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        grid.add(errorLabel, 0, 8, 2, 1);
        
        // Register button
        registerButton = new Button("Register");
        HBox hbRegisterBtn = new HBox(10);
        hbRegisterBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbRegisterBtn.getChildren().add(registerButton);
        grid.add(hbRegisterBtn, 1, 9);
        
        // Back button
        backButton = new Button("Back to Login");
        HBox hbBackBtn = new HBox(10);
        hbBackBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbBackBtn.getChildren().add(backButton);
        grid.add(hbBackBtn, 0, 9);
        
        // Set button actions
        registerButton.setOnAction(e -> handleRegistration());
        backButton.setOnAction(e -> controller.showLoginScreen());
        
        // Create the scene
        scene = new Scene(grid, 500, 450);
    }
    
    /**
     * Handles the registration button action.
     */
    private void handleRegistration() {
        // Get input values
        String username = usernameField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String role = getRoleValue();
        
        // Validate input
        if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || 
            password.isEmpty() || confirmPassword.isEmpty()) {
            displayError("All fields are required");
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            displayError("Passwords do not match");
            return;
        }
        
        // Register the user
        boolean success = controller.registerUser(username, firstName, lastName, password, role);
        if (success) {
            // Clear fields and show login
            clearFields();
            controller.showLoginScreen();
        }
    }
    
    /**
     * Converts the selected role to the format expected by the controller.
     */
    private String getRoleValue() {
        String selectedRole = roleComboBox.getValue();
        if ("Student".equals(selectedRole)) {
            return "student";
        } else if ("Teacher".equals(selectedRole)) {
            return "teacher";
        }
        return "student"; // Default
    }
    
    /**
     * Gets the scene for this view.
     *
     * @return the registration scene
     */
    public Scene getScene() {
        return scene;
    }
    
    /**
     * Displays an error message.
     *
     * @param message the error message
     */
    public void displayError(String message) {
        errorLabel.setText(message);
    }
    
    /**
     * Clears all input fields.
     */
    public void clearFields() {
        usernameField.clear();
        firstNameField.clear();
        lastNameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
        roleComboBox.setValue("Student");
        errorLabel.setText("");
    }
} 