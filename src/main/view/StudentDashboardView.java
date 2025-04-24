// StudentDashboardView.java (Updated to support GPA updates via Observer)
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

    public StudentDashboardView(Student student, GradebookModel model, MainController controller, MainView mainView) {
        this.student = student;
        this.model = model;
        this.content = new VBox(15);
        content.setPadding(new Insets(20));

        update(); // build initial UI
        model.addObserver(this); // register as observer

        // Logout
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> controller.logout());
        HBox footer = new HBox(logoutButton);
        footer.setAlignment(Pos.BOTTOM_RIGHT);
        footer.setPadding(new Insets(20, 0, 0, 0));

        BorderPane layout = new BorderPane();
        layout.setCenter(content);
        layout.setBottom(footer);

        this.scene = new Scene(layout, 600, 700);
    }

    @Override
    public void update() {
        content.getChildren().clear();

        Label welcome = new Label("Welcome, " + student.getFirstName() + " " + student.getLastName());
        welcome.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        List<Course> enrolledCourses = model.getAllCourses().stream()
            .filter(c -> c.getStudents().contains(student))
            .toList();

        List<Course> currentCourses = enrolledCourses.stream()
            .filter(c -> student.getFinalGrade(c) == null)
            .toList();

        List<Course> completedCourses = enrolledCourses.stream()
            .filter(c -> student.getFinalGrade(c) != null)
            .toList();

        // Current Courses
        Label currentCoursesLabel = new Label("Current Courses:");
        VBox currentCoursesBox = new VBox();
        if (currentCourses.isEmpty()) {
            currentCoursesBox.getChildren().add(new Label("No current courses."));
        } else {
            for (Course c : currentCourses) {
                currentCoursesBox.getChildren().add(new Label("• " + c.getCourseName()));
            }
        }

        // Completed Courses
        Label completedCoursesLabel = new Label("Completed Courses:");
        VBox completedCoursesBox = new VBox();
        if (completedCourses.isEmpty()) {
            completedCoursesBox.getChildren().add(new Label("No completed courses."));
        } else {
            for (Course c : completedCourses) {
                FinalGrade grade = student.getFinalGrade(c);
                completedCoursesBox.getChildren().add(new Label("• " + c.getCourseName() + " (Final Grade: " + grade + ")"));
            }
        }

        // Assignments
        Label assignmentsLabel = new Label("Assignments by Course:");
        VBox assignmentsBox = new VBox();
        if (enrolledCourses.isEmpty()) {
            assignmentsBox.getChildren().add(new Label("No assignments. No courses found."));
        } else {
            boolean anyAssignments = false;
            for (Course course : enrolledCourses) {
                if (course.getAssignments().isEmpty()) continue;
                anyAssignments = true;
                assignmentsBox.getChildren().add(new Label("• " + course.getCourseName() + ":"));
                for (Assignment a : course.getAssignments()) {
                    Grade g = a.getGrade(student);
                    String status = (g != null) ? "Graded" : "Ungraded";
                    double score = (g != null) ? g.getPointsReceived() : 0;
                    assignmentsBox.getChildren().add(new Label("   - " + a.getTitle() + ": " + score + " (" + status + ")"));
                }
            }
            if (!anyAssignments) {
                assignmentsBox.getChildren().add(new Label("No assignments found."));
            }
        }

        // Class Averages
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

        // GPA
        Label gpaLabel = new Label("GPA: " + String.format("%.2f", student.calculateGPA()));
        gpaLabel.setStyle("-fx-font-weight: bold");

        content.getChildren().addAll(
            welcome,
            currentCoursesLabel, currentCoursesBox,
            completedCoursesLabel, completedCoursesBox,
            assignmentsLabel, assignmentsBox,
            averagesLabel, avgBox,
            gpaLabel
        );
    }

    public Scene getScene() {
        return scene;
    }
}
