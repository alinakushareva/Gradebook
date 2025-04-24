/**
 * Project Name: Gradebook
 * File Name: AssignmentManagementView.java
 * Course: CSC 335 Spring 2025
 *
 * Purpose: 
 * This class defines the JavaFX user interface for managing assignments within a teacher's selected course.
 * Teachers can add/remove assignments, assign categories, view grading status, and apply rules for dropping
 * the lowest scores in assignment categories. It utilizes the Observer pattern to keep UI updated with course state.
 * AI generated class!
 */
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

    /**
     * Constructor that initializes the assignment management view for teachers.
     * Sets up UI components and registers event handlers.
     */
    @SuppressWarnings("unchecked")
    public AssignmentManagementView(Teacher teacher, GradebookModel model, MainController controller, MainView mainView) {
        model.addObserver(this);

        // Left side layout
        VBox leftLayout = new VBox(15);
        leftLayout.setPadding(new Insets(30));

        // Header label for the section
        Label title = new Label("📘 Manage Assignments");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold");

        // Dropdown for course selection
        courseSelector = new ComboBox<>();
        courseSelector.getItems().addAll(teacher.getCourses().stream().map(Course::getCourseName).toList());
        courseSelector.setPromptText("Select Course");
        
        // When a course is selected, load its data into the view
        courseSelector.setOnAction(e -> {
            String courseName = courseSelector.getValue();
            selectedCourse = model.getCourseByName(courseName);
            updateAssignmentTable();
            updateCategorySelector();
            updateDropCategorySelector();
        });

        // Set up the assignment table for displaying assignment info
        assignmentTable = new TableView<>();
        
        // Assignment name column
        TableColumn<AssignmentRow, String> nameCol = new TableColumn<>("Assignment");
        nameCol.setCellValueFactory(data -> data.getValue().titleProperty());

        // Category column
        TableColumn<AssignmentRow, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> data.getValue().categoryProperty());

        // Maximum points column
        TableColumn<AssignmentRow, String> maxPointsCol = new TableColumn<>("Max Points");
        maxPointsCol.setCellValueFactory(data -> data.getValue().maxPointsProperty());

        // Graded count column
        TableColumn<AssignmentRow, String> gradedCol = new TableColumn<>("Graded");
        gradedCol.setCellValueFactory(data -> data.getValue().gradedProperty());

        // Class average column
        TableColumn<AssignmentRow, String> avgCol = new TableColumn<>("Average");
        avgCol.setCellValueFactory(data -> data.getValue().averageProperty());

        // Class median column
        TableColumn<AssignmentRow, String> medianCol = new TableColumn<>("Median");
        medianCol.setCellValueFactory(data -> data.getValue().medianProperty());

        // Add all columns to the assignment table
        assignmentTable.getColumns().addAll(nameCol, categoryCol, maxPointsCol, gradedCol, avgCol, medianCol);
        assignmentTable.setPlaceholder(new Label("No assignments yet"));

        // Set up double-click event to open grade popup
        assignmentTable.setOnMouseClicked(event -> {
            // Allow double-click to open grade popup for assignment
            if (event.getClickCount() == 2) {
                AssignmentRow selectedRow = assignmentTable.getSelectionModel().getSelectedItem();
                if (selectedRow != null && selectedCourse != null) {
                    // Locate the matching assignment by title
                    Assignment assignment = selectedCourse.getAssignments().stream()
                        .filter(a -> a.getTitle().equals(selectedRow.title()))
                        .findFirst().orElse(null);
                    // If found, open the grade popup for editing
                    if (assignment != null) {
                        GradePopupView.showPopup(selectedCourse, assignment);
                        updateAssignmentTable();
                    }
                }
            }
        });

        // Text field for entering the assignment title
        TextField assignmentTitleField = new TextField();
        assignmentTitleField.setPromptText("Assignment Title");
        
        // Spinner to set maximum points for the assignment (1 to 500, default 100)
        Spinner<Integer> maxPointsSpinner = new Spinner<>(1, 500, 100);
        maxPointsSpinner.setEditable(true);

        // Dropdown to select an existing category to assign the assignment to
        categorySelector = new ComboBox<>();
        categorySelector.setPromptText("Select Category");

        // Button to add a new assignment to the selected course
        Button addBtn = new Button("Add Assignment");
        addBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            // Get user input for assignment
            String assignmentTitle = assignmentTitleField.getText().trim();
            int points = maxPointsSpinner.getValue();
            String category = categorySelector.getValue();

            // Proceed only if the input is valid
            if (!assignmentTitle.isEmpty() && category != null) {
                // Create new assignment and add to course
                Assignment a = new Assignment(assignmentTitle, points, selectedCourse);
                selectedCourse.addAssignment(a);
                // Add the assignment to the correct category
                for (Category cat : selectedCourse.getCategories()) {
                    if (cat.getName().equals(category)) {
                        cat.addAssignment(a);
                        break;
                    }
                }
                // Clear title field after adding and refresh table
                assignmentTitleField.clear();
                updateAssignmentTable();
            }
        });

        // Button to remove the selected assignment from the course
        Button removeBtn = new Button("Remove Selected");
        removeBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            AssignmentRow selected = assignmentTable.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            // Find the corresponding Assignment object
            Assignment toRemove = selectedCourse.getAssignments().stream()
                .filter(a -> a.getTitle().equals(selected.title()))
                .findFirst().orElse(null);
            // Remove from course and update table if found
            if (toRemove != null) {
                selectedCourse.removeAssignment(toRemove);
                updateAssignmentTable();
            }
        });

        // Button to return to the teacher dashboard
        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> mainView.setScene(new TeacherDashboardView(teacher, model, controller, mainView).getScene()));

        // Add all controls to the left layout container
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

        // Right-side panel: Drop logic setup for dropping lowest grades per category
        VBox dropPanel = new VBox(10);
        dropPanel.setPadding(new Insets(20));
        dropPanel.setStyle("-fx-border-color: #ccc; -fx-border-width: 1px;");

        // Title label for the panel
        Label dropTitle = new Label("⚙️ Drop Lowest Grades");
        dropTitle.setStyle("-fx-font-weight: bold");

        // Dropdown to select which category the drop logic should apply to
        ComboBox<String> dropCategorySelector = new ComboBox<>();
        dropCategorySelector.setPromptText("Select Category");
        // Spinner to choose how many lowest grades to drop (range: 0 to 10)
        Spinner<Integer> dropSpinner = new Spinner<>(0, 10, 0);
        dropSpinner.setEditable(true);

        // Button to apply drop logic
        Button applyDropBtn = new Button("Apply Drop Count");
        applyDropBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            String catName = dropCategorySelector.getValue();
            int dropCount = dropSpinner.getValue();

            // Find matching category and set drop count
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
            // Notify UI observers (like tables or GPA labels) of the changes
            selectedCourse.notifyObservers();

            // Show confirmation message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Drop Rule Applied");
            alert.setHeaderText(null);
            alert.setContentText("Now dropping " + dropCount + " lowest grades in category " + catName);
            alert.showAndWait();
        });

        // Add all UI elements to the drop panel
        dropPanel.getChildren().addAll(dropTitle, dropCategorySelector, dropSpinner, applyDropBtn);

        // Combine left (assignments) and right (drop logic) panels into one layout
        HBox fullLayout = new HBox(20, leftLayout, dropPanel);

        this.scene = new Scene(fullLayout, 1000, 600);
    }
    
    /**
     * Updates the assignment table with the latest data from the selected course.
     * Shows assignment name, category, max points, graded count, average, and median.
     */
    private void updateAssignmentTable() {
        if (selectedCourse == null) return; // Exit if no course is selected
        List<AssignmentRow> rows = new ArrayList<>();
        
        // Iterate through all assignments in the selected course
        for (Assignment a : selectedCourse.getAssignments()) {
            // Find the category this assignment belongs to
            String category = selectedCourse.getCategories().stream()
                .filter(cat -> cat.getAssignments().contains(a))
                .map(Category::getName)
                .findFirst().orElse("None");

            // Count how many students have received grades for this assignment
            long graded = selectedCourse.getStudents().stream()
                .map(a::getGrade)
                .filter(Objects::nonNull)
                .count();

            // Collect list of all numeric grades (points received) for this assignment
            List<Double> grades = selectedCourse.getStudents().stream()
                .map(a::getGrade)
                .filter(Objects::nonNull)
                .map(Grade::getPointsReceived)
                .sorted()
                .toList();

            // Calculate average score
            double average = grades.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            
            // Calculate median score
            double median = 0.0;
            int n = grades.size();
            if (n > 0) {
                median = (n % 2 == 1) ? grades.get(n / 2) : (grades.get(n / 2 - 1) + grades.get(n / 2)) / 2.0;
            }

            // Add a row to the table with all collected values
            rows.add(new AssignmentRow(
                a.getTitle(),
                category,
                String.valueOf((int) a.getMaxPoints()),
                graded + "/" + selectedCourse.getStudents().size(),
                String.format("%.1f", average),
                String.format("%.1f", median)
            ));
        }
        // Update the table with new rows
        assignmentTable.setItems(FXCollections.observableArrayList(rows));
    }

    /**
     * Updates the category dropdown selector with categories from the selected course.
     */
    private void updateCategorySelector() {
        if (selectedCourse == null) return;
        // Extract category names from the selected course
        List<String> names = selectedCourse.getCategories().stream().map(Category::getName).toList();
        categorySelector.getItems().setAll(names);
    }

    /**
     * Updates the right-side panel dropdown used for applying drop rules to categories.
     * Uses manual node traversal to access the drop category ComboBox.
     */
    @SuppressWarnings("unchecked")
	private void updateDropCategorySelector() {
        // Used for right-panel category dropdown
        if (selectedCourse == null) return;
        List<String> names = selectedCourse.getCategories().stream().map(Category::getName).toList();
        // Navigate the scene's layout to find the drop category ComboBox and update it
        ((ComboBox<String>) ((VBox) ((HBox) scene.getRoot()).getChildren().get(1)).getChildren().get(1)).getItems().setAll(names);
    }

    /**
     * Returns the scene for this view, used for displaying in the application window.
     * @return Scene object representing this view
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Data holder class for a row in the assignment table.
     * Each row represents one assignment with key information.
     */
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

    /**
     * Callback triggered when an observer notification is received.
     * Used here to update the assignment table when data changes.
     */
    @Override
    public void update() {
        updateAssignmentTable();
    }
}