package view;

import controller.TeacherController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import model.*;

import java.util.List;

public class CourseDetailView {
    private final Label courseNameLabel = new Label();
    private final TableView<Assignment> assignmentTable = new TableView<>();
    private final TableView<Student> studentTable = new TableView<>();
    private final Button sortByNameButton = new Button("Sort by Name");
    private final Button sortByGradeButton = new Button("Sort by Grade");
    private final Button assignFinalGradesButton = new Button("Assign Final Grades");
    private final Scene scene;

    public CourseDetailView(Course course, TeacherController controller) {
        courseNameLabel.setText("Course: " + course.getCourseName());
        courseNameLabel.setFont(new Font(18));

        HBox buttonBar = new HBox(10, sortByNameButton, sortByGradeButton, assignFinalGradesButton);
        VBox layout = new VBox(10, courseNameLabel, assignmentTable, studentTable, buttonBar);
        layout.setPadding(new Insets(15));
        scene = new Scene(layout, 900, 600);
    }

    public Scene getScene() {
        return scene;
    }

    public void updateAssignments(List<Assignment> assignments) {
        assignmentTable.getItems().setAll(assignments);
    }

    public void updateStudents(List<Student> students) {
        studentTable.getItems().setAll(students);
    }

    public void displayCourseInfo(Course course) {
        courseNameLabel.setText("Course: " + course.getCourseName());
        updateAssignments(course.getAssignments());
        updateStudents(course.getStudents());
    }
}

