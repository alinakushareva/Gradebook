package org.gradebook.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import org.gradebook.controller.MainController;

/**
 * The login view for the gradebook application.
 * Provides UI for user authentication.
 */
public class LoginView {
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;
    private Label errorLabel;
    private MainController controller;
    private Scene scene;
    
    /**
     * Constructor for LoginView class.
     *
     * @param controller the main controller
     */
    public LoginView(MainController controller) {
        this.controller = controller;
        createLoginScene();
    }
    
    /**
     * Creates the login scene.
     */
    private void createLoginScene() {
        // Create the root layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        // Title
        Text sceneTitle = new Text("Gradebook Login");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);
        
        // Username label and field
        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 1);
        
        usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        grid.add(usernameField, 1, 1);
        
        // Password label and field
        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);
        
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        grid.add(passwordField, 1, 2);
        
        // Error label
        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        grid.add(errorLabel, 1, 3);
        
        // Login button
        loginButton = new Button("Login");
        HBox hbLoginBtn = new HBox(10);
        hbLoginBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbLoginBtn.getChildren().add(loginButton);
        grid.add(hbLoginBtn, 1, 4);
        
        // Register button
        registerButton = new Button("Register");
        HBox hbRegisterBtn = new HBox(10);
        hbRegisterBtn.setAlignment(Pos.BOTTOM_LEFT);
        hbRegisterBtn.getChildren().add(registerButton);
        grid.add(hbRegisterBtn, 0, 4);
        
        // Set button actions
        loginButton.setOnAction(e -> handleLogin());
        registerButton.setOnAction(e -> controller.showRegistrationScreen());
        
        // Enter key triggers login
        passwordField.setOnAction(e -> handleLogin());
        
        // Create the scene
        scene = new Scene(grid, 400, 300);
    }
    
    /**
     * Handles the login button action.
     */
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            displayError("Username and password cannot be empty");
            return;
        }
        
        // Attempt login
        boolean success = controller.authenticate(username, password);
        if (!success) {
            displayError("Invalid username or password");
        }
    }
    
    /**
     * Gets the scene for this view.
     *
     * @return the login scene
     */
    public Scene getScene() {
        return scene;
    }
    
    /**
     * Gets the username input.
     *
     * @return the username
     */
    public String getUsernameInput() {
        return usernameField.getText();
    }
    
    /**
     * Gets the password input.
     *
     * @return the password
     */
    public String getPasswordInput() {
        return passwordField.getText();
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
        passwordField.clear();
        errorLabel.setText("");
    }
} 