package view;

import controller.TeacherController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import model.*;

import java.util.List;

public class TeacherDashboardView {
    private final ListView<Course> courseList = new ListView<>();
    private final Button addAssignmentButton = new Button("Add Assignment");
    private final Button importStudentsButton = new Button("Import Students");
    private final Button assignGradeButton = new Button("Assign Grade");
    private final Button viewUngradedButton = new Button("View Ungraded");
    private final Button calculateStatsButton = new Button("Class Stats");
    private final TableView<Student> studentTable = new TableView<>();
    private final Scene scene;

    public TeacherDashboardView(TeacherController controller) {
        Label title = new Label("Teacher Dashboard");
        title.setFont(new Font(20));

        VBox leftPane = new VBox(10, title, courseList, addAssignmentButton, importStudentsButton,
                assignGradeButton, viewUngradedButton, calculateStatsButton);
        leftPane.setPadding(new Insets(10));
        leftPane.setPrefWidth(300);

        VBox rightPane = new VBox(new Label("Students"), studentTable);
        rightPane.setPadding(new Insets(10));
        studentTable.setPrefHeight(400);

        HBox root = new HBox(leftPane, rightPane);
        scene = new Scene(root, 1000, 600);
    }

    public Scene getScene() {
        return scene;
    }

    public void updateCourseList(List<Course> courses) {
        courseList.getItems().setAll(courses);
    }

    public void updateStudentTable(List<Student> students) {
        studentTable.getItems().setAll(students);
    }

    public void showUngradedAssignments(List<Assignment> ungraded) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ungraded Assignments");
        alert.setHeaderText("Assignments needing grading:");
        StringBuilder sb = new StringBuilder();
        for (Assignment a : ungraded) {
            sb.append(a.getTitle()).append("\n");
        }
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }

    public void showClassStats(double avg, double median) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Class Stats");
        alert.setHeaderText("Class Average and Median");
        alert.setContentText("Average: " + avg + "%\nMedian: " + median + "%");
        alert.showAndWait();
    }
}
