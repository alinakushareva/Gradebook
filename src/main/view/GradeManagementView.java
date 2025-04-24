/**
 * Project Name: Gradebook
 * File Name: GradeManagementView.java
 * Course: CSC 335 Spring 2025
 *
 * Purpose: 
 * This class provides a JavaFX view for teachers to manage student grades in a selected course.
 * It supports toggling between grading modes (weighted vs. total points), assigning final grades,
 * and viewing student averages and final grades in a table.
 * AI generated class!
 */
package view;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;

import java.util.*;

public class GradeManagementView implements model.Observer {
    private final Scene scene;
    private Course selectedCourse;
    private final TableView<StudentRow> studentTable;

    /**
     * Constructs the GradeManagementView and sets up the UI layout and behavior.
     *
     * @param teacher the logged-in teacher
     * @param model the data model
     * @param controller the main controller
     * @param mainView the reference to the MainView for navigation
     */
    @SuppressWarnings("unchecked")
	public GradeManagementView(Teacher teacher, GradebookModel model, MainController controller, MainView mainView) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));

        // View title
        Label title = new Label("📊 Grade Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");

        // Dropdown for selecting a course to manage
        ComboBox<String> courseSelector = new ComboBox<>();
        courseSelector.getItems().addAll(
                teacher.getCourses().stream().map(Course::getCourseName).toList()
        );
        courseSelector.setPromptText("Select Course");

        // Set up student table with name, average, and final grade columns
        studentTable = new TableView<>();
        
        // Column for displaying the full name of the student
        TableColumn<StudentRow, String> nameCol = new TableColumn<>("Student");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());

        // Column for displaying the student's current average in the course
        TableColumn<StudentRow, String> avgCol = new TableColumn<>("Current Average");
        avgCol.setCellValueFactory(data -> data.getValue().averageProperty());

        // Column for displaying the final letter grade (if assigned)
        TableColumn<StudentRow, String> finalCol = new TableColumn<>("Final Grade");
        finalCol.setCellValueFactory(data -> data.getValue().finalGradeProperty());

        // Add all three columns to the student table
        studentTable.getColumns().addAll(nameCol, avgCol, finalCol);

        // Load selected course and observe changes
        courseSelector.setOnAction(e -> {
            selectedCourse = model.getCourseByName(courseSelector.getValue());
            if (selectedCourse != null) {
                selectedCourse.addObserver(this); // register as observer
                updateStudentTable(); // Refresh table
            }
        });

        // Grading mode toggle setup
        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton totalPointsMode = new RadioButton("Total Points Mode");
        RadioButton weightedCategoryMode = new RadioButton("Weighted Category Mode");
        totalPointsMode.setToggleGroup(modeGroup);
        weightedCategoryMode.setToggleGroup(modeGroup);

        // Apply grading mode button
        Button applyModeBtn = new Button("Apply Mode");
        applyModeBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            selectedCourse.setGradingMode(weightedCategoryMode.isSelected());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Grading mode set to: " + (weightedCategoryMode.isSelected() ? "Weighted Categories" : "Total Points"));
            alert.showAndWait();
            updateStudentTable();
        });

        // Assign final grades to all students
        Button calculateGradesBtn = new Button("Assign Final Grades");
        calculateGradesBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            for (Student s : selectedCourse.getStudents()) {
                // Get the true instance from the model to ensure GPA gets updated correctly
                Student modelStudent = model.getStudentByUsername(s.getUsername());
                double avg = selectedCourse.calculateStudentAverage(modelStudent);
                FinalGrade letter = getLetterGrade(avg);
                selectedCourse.assignFinalGrade(modelStudent, letter);
            }
            updateStudentTable(); // Refresh display after update
        });

        // Return to the teacher dashboard
        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> mainView.setScene(new TeacherDashboardView(teacher, model, controller, mainView).getScene()));

        // Build final layout
        layout.getChildren().addAll(
                title,
                courseSelector,
                new Label("Choose Grade Mode:"),
                new HBox(10, totalPointsMode, weightedCategoryMode, applyModeBtn),
                new Label("Students and Grades:"),
                studentTable,
                calculateGradesBtn,
                backBtn
        );

        this.scene = new Scene(layout, 700, 600);

        // Register GradeManagementView as observer of the model
        model.addObserver(this);
    }

    /**
     * Maps numeric average to FinalGrade enum.
     *
     * @param avg average percentage score
     * @return corresponding FinalGrade
     */
    private FinalGrade getLetterGrade(double avg) {
        if (avg >= 90) return FinalGrade.A;
        if (avg >= 80) return FinalGrade.B;
        if (avg >= 70) return FinalGrade.C;
        if (avg >= 60) return FinalGrade.D;
        return FinalGrade.E;
    }

    /**
     * Updates the student table with current averages and final grades.
     */
    private void updateStudentTable() {
        // If no course is selected, exit early
        if (selectedCourse == null) return;
        
        List<StudentRow> rows = new ArrayList<>();
        
        // Iterate over each student in the selected course
        for (Student s : selectedCourse.getStudents()) {
            // Always fetch the authoritative Student from the model
            Student modelStudent = selectedCourse.getStudents().stream()
                .filter(st -> st.getUsername().equals(s.getUsername()))
                .findFirst().orElse(s);  // fallback just in case

            // Calculate the average percentage grade for this student
            double avg = selectedCourse.calculateStudentAverage(modelStudent);
           
            // Fetch the student's final letter grade for this course, if it has been assigned
            FinalGrade finalGrade = selectedCourse.getFinalGrade(modelStudent);
           
            // Create a new row for the student table, formatting the average and grade
            rows.add(new StudentRow(
                modelStudent.getFirstName() + " " + modelStudent.getLastName(),
                String.format("%.2f", avg),
                finalGrade != null ? finalGrade.toString() : "Not Assigned"
            ));
        }
        // Refresh the table by setting new row data
        studentTable.setItems(FXCollections.observableArrayList(rows));
    }

    /**
     * Returns the JavaFX Scene representing this view.
     *
     * @return the scene object
     */
    public Scene getScene() {
        return scene;
    }

    @Override
    public void update() {
        updateStudentTable();
    }

    public record StudentRow(String name, String average, String finalGrade) {
        public javafx.beans.property.SimpleStringProperty nameProperty() {
            return new javafx.beans.property.SimpleStringProperty(name);
        }

        public javafx.beans.property.SimpleStringProperty averageProperty() {
            return new javafx.beans.property.SimpleStringProperty(average);
        }

        public javafx.beans.property.SimpleStringProperty finalGradeProperty() {
            return new javafx.beans.property.SimpleStringProperty(finalGrade);
        }
    }
}
