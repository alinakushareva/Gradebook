package org.gradebook.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import org.gradebook.controller.StudentController;
import org.gradebook.model.Assignment;
import org.gradebook.model.Course;
import org.gradebook.model.Grade;

/**
 * Dashboard view for students.
 * Displays courses, assignments, grades, and GPA.
 */
public class StudentDashboardView {
    private StudentController controller;
    private Scene scene;
    private ListView<Course> courseList;
    private TableView<Assignment> assignmentTable;
    private Label gpaLabel;
    private Button viewAssignmentButton;
    private Button calculateGPAButton;
    
    /**
     * Constructor for StudentDashboardView class.
     *
     * @param controller the student controller
     */
    public StudentDashboardView(StudentController controller) {
        this.controller = controller;
        createDashboardScene();
    }
    
    /**
     * Creates the dashboard scene.
     */
    private void createDashboardScene() {
        // Create the root layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Create a top panel with student info
        HBox topPanel = createTopPanel();
        root.setTop(topPanel);
        
        // Create left panel with course list
        VBox leftPanel = createLeftPanel();
        root.setLeft(leftPanel);
        
        // Create center panel with assignment table
        VBox centerPanel = createCenterPanel();
        root.setCenter(centerPanel);
        
        // Create a scene with the root layout
        scene = new Scene(root, 800, 600);
    }
    
