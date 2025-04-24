/**
 * Project Name: Gradebook
 * File Name: StudentDashboardView.java
 * Course: CSC 335 Spring 2025
 *
 * Purpose: Displays the student dashboard GUI.
 * It shows current/completed courses, assignments, class averages, and GPA.
 * Updates automatically using the Observer pattern when data changes.
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

import java.util.List;

public class StudentDashboardView implements model.Observer {

    private final Scene scene;
    private final Student student;
    private final GradebookModel model;
    private final VBox content;

    /**
     * Constructor for StudentDashboardView
     *
     * @param student the logged-in student
     * @param model the main gradebook model
     * @param controller the main controller
     * @param mainView reference to main view for navigation
     */
    public StudentDashboardView(Student student, GradebookModel model, MainController controller, MainView mainView) {
        this.student = student;
        this.model = model;
        
        // Create vertical layout container for the dashboard
        this.content = new VBox(15);
        content.setPadding(new Insets(20));

        update(); // build initial UI
        model.addObserver(this); // register as observer

        // Logout button setup
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> controller.logout());
        HBox footer = new HBox(logoutButton);
        footer.setAlignment(Pos.BOTTOM_RIGHT);
        footer.setPadding(new Insets(20, 0, 0, 0));

        // Use BorderPane to organize layout
        BorderPane layout = new BorderPane();
        layout.setCenter(content);
        layout.setBottom(footer);

        // Set the full scene
        this.scene = new Scene(layout, 600, 700);
    }

    /**
     * Rebuilds the student dashboard whenever the model is updated.
     */
    @Override
    public void update() {
        content.getChildren().clear(); // Clear old content

        // Greeting
        Label welcome = new Label("Welcome, " + student.getFirstName() + " " + student.getLastName());
        welcome.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Gather list of all courses the student is enrolled in
        List<Course> enrolledCourses = model.getAllCourses().stream()
            .filter(c -> c.getStudents().contains(student))
            .toList();

        // Separate into current (no final grade) and completed (with final grade)
        List<Course> currentCourses = enrolledCourses.stream()
            .filter(c -> student.getFinalGrade(c) == null)
            .toList();

        // Filter out courses where the student has received a final grade 
        List<Course> completedCourses = enrolledCourses.stream()
            .filter(c -> student.getFinalGrade(c) != null)
            .toList();

        // Current courses section
        Label currentCoursesLabel = new Label("Current Courses:");
        VBox currentCoursesBox = new VBox();
        // If the student has no current courses, show a placeholder message
        if (currentCourses.isEmpty()) {
            currentCoursesBox.getChildren().add(new Label("No current courses."));
        } else {
            // Otherwise, list each course by name
            for (Course c : currentCourses) {
                currentCoursesBox.getChildren().add(new Label("• " + c.getCourseName()));
            }
        }

        // Completed courses section with final grades
        Label completedCoursesLabel = new Label("Completed Courses:");
        VBox completedCoursesBox = new VBox();
        // If the student hasn't completed any courses, show a placeholder
        if (completedCourses.isEmpty()) {
            completedCoursesBox.getChildren().add(new Label("No completed courses."));
        } else {
            // Otherwise, show course name along with the final grade received
            for (Course c : completedCourses) {
                FinalGrade grade = student.getFinalGrade(c);
                completedCoursesBox.getChildren().add(new Label("• " + c.getCourseName() + " (Final Grade: " + grade + ")"));
            }
        }

        // Display all assignments and grading status
        Label assignmentsLabel = new Label("Assignments by Course:");
        VBox assignmentsBox = new VBox();
        if (enrolledCourses.isEmpty()) {
            assignmentsBox.getChildren().add(new Label("No assignments. No courses found."));
        } else {
            boolean anyAssignments = false;
            for (Course course : enrolledCourses) {
                // Skip courses with no assignments
                if (course.getAssignments().isEmpty()) continue;
                anyAssignments = true;
                assignmentsBox.getChildren().add(new Label("• " + course.getCourseName() + ":"));
                // For each assignment, show title, grade received, and whether it's graded
                for (Assignment a : course.getAssignments()) {
                    Grade g = a.getGrade(student);
                    String status = (g != null) ? "Graded" : "Ungraded";
                    double score = (g != null) ? g.getPointsReceived() : 0;
                    // Display line with assignment name, score (if any), and grading status
                    assignmentsBox.getChildren().add(new Label("   - " + a.getTitle() + ": " + score + " (" + status + ")"));
                }
            }
            // If none of the courses had assignments
            if (!anyAssignments) {
                assignmentsBox.getChildren().add(new Label("No assignments found."));
            }
        }

        // Show average scores per course
        Label averagesLabel = new Label("Class Averages:");
        VBox avgBox = new VBox();
        if (enrolledCourses.isEmpty()) {
            avgBox.getChildren().add(new Label("No courses to calculate averages."));
        } else {
            for (Course c : enrolledCourses) {
                double avg = c.calculateClassAverage();
                avgBox.getChildren().add(new Label("• " + c.getCourseName() + ": " + String.format("%.2f", avg) + "%"));
            }
        }

        // Show GPA at bottom
        Label gpaLabel = new Label("GPA: " + String.format("%.2f", student.calculateGPA()));
        gpaLabel.setStyle("-fx-font-weight: bold");

        // Assemble all into the main content layout
        content.getChildren().addAll(
            welcome,
            currentCoursesLabel, currentCoursesBox,
            completedCoursesLabel, completedCoursesBox,
            assignmentsLabel, assignmentsBox,
            averagesLabel, avgBox,
            gpaLabel
        );
    }

    /**
     * Gets the current scene representing this dashboard view.
     *
     * @return the JavaFX scene for this view
     */
    public Scene getScene() {
        return scene;
    }
}