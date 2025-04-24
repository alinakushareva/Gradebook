/**
 * Project Name: Gradebook
 * File Name: TeacherDashboardView.java
 * Course: CSC 335 Spring 2025
 *
 * Purpose:
 * This view displays the main dashboard for a logged-in teacher,
 * providing navigation buttons to manage courses, students, assignments, and grades.
 * It links to the corresponding views and handles logout functionality.
 * AI generated class!
 */
package view;

import controller.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;

public class TeacherDashboardView {
    private final Scene scene;

    /**
     * Constructor for TeacherDashboardView.
     *
     * @param teacher the logged-in teacher
     * @param model the gradebook model
     * @param controller the main controller
     * @param mainView the main UI handler for changing scenes
     */
    public TeacherDashboardView(Teacher teacher, GradebookModel model, MainController controller, MainView mainView) {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));

        // Welcome message
        Label welcome = new Label("Welcome, " + teacher.getFirstName() + " " + teacher.getLastName());
        welcome.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Instruction label
        Label instruction = new Label("Choose a section to manage your gradebook:");
        instruction.setStyle("-fx-font-size: 14px;");

        // Create navigation buttons for managing different areas
        Button coursesButton = new Button("📚 Manage Courses");
        coursesButton.setPrefWidth(200);
        coursesButton.setOnAction(e -> {
            // Navigate to CourseDetailView
            CourseDetailView view = new CourseDetailView(teacher, model, controller, mainView);
            mainView.setScene(view.getScene());
        });

        Button studentsButton = new Button("👥 Manage Students");
        studentsButton.setPrefWidth(200);
        studentsButton.setOnAction(e -> {
            // Navigate to StudentManagementView
            StudentManagementView view = new StudentManagementView(teacher, model, controller, mainView);
            mainView.setScene(view.getScene());
        });

        Button assignmentsButton = new Button("📘 Manage Assignments");
        assignmentsButton.setPrefWidth(200);
        assignmentsButton.setOnAction(e -> {
            // Navigate to AssignmentManagementView
            AssignmentManagementView view = new AssignmentManagementView(teacher, model, controller, mainView);
            mainView.setScene(view.getScene());
        });

        Button gradesButton = new Button("📝 Manage Grades");
        gradesButton.setPrefWidth(200);
        gradesButton.setOnAction(e -> {
            // Navigate to GradeManagementView
            GradeManagementView view = new GradeManagementView(teacher, model, controller, mainView);
            mainView.setScene(view.getScene());
        });

        // Group buttons in a vertical layou
        VBox navButtons = new VBox(15, coursesButton, studentsButton, assignmentsButton, gradesButton);
        navButtons.setAlignment(Pos.CENTER);

        // Logout button placed in the bottom-right corner
        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> controller.logout());
        HBox footer = new HBox(logoutBtn);
        footer.setAlignment(Pos.BOTTOM_RIGHT);

        // Assemble all components
        mainContent.getChildren().addAll(welcome, instruction, navButtons);

        BorderPane layout = new BorderPane();
        layout.setCenter(mainContent);
        layout.setBottom(footer);
        layout.setPadding(new Insets(20));

        this.scene = new Scene(layout, 600, 500);
    }

    /**
     * Returns the current dashboard scene for teacher.
     * 
     * @return the JavaFX scene
     */
    public Scene getScene() {
        return scene;
    }
}