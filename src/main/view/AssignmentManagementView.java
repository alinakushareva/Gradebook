// AssignmentManagementView.java
package view;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;
import model.Observer;

import java.util.*;
import java.util.stream.Collectors;

public class AssignmentManagementView implements Observer {
    private final Scene scene;
    private final ComboBox<String> courseSelector;
    private final TableView<AssignmentRow> assignmentTable;
    private final ComboBox<String> categorySelector;
    private Course selectedCourse;

    @SuppressWarnings("unchecked")
    public AssignmentManagementView(Teacher teacher, GradebookModel model, MainController controller, MainView mainView) {
        model.addObserver(this);

        // Left side layout
        VBox leftLayout = new VBox(15);
        leftLayout.setPadding(new Insets(30));

        Label title = new Label("📘 Manage Assignments");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");

        courseSelector = new ComboBox<>();
        courseSelector.getItems().addAll(teacher.getCourses().stream().map(Course::getCourseName).toList());
        courseSelector.setPromptText("Select Course");
        courseSelector.setOnAction(e -> {
            String courseName = courseSelector.getValue();
            selectedCourse = model.getCourseByName(courseName);
            updateAssignmentTable();
            updateCategorySelector();
            updateDropCategorySelector();
        });

        assignmentTable = new TableView<>();
        TableColumn<AssignmentRow, String> nameCol = new TableColumn<>("Assignment");
        nameCol.setCellValueFactory(data -> data.getValue().titleProperty());

        TableColumn<AssignmentRow, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> data.getValue().categoryProperty());

        TableColumn<AssignmentRow, String> maxPointsCol = new TableColumn<>("Max Points");
        maxPointsCol.setCellValueFactory(data -> data.getValue().maxPointsProperty());

        TableColumn<AssignmentRow, String> gradedCol = new TableColumn<>("Graded");
        gradedCol.setCellValueFactory(data -> data.getValue().gradedProperty());

        TableColumn<AssignmentRow, String> avgCol = new TableColumn<>("Average");
        avgCol.setCellValueFactory(data -> data.getValue().averageProperty());

        TableColumn<AssignmentRow, String> medianCol = new TableColumn<>("Median");
        medianCol.setCellValueFactory(data -> data.getValue().medianProperty());

        assignmentTable.getColumns().addAll(nameCol, categoryCol, maxPointsCol, gradedCol, avgCol, medianCol);
        assignmentTable.setPlaceholder(new Label("No assignments yet"));

        assignmentTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                AssignmentRow selectedRow = assignmentTable.getSelectionModel().getSelectedItem();
                if (selectedRow != null && selectedCourse != null) {
                    Assignment assignment = selectedCourse.getAssignments().stream()
                        .filter(a -> a.getTitle().equals(selectedRow.title()))
                        .findFirst().orElse(null);
                    if (assignment != null) {
                        GradePopupView.showPopup(selectedCourse, assignment);
                        updateAssignmentTable();
                    }
                }
            }
        });

        TextField assignmentTitleField = new TextField();
        assignmentTitleField.setPromptText("Assignment Title");
        Spinner<Integer> maxPointsSpinner = new Spinner<>(1, 500, 100);
        maxPointsSpinner.setEditable(true);

        categorySelector = new ComboBox<>();
        categorySelector.setPromptText("Select Category");

        Button addBtn = new Button("Add Assignment");
        addBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            String assignmentTitle = assignmentTitleField.getText().trim();
            int points = maxPointsSpinner.getValue();
            String category = categorySelector.getValue();

            if (!assignmentTitle.isEmpty() && category != null) {
                Assignment a = new Assignment(assignmentTitle, points, selectedCourse);
                selectedCourse.addAssignment(a);
                for (Category cat : selectedCourse.getCategories()) {
                    if (cat.getName().equals(category)) {
                        cat.addAssignment(a);
                        break;
                    }
                }
                assignmentTitleField.clear();
                updateAssignmentTable();
            }
        });

        Button removeBtn = new Button("Remove Selected");
        removeBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            AssignmentRow selected = assignmentTable.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            Assignment toRemove = selectedCourse.getAssignments().stream()
                .filter(a -> a.getTitle().equals(selected.title()))
                .findFirst().orElse(null);
            if (toRemove != null) {
                selectedCourse.removeAssignment(toRemove);
                updateAssignmentTable();
            }
        });

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> mainView.setScene(new TeacherDashboardView(teacher, model, controller, mainView).getScene()));

        leftLayout.getChildren().addAll(
            title,
            courseSelector,
            assignmentTable,
            new HBox(10, new Label("Title:"), assignmentTitleField),
            new HBox(10, new Label("Points:"), maxPointsSpinner),
            categorySelector,
            new HBox(10, addBtn, removeBtn),
            backBtn
        );

        // Right-side panel: Drop logic
        VBox dropPanel = new VBox(10);
        dropPanel.setPadding(new Insets(20));
        dropPanel.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px;");

        Label dropTitle = new Label("⚙️ Drop Lowest Grades");
        dropTitle.setStyle("-fx-font-weight: bold");

        ComboBox<String> dropCategorySelector = new ComboBox<>();
        dropCategorySelector.setPromptText("Select Category");
        Spinner<Integer> dropSpinner = new Spinner<>(0, 10, 0);
        dropSpinner.setEditable(true);

        Button applyDropBtn = new Button("Apply Drop Count");
        applyDropBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            String catName = dropCategorySelector.getValue();
            int dropCount = dropSpinner.getValue();

            selectedCourse.getCategories().stream()
                .filter(cat -> cat.getName().equals(catName))
                .findFirst()
                .ifPresent(cat -> cat.setDropLowestCount(dropCount, selectedCourse));

            // Recalculate final grades and notify observers
            for (Student s : selectedCourse.getStudents()) {
                double avg = selectedCourse.calculateStudentAverage(s);
                FinalGrade updated = FinalGrade.getLetterGrade(avg);
                selectedCourse.assignFinalGrade(s, updated);
            }

            selectedCourse.notifyObservers();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Drop Rule Applied");
            alert.setHeaderText(null);
            alert.setContentText("Now dropping " + dropCount + " lowest grades in category " + catName);
            alert.showAndWait();
        });


        dropPanel.getChildren().addAll(dropTitle, dropCategorySelector, dropSpinner, applyDropBtn);

        HBox fullLayout = new HBox(20, leftLayout, dropPanel);

        this.scene = new Scene(fullLayout, 1000, 600);
    }

    private void updateAssignmentTable() {
        if (selectedCourse == null) return;
        List<AssignmentRow> rows = new ArrayList<>();
        for (Assignment a : selectedCourse.getAssignments()) {
            String category = selectedCourse.getCategories().stream()
                .filter(cat -> cat.getAssignments().contains(a))
                .map(Category::getName)
                .findFirst().orElse("None");

            long graded = selectedCourse.getStudents().stream()
                .map(a::getGrade)
                .filter(Objects::nonNull)
                .count();

            List<Double> grades = selectedCourse.getStudents().stream()
                .map(a::getGrade)
                .filter(Objects::nonNull)
                .map(Grade::getPointsReceived)
                .sorted()
                .toList();

            double average = grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double median = 0.0;
            int n = grades.size();
            if (n > 0) {
                median = (n % 2 == 1) ? grades.get(n / 2) : (grades.get(n / 2 - 1) + grades.get(n / 2)) / 2.0;
            }

            rows.add(new AssignmentRow(
                a.getTitle(),
                category,
                String.valueOf((int) a.getMaxPoints()),
                graded + "/" + selectedCourse.getStudents().size(),
                String.format("%.1f", average),
                String.format("%.1f", median)
            ));
        }
        assignmentTable.setItems(FXCollections.observableArrayList(rows));
    }

    private void updateCategorySelector() {
        if (selectedCourse == null) return;
        List<String> names = selectedCourse.getCategories().stream().map(Category::getName).toList();
        categorySelector.getItems().setAll(names);
    }

    @SuppressWarnings("unchecked")
	private void updateDropCategorySelector() {
        // Used for right-panel category dropdown
        if (selectedCourse == null) return;
        List<String> names = selectedCourse.getCategories().stream().map(Category::getName).toList();
        ((ComboBox<String>) ((VBox) ((HBox) scene.getRoot()).getChildren().get(1)).getChildren().get(1)).getItems().setAll(names);
    }

    public Scene getScene() {
        return scene;
    }

    public record AssignmentRow(String title, String category, String maxPoints, String graded, String average, String median) {
        public javafx.beans.property.SimpleStringProperty titleProperty() {
            return new javafx.beans.property.SimpleStringProperty(title);
        }
        public javafx.beans.property.SimpleStringProperty categoryProperty() {
            return new javafx.beans.property.SimpleStringProperty(category);
        }
        public javafx.beans.property.SimpleStringProperty maxPointsProperty() {
            return new javafx.beans.property.SimpleStringProperty(maxPoints);
        }
        public javafx.beans.property.SimpleStringProperty gradedProperty() {
            return new javafx.beans.property.SimpleStringProperty(graded);
        }
        public javafx.beans.property.SimpleStringProperty averageProperty() {
            return new javafx.beans.property.SimpleStringProperty(average);
        }
        public javafx.beans.property.SimpleStringProperty medianProperty() {
            return new javafx.beans.property.SimpleStringProperty(median);
        }
    }

    @Override
    public void update() {
        updateAssignmentTable();
    }
}
