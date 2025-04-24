/**
 * Project Name: Gradebook
 * File Name: MainView.java
 * Course: CSC 335 Spring 2025
 *
 * Purpose:
 * This class handles all view transitions within the Gradebook JavaFX application.
 * It manages navigation between the home screen, login, registration, and user dashboards
 * for students and teachers.
 */
package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;

import model.*;
import controller.*;

public class MainView {

    private Stage stage;
    private GradebookModel model;
    private MainController mainController;

    /**
     * Constructs the main view manager and displays the home screen.
     * 
     * @param stage the main stage of the application
     * @param model the gradebook data model
     */
    public MainView(Stage stage, GradebookModel model) {
        this.stage = stage;
        this.model = model;
        this.mainController = new MainController(model, this);
        showHomeScreen();
    }

    /**
     * Displays the home screen with options to login or register.
     */
    public void showHomeScreen() {
        // Create a vertical layout with spacing of 15 pixels
        VBox root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-alignment: center");

        // Create and style the title text
        Text title = new Text("Gradebook System");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 26));

        // Create the login button
        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(150);
        loginBtn.setOnAction(e -> showLoginScreen());

        // Create the register button
        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(150);
        registerBtn.setOnAction(e -> showRegistrationScreen());

        // Add all UI elements to the layout
        root.getChildren().addAll(title, loginBtn, registerBtn);

        // Set up the scene and show it on the stage
        stage.setTitle("Gradebook - Home");
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }

    /**
     * Switches to the login screen.
     */
    public void showLoginScreen() {
        LoginView loginView = new LoginView(model, this);
        stage.setScene(loginView.getScene());
    }

    /**
     * Switches to the registration screen.
     */
    public void showRegistrationScreen() {
        RegistrationView registrationView = new RegistrationView(model, this);
        stage.setScene(registrationView.getScene());
    }

    /**
     * Displays the student dashboard after a successful login.
     * 
     * @param student the logged-in student
     */
    public void showStudentDashboard(Student student) {
        StudentDashboardView dashboardView = new StudentDashboardView(student, model, mainController, this);
        stage.setScene(dashboardView.getScene());
    }

    /**
     * Displays the teacher dashboard after a successful login.
     * 
     * @param teacher the logged-in teacher
     */
    public void showTeacherDashboard(Teacher teacher) {
        TeacherDashboardView dashboardView = new TeacherDashboardView(teacher, model, mainController, this);
        stage.setScene(dashboardView.getScene());
    }

    /**
     * Allows external components to set a custom scene on the stage.
     * 
     * @param scene the new scene to display
     */
    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    /**
     * Retrieves the JavaFX stage instance.
     * 
     * @return the current stage
     */
    public Stage getStage() {
        return stage;
    }
}
