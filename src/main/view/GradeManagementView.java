// GradeManagementView.java
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

    @SuppressWarnings("unchecked")
	public GradeManagementView(Teacher teacher, GradebookModel model, MainController controller, MainView mainView) {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(30));

        Label title = new Label("📊 Grade Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");

        ComboBox<String> courseSelector = new ComboBox<>();
        courseSelector.getItems().addAll(
                teacher.getCourses().stream().map(Course::getCourseName).toList()
        );
        courseSelector.setPromptText("Select Course");

        // Student table
        studentTable = new TableView<>();
        TableColumn<StudentRow, String> nameCol = new TableColumn<>("Student");
        nameCol.setCellValueFactory(data -> data.getValue().nameProperty());

        TableColumn<StudentRow, String> avgCol = new TableColumn<>("Current Average");
        avgCol.setCellValueFactory(data -> data.getValue().averageProperty());

        TableColumn<StudentRow, String> finalCol = new TableColumn<>("Final Grade");
        finalCol.setCellValueFactory(data -> data.getValue().finalGradeProperty());

        studentTable.getColumns().addAll(nameCol, avgCol, finalCol);

        courseSelector.setOnAction(e -> {
            selectedCourse = model.getCourseByName(courseSelector.getValue());
            if (selectedCourse != null) {
                selectedCourse.addObserver(this); // register as observer
                updateStudentTable();
            }
        });

        // Grading mode
        ToggleGroup modeGroup = new ToggleGroup();
        RadioButton totalPointsMode = new RadioButton("Total Points Mode");
        RadioButton weightedCategoryMode = new RadioButton("Weighted Category Mode");
        totalPointsMode.setToggleGroup(modeGroup);
        weightedCategoryMode.setToggleGroup(modeGroup);

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
            updateStudentTable();
        });

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> mainView.setScene(new TeacherDashboardView(teacher, model, controller, mainView).getScene()));

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

    private FinalGrade getLetterGrade(double avg) {
        if (avg >= 90) return FinalGrade.A;
        if (avg >= 80) return FinalGrade.B;
        if (avg >= 70) return FinalGrade.C;
        if (avg >= 60) return FinalGrade.D;
        return FinalGrade.E;
    }

    private void updateStudentTable() {
        if (selectedCourse == null) return;
        List<StudentRow> rows = new ArrayList<>();
        for (Student s : selectedCourse.getStudents()) {
            // Always fetch the authoritative Student from the model
            Student modelStudent = selectedCourse.getStudents().stream()
                .filter(st -> st.getUsername().equals(s.getUsername()))
                .findFirst().orElse(s);  // fallback just in case

            double avg = selectedCourse.calculateStudentAverage(modelStudent);
            FinalGrade finalGrade = selectedCourse.getFinalGrade(modelStudent);
            rows.add(new StudentRow(
                modelStudent.getFirstName() + " " + modelStudent.getLastName(),
                String.format("%.2f", avg),
                finalGrade != null ? finalGrade.toString() : "Not Assigned"
            ));
        }

        studentTable.setItems(FXCollections.observableArrayList(rows));
    }

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