    /**
     * Creates the top panel with student information.
     *
     * @return the top panel
     */
    private HBox createTopPanel() {
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));
        
        // Welcome label with student name
        Label welcomeLabel = new Label("Welcome, " + controller.getStudent().getFullName() + "!");
        welcomeLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        
        // GPA label
        gpaLabel = new Label("GPA: " + String.format("%.2f", controller.calculateGPA()));
        
        // Calculate GPA button
        calculateGPAButton = new Button("Refresh GPA");
        calculateGPAButton.setOnAction(e -> updateGPALabel(controller.calculateGPA()));
        
        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> controller.logout());
        
        // Add components to the panel
        topPanel.getChildren().addAll(welcomeLabel, gpaLabel, calculateGPAButton, logoutButton);
        
        return topPanel;
    }
    
    /**
     * Creates the left panel with course list.
     *
     * @return the left panel
     */
    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setPrefWidth(200);
        
        // Label for courses
        Label coursesLabel = new Label("Your Courses");
        coursesLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        
        // Course list view
        courseList = new ListView<>();
        courseList.setItems(FXCollections.observableArrayList(controller.getEnrolledCourses()));
        courseList.setCellFactory(param -> new CourseListCell());
        
        // Selection listener to update assignment table
        courseList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        updateAssignmentTable(controller.getAssignmentsForCourse(newValue));
                    }
                });
        
        // Add components to the panel
        leftPanel.getChildren().addAll(coursesLabel, courseList);
        
        return leftPanel;
    }
    
    /**
     * Creates the center panel with assignment table.
     *
     * @return the center panel
     */
    private VBox createCenterPanel() {
        VBox centerPanel = new VBox(10);
        centerPanel.setPadding(new Insets(10));
        
        // Label for assignments
        Label assignmentsLabel = new Label("Assignments");
        assignmentsLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        
        // Assignment table
        assignmentTable = new TableView<>();
        
        // Table columns
        TableColumn<Assignment, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        
        TableColumn<Assignment, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCategory() != null ? data.getValue().getCategory() : ""));
        
        TableColumn<Assignment, Number> pointsCol = new TableColumn<>("Max Points");
        pointsCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getMaxPoints()));
        
        TableColumn<Assignment, String> gradeCol = new TableColumn<>("Your Grade");
        gradeCol.setCellValueFactory(data -> {
            Assignment assignment = data.getValue();
            Course course = courseList.getSelectionModel().getSelectedItem();
            if (course != null) {
                Grade grade = controller.getGrade(course, assignment);
                if (grade != null) {
                    return new SimpleStringProperty(grade.toString());
                }
            }
            return new SimpleStringProperty("Not graded");
        });
        
        assignmentTable.getColumns().addAll(titleCol, categoryCol, pointsCol, gradeCol);
        
        // View assignment button
        viewAssignmentButton = new Button("View Details");
        viewAssignmentButton.setDisable(true);
        
        // Enable button when an assignment is selected
        assignmentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> viewAssignmentButton.setDisable(newValue == null));
        
        // Add action to view assignment button
        viewAssignmentButton.setOnAction(e -> showAssignmentDetailsDialog());
        
        // Add components to the panel
        centerPanel.getChildren().addAll(assignmentsLabel, assignmentTable, viewAssignmentButton);
        
        return centerPanel;
    }
    
    /**
     * Gets the scene for this view.
     *
     * @return the dashboard scene
     */
    public Scene getScene() {
        return scene;
    }
    
    /**
     * Updates the course list.
     *
     * @param courses the list of courses
     */
    public void updateCourseList(java.util.List<Course> courses) {
        courseList.setItems(FXCollections.observableArrayList(courses));
    }
    
    /**
     * Updates the assignment table.
     *
     * @param assignments the list of assignments
     */
    public void updateAssignmentTable(java.util.List<Assignment> assignments) {
        assignmentTable.setItems(FXCollections.observableArrayList(assignments));
    }
    
    /**
     * Updates the GPA label.
     *
     * @param gpa the GPA value
     */
    public void updateGPALabel(double gpa) {
        gpaLabel.setText("GPA: " + String.format("%.2f", gpa));
    }
    
    /**
     * Shows a dialog with details about the selected assignment.
     */
    private void showAssignmentDetailsDialog() {
        Assignment selectedAssignment = assignmentTable.getSelectionModel().getSelectedItem();
        Course selectedCourse = courseList.getSelectionModel().getSelectedItem();
        
        if (selectedAssignment == null || selectedCourse == null) {
            return;
        }
        
        // Create a dialog window
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.setTitle("Assignment Details");
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        
        // Create the dialog content
        VBox dialogVbox = new VBox(10);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        
        // Assignment info
        Label titleLabel = new Label("Title: " + selectedAssignment.getTitle());
        titleLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        
        Label categoryLabel = new Label("Category: " + 
                (selectedAssignment.getCategory() != null ? selectedAssignment.getCategory() : "None"));
        
        Label pointsLabel = new Label("Maximum Points: " + selectedAssignment.getMaxPoints());
        
        // Grade information
        Grade grade = controller.getGrade(selectedCourse, selectedAssignment);
        Label gradeLabel = new Label("Your Grade: " + (grade != null ? grade.toString() : "Not graded yet"));
        
        if (grade != null) {
            gradeLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
            
            // Determine color based on performance
            if (grade.getPercentage() >= 90) {
                gradeLabel.setTextFill(javafx.scene.paint.Color.GREEN);
            } else if (grade.getPercentage() >= 70) {
                gradeLabel.setTextFill(javafx.scene.paint.Color.BLUE);
            } else if (grade.getPercentage() >= 60) {
                gradeLabel.setTextFill(javafx.scene.paint.Color.ORANGE);
            } else {
                gradeLabel.setTextFill(javafx.scene.paint.Color.RED);
            }
        }
        
        // Class average for this assignment
        double average = selectedAssignment.getAverage();
        Label averageLabel = new Label("Class Average: " + 
                (average > 0 ? String.format("%.2f%%", average) : "N/A"));
        
        // Close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> dialogStage.close());
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.getChildren().add(closeButton);
        
        // Add components to dialog
        dialogVbox.getChildren().addAll(
                titleLabel, 
                categoryLabel, 
                pointsLabel, 
                gradeLabel, 
                averageLabel, 
                buttonBox);
        
        // Create the scene
        javafx.scene.Scene dialogScene = new javafx.scene.Scene(dialogVbox, 350, 250);
        dialogStage.setScene(dialogScene);
        
        // Show the dialog
        dialogStage.showAndWait();
    }
    
    /**
     * Custom ListCell for displaying courses.
     */
    private static class CourseListCell extends javafx.scene.control.ListCell<Course> {
        @Override
        protected void updateItem(Course course, boolean empty) {
            super.updateItem(course, empty);
            
            if (empty || course == null) {
                setText(null);
            } else {
                setText(course.getCourseName());
            }
        }
    }
}