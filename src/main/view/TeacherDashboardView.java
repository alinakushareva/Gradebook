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

    /**
     * Constructs the detailed course view.
     * Displays assignments and students, with grading tools.
     */
    public CourseDetailView(Course course, TeacherController controller) {
        courseNameLabel.setText("Course: " + course.getCourseName());
        courseNameLabel.setFont(new Font(18));

        // Button Actions
        sortByNameButton.setOnAction(e -> {
            List<Student> sorted = controller.sortStudentsByName(course);
            updateStudents(sorted);
        });

        sortByGradeButton.setOnAction(e -> {
            if (!assignmentTable.getItems().isEmpty()) {
                Assignment selected = assignmentTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    List<Student> sorted = controller.sortStudentsByAssignmentGrade(course, selected);
                    updateStudents(sorted);
                }
            }
        });

        assignFinalGradesButton.setOnAction(e -> {
            for (Student s : course.getStudents()) {
                double average = course.calculateStudentAverage(s);
                FinalGrade fg = GradeCalculator.getLetterGrade(average);
                controller.assignFinalGrade(course, s, fg);
            }
            updateStudents(course.getStudents()); // refresh
        });

        VBox layout = new VBox(10, courseNameLabel, assignmentTable, studentTable, 
                               new HBox(10, sortByNameButton, sortByGradeButton, assignFinalGradesButton));
        layout.setPadding(new Insets(15));
        scene = new Scene(layout, 900, 600);
    }

    public Scene getScene() {
        return scene;
    }

    /**
     * Refreshes the list of assignments in the table view.
     */
    public void updateAssignments(List<Assignment> assignments) {
        assignmentTable.getItems().setAll(assignments);
    }

    /**
     * Refreshes the list of students in the table view.
     */
    public void updateStudents(List<Student> students) {
        studentTable.getItems().setAll(students);
    }

    /**
     * Loads course-specific content into view.
     */
    public void displayCourseInfo(Course course) {
        courseNameLabel.setText("Course: " + course.getCourseName());
        updateAssignments(course.getAssignments());
        updateStudents(course.getStudents());
    }
}
