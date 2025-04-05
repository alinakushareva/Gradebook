package org.gradebook.view;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import org.gradebook.controller.TeacherController;
import org.gradebook.model.Assignment;
import org.gradebook.model.Course;
import org.gradebook.model.FinalGrade;
import org.gradebook.model.Grade;
import org.gradebook.model.Student;

import java.util.List;

/**
 * Dashboard view for teachers.
 * Provides interfaces for managing courses, assignments, and grades.
 */
public class TeacherDashboardView {
    private TeacherController controller;
    private Scene scene;
    private ComboBox<Course> courseSelector;
    private TableView<Student> studentTable;
    private TableView<Assignment> assignmentTable;
    private Button addAssignmentButton;
    private Button addStudentButton;
    private Button assignGradeButton;
    private Button groupStudentsButton;
    private Label classAverageLabel;
    
    /**
     * Constructor for TeacherDashboardView class.
     *
     * @param controller the teacher controller
     */
    public TeacherDashboardView(TeacherController controller) {
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
        
        // Create a top panel with teacher info and course selector
        HBox topPanel = createTopPanel();
        root.setTop(topPanel);
        
        // Create tabs for different sections
        TabPane tabPane = new TabPane();
        
        // Students tab
        Tab studentsTab = new Tab("Students");
        studentsTab.setContent(createStudentsPanel());
        studentsTab.setClosable(false);
        
        // Assignments tab
        Tab assignmentsTab = new Tab("Assignments");
        assignmentsTab.setContent(createAssignmentsPanel());
        assignmentsTab.setClosable(false);
        
        // Analytics tab
        Tab analyticsTab = new Tab("Analytics");
        analyticsTab.setContent(createAnalyticsPanel());
        analyticsTab.setClosable(false);
        
        // Add tabs to the tab pane
        tabPane.getTabs().addAll(studentsTab, assignmentsTab, analyticsTab);
        
        // Set the tab pane as the center content
        root.setCenter(tabPane);
        
        // Create a scene with the root layout
        scene = new Scene(root, 900, 700);
    }
    
    /**
     * Creates the top panel with teacher information and course selector.
     *
     * @return the top panel
     */
    private HBox createTopPanel() {
        HBox topPanel = new HBox(15);
        topPanel.setPadding(new Insets(10));
        
        // Welcome label with teacher name
        Label welcomeLabel = new Label("Welcome, " + controller.getTeacher().getFullName() + "!");
        welcomeLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        
        // Course selector label
        Label courseSelectorLabel = new Label("Select Course:");
        
        // Course selector
        courseSelector = new ComboBox<>();
        courseSelector.setItems(FXCollections.observableArrayList(controller.getTeachingCourses()));
        courseSelector.setPromptText("Select a course");
        
        // Course selection change listener
        courseSelector.setOnAction(e -> {
            Course selectedCourse = courseSelector.getValue();
            if (selectedCourse != null) {
                updateStudentTable(selectedCourse);
                updateAssignmentTable(selectedCourse);
                updateClassAverage(selectedCourse);
                
                // 强制刷新表格，确保重新计算所有单元格值
                studentTable.refresh();
            }
        });
        
        // Add new course button
        Button addCourseButton = new Button("Add Course");
        addCourseButton.setOnAction(e -> showAddCourseDialog());
        
        // Logout button
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> controller.logout());
        
        // Add components to the panel
        topPanel.getChildren().addAll(welcomeLabel, courseSelectorLabel, courseSelector, 
                addCourseButton, logoutButton);
        
        return topPanel;
    }
    
    /**
     * Creates the students panel.
     *
     * @return the students panel
     */
    private VBox createStudentsPanel() {
        VBox studentsPanel = new VBox(10);
        studentsPanel.setPadding(new Insets(10));
        
        // Label for student list
        Label studentsLabel = new Label("Students in Course");
        studentsLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        
        // Student table
        studentTable = new TableView<>();
        
        // Table columns
        TableColumn<Student, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsername()));
        
        TableColumn<Student, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFullName()));
        
        TableColumn<Student, String> gradeCol = new TableColumn<>("Final Grade");
        gradeCol.setCellValueFactory(data -> {
            Course course = courseSelector.getValue();
            if (course != null) {
                FinalGrade grade = course.getFinalGrade(data.getValue());
                return new SimpleStringProperty(grade != null ? grade.toString() : "Not Assigned");
            }
            return new SimpleStringProperty("N/A");
        });
        gradeCol.setCellFactory(column -> {
            return new javafx.scene.control.TableCell<Student, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
        });
        
        TableColumn<Student, Number> averageCol = new TableColumn<>("Average");
        averageCol.setCellValueFactory(data -> {
            Course course = courseSelector.getValue();
            if (course != null) {
                double avg = course.calculateStudentAverage(data.getValue());
                return new SimpleDoubleProperty(avg >= 0 ? avg : 0);
            }
            return new SimpleDoubleProperty(0);
        });
        
        studentTable.getColumns().addAll(usernameCol, nameCol, gradeCol, averageCol);
        
        // Buttons for student management
        HBox buttonPanel = new HBox(10);
        
        addStudentButton = new Button("Add Student");
        addStudentButton.setOnAction(e -> showAddStudentDialog());
        
        Button removeStudentButton = new Button("Remove Student");
        removeStudentButton.setDisable(true);
        removeStudentButton.setOnAction(e -> showRemoveStudentDialog());
        
        Button assignFinalGradeButton = new Button("Assign Final Grade");
        assignFinalGradeButton.setDisable(true);
        assignFinalGradeButton.setOnAction(e -> showAssignFinalGradeDialog());
        
        // Enable buttons when a student is selected
        studentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean hasSelection = newValue != null;
                    removeStudentButton.setDisable(!hasSelection);
                    assignFinalGradeButton.setDisable(!hasSelection);
                });
        
        // Class average label
        classAverageLabel = new Label("Class Average: N/A");
        classAverageLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        
        // 添加学生管理按钮
        buttonPanel.getChildren().addAll(addStudentButton, removeStudentButton, 
                assignFinalGradeButton, classAverageLabel);
        
        // 创建第二行按钮面板，用于分析功能
        HBox analyticsButtonPanel = new HBox(10);
        analyticsButtonPanel.setPadding(new Insets(5, 0, 0, 0));
        
        // 从Analytics面板移动过来的按钮
        Button sortByNameButton = new Button("Sort by Name");
        sortByNameButton.setOnAction(e -> {
            Course course = courseSelector.getValue();
            if (course != null) {
                updateStudentTable(controller.sortStudentsByName(course));
            }
        });
        
        Button sortByGradeButton = new Button("Sort by Grade");
        sortByGradeButton.setDisable(true);
        
        // Enable sort by grade button when course is selected
        courseSelector.valueProperty().addListener(
                (observable, oldValue, newValue) -> sortByGradeButton.setDisable(newValue == null));
        
        sortByGradeButton.setOnAction(e -> showSortByGradeDialog());
        
        groupStudentsButton = new Button("Group Students");
        groupStudentsButton.setOnAction(e -> showGroupStudentsDialog());
        
        analyticsButtonPanel.getChildren().addAll(
                new Label("Student Analytics:"), sortByNameButton, sortByGradeButton, groupStudentsButton);
        
        // Add components to the panel
        studentsPanel.getChildren().addAll(studentsLabel, studentTable, buttonPanel, analyticsButtonPanel);
        
        return studentsPanel;
    }
    
    /**
     * Creates the assignments panel.
     *
     * @return the assignments panel
     */
    private VBox createAssignmentsPanel() {
        VBox assignmentsPanel = new VBox(10);
        assignmentsPanel.setPadding(new Insets(10));
        
        // Label for assignment list
        Label assignmentsLabel = new Label("Assignments in Course");
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
        
        TableColumn<Assignment, Number> averageCol = new TableColumn<>("Average");
        averageCol.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getAverage()));
        
        assignmentTable.getColumns().addAll(titleCol, categoryCol, pointsCol, averageCol);
        
        // Buttons for assignment management
        HBox buttonPanel = new HBox(10);
        
        addAssignmentButton = new Button("Add Assignment");
        addAssignmentButton.setOnAction(e -> showAddAssignmentDialog());
        
        Button editAssignmentButton = new Button("Edit Assignment");
        editAssignmentButton.setDisable(true);
        editAssignmentButton.setOnAction(e -> showEditAssignmentDialog());
        
        assignGradeButton = new Button("Assign Grades");
        assignGradeButton.setDisable(true);
        assignGradeButton.setOnAction(e -> showAssignGradeDialog());
        
        // Enable buttons when an assignment is selected
        assignmentTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    boolean hasSelection = newValue != null;
                    editAssignmentButton.setDisable(!hasSelection);
                    assignGradeButton.setDisable(!hasSelection);
                });
        
        buttonPanel.getChildren().addAll(addAssignmentButton, editAssignmentButton, assignGradeButton);
        
        // Add components to the panel
        assignmentsPanel.getChildren().addAll(assignmentsLabel, assignmentTable, buttonPanel);
        
        return assignmentsPanel;
    }
    
    /**
     * Creates the analytics panel.
     *
     * @return the analytics panel
     */
    private VBox createAnalyticsPanel() {
        VBox analyticsPanel = new VBox(10);
        analyticsPanel.setPadding(new Insets(10));
        
        // Label for analytics
        Label analyticsLabel = new Label("Course Analytics");
        analyticsLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        
        // 添加一些说明文字
        Label infoLabel = new Label("Analytics features have been moved to the Students tab for easier access.");
        infoLabel.setWrapText(true);
        
        TextArea helpTextArea = new TextArea(
            "Student analytics features include:\n\n" +
            "- Sort by Name: Sorts students alphabetically by name\n" +
            "- Sort by Grade: Sorts students by their grades on a specific assignment\n" +
            "- Group Students: Creates random student groups for group projects"
        );
        helpTextArea.setEditable(false);
        helpTextArea.setPrefHeight(200);
        helpTextArea.setWrapText(true);
        
        // Add components to the panel
        analyticsPanel.getChildren().addAll(analyticsLabel, infoLabel, helpTextArea);
        
        return analyticsPanel;
    }
    
    /**
     * Shows a dialog for adding a new course.
     */
    private void showAddCourseDialog() {
        // Create a dialog window
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.setTitle("Add New Course");
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        
        // Create the dialog content
        VBox dialogVbox = new VBox(10);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        
        // Course name label and field
        Label courseNameLabel = new Label("Course Name:");
        TextField courseNameField = new TextField();
        courseNameField.setPromptText("Enter course name");
        
        // Buttons
        Button addButton = new Button("Add Course");
        Button cancelButton = new Button("Cancel");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(cancelButton, addButton);
        
        // Add components to dialog
        dialogVbox.getChildren().addAll(courseNameLabel, courseNameField, buttonBox);
        
        // Create the scene
        Scene dialogScene = new Scene(dialogVbox, 300, 150);
        dialogStage.setScene(dialogScene);
        
        // Set button actions
        cancelButton.setOnAction(e -> dialogStage.close());
        
        addButton.setOnAction(e -> {
            String courseName = courseNameField.getText().trim();
            
            if (courseName.isEmpty()) {
                // Show error if course name is empty
                Label errorLabel = new Label("Course name cannot be empty");
                errorLabel.setTextFill(Color.RED);
                
                // Remove previous error messages
                dialogVbox.getChildren().removeIf(node -> node instanceof Label && 
                        ((Label) node).getTextFill().equals(Color.RED));
                
                // Add error message
                dialogVbox.getChildren().add(dialogVbox.getChildren().size() - 1, errorLabel);
            } else {
                // Create the course
                Course course = controller.createCourse(courseName);
                
                if (course != null) {
                    // Update course selector
                    courseSelector.setItems(FXCollections.observableArrayList(controller.getTeachingCourses()));
                    courseSelector.setValue(course);
                    
                    // Close the dialog
                    dialogStage.close();
                } else {
                    // Show error if course creation failed
                    Label errorLabel = new Label("Course already exists or could not be created");
                    errorLabel.setTextFill(Color.RED);
                    
                    // Remove previous error messages
                    dialogVbox.getChildren().removeIf(node -> node instanceof Label && 
                            ((Label) node).getTextFill().equals(Color.RED));
                    
                    // Add error message
                    dialogVbox.getChildren().add(dialogVbox.getChildren().size() - 1, errorLabel);
                }
            }
        });
        
        // Show the dialog
        dialogStage.showAndWait();
    }
    
    /**
     * Shows a dialog for adding a student to a course.
     */
    private void showAddStudentDialog() {
        Course selectedCourse = courseSelector.getValue();
        if (selectedCourse == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Course Selected");
            alert.setContentText("Please select a course first.");
            alert.showAndWait();
            return;
        }
        
        // Create a dialog window
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.setTitle("Add Student to Course");
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        
        // Create the dialog content
        VBox dialogVbox = new VBox(10);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        
        // Student username label and field
        Label usernameLabel = new Label("Student Username:");
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter student username");
        
        // Buttons
        Button addButton = new Button("Add Student");
        Button cancelButton = new Button("Cancel");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(cancelButton, addButton);
        
        // Add components to dialog
        dialogVbox.getChildren().addAll(usernameLabel, usernameField, buttonBox);
        
        // Create the scene
        Scene dialogScene = new Scene(dialogVbox, 300, 150);
        dialogStage.setScene(dialogScene);
        
        // Set button actions
        cancelButton.setOnAction(e -> dialogStage.close());
        
        addButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            
            if (username.isEmpty()) {
                // Show error if username is empty
                Label errorLabel = new Label("Username cannot be empty");
                errorLabel.setTextFill(Color.RED);
                
                // Remove previous error messages
                dialogVbox.getChildren().removeIf(node -> node instanceof Label && 
                        ((Label) node).getTextFill().equals(Color.RED));
                
                // Add error message
                dialogVbox.getChildren().add(dialogVbox.getChildren().size() - 1, errorLabel);
            } else {
                // Add student to course
                boolean success = controller.addStudentToCourse(username, selectedCourse);
                
                if (success) {
                    // Update student table
                    updateStudentTable(selectedCourse);
                    
                    // Close the dialog
                    dialogStage.close();
                } else {
                    // Show error if student not found
                    Label errorLabel = new Label("Student not found or already enrolled");
                    errorLabel.setTextFill(Color.RED);
                    
                    // Remove previous error messages
                    dialogVbox.getChildren().removeIf(node -> node instanceof Label && 
                            ((Label) node).getTextFill().equals(Color.RED));
                    
                    // Add error message
                    dialogVbox.getChildren().add(dialogVbox.getChildren().size() - 1, errorLabel);
                }
            }
        });
        
        // Show the dialog
        dialogStage.showAndWait();
    }
    
    /**
     * Shows a dialog for adding an assignment to a course.
     */
    private void showAddAssignmentDialog() {
        Course selectedCourse = courseSelector.getValue();
        if (selectedCourse == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Course Selected");
            alert.setContentText("Please select a course first.");
            alert.showAndWait();
            return;
        }
        
        // Create a dialog window
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.setTitle("Add Assignment to Course");
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        
        // Create the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        
        // Assignment title
        Label titleLabel = new Label("Title:");
        grid.add(titleLabel, 0, 0);
        
        TextField titleField = new TextField();
        titleField.setPromptText("Enter assignment title");
        grid.add(titleField, 1, 0);
        
        // Max points
        Label pointsLabel = new Label("Max Points:");
        grid.add(pointsLabel, 0, 1);
        
        TextField pointsField = new TextField();
        pointsField.setPromptText("Enter max points");
        grid.add(pointsField, 1, 1);
        
        // Category (optional)
        Label categoryLabel = new Label("Category (optional):");
        grid.add(categoryLabel, 0, 2);
        
        TextField categoryField = new TextField();
        categoryField.setPromptText("Enter category name");
        grid.add(categoryField, 1, 2);
        
        // Buttons
        Button addButton = new Button("Add Assignment");
        Button cancelButton = new Button("Cancel");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(cancelButton, addButton);
        grid.add(buttonBox, 0, 4, 2, 1);
        
        // Error label
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        grid.add(errorLabel, 0, 3, 2, 1);
        errorLabel.setVisible(false);
        
        // Create the scene
        Scene dialogScene = new Scene(grid, 400, 250);
        dialogStage.setScene(dialogScene);
        
        // Set button actions
        cancelButton.setOnAction(e -> dialogStage.close());
        
        addButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String pointsText = pointsField.getText().trim();
            String category = categoryField.getText().trim();
            
            // Validate input
            if (title.isEmpty()) {
                errorLabel.setText("Title cannot be empty");
                errorLabel.setVisible(true);
                return;
            }
            
            double maxPoints;
            try {
                maxPoints = Double.parseDouble(pointsText);
                if (maxPoints <= 0) {
                    errorLabel.setText("Max points must be greater than 0");
                    errorLabel.setVisible(true);
                    return;
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Max points must be a valid number");
                errorLabel.setVisible(true);
                return;
            }
            
            // Use empty string if category is not provided
            if (category.isEmpty()) {
                category = null;
            }
            
            // Create the assignment
            Assignment assignment = controller.addAssignment(selectedCourse, title, maxPoints, category);
            
            if (assignment != null) {
                // Update assignment table
                updateAssignmentTable(selectedCourse);
                
                // Close the dialog
                dialogStage.close();
            } else {
                errorLabel.setText("Failed to create assignment");
                errorLabel.setVisible(true);
            }
        });
        
        // Show the dialog
        dialogStage.showAndWait();
    }
    
    /**
     * Shows a dialog for grouping students.
     */
    private void showGroupStudentsDialog() {
        Course selectedCourse = courseSelector.getValue();
        if (selectedCourse == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Course Selected");
            alert.setContentText("Please select a course first.");
            alert.showAndWait();
            return;
        }
        
        // Check if course has students
        List<Student> students = controller.getStudentsForCourse(selectedCourse);
        if (students.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Students in Course");
            alert.setContentText("This course has no students to group.");
            alert.showAndWait();
            return;
        }
        
        // Create a dialog window
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.setTitle("Group Students");
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        
        // Create the dialog content
        VBox dialogVbox = new VBox(10);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        
        // Group size label and field
        Label groupSizeLabel = new Label("Group Size:");
        TextField groupSizeField = new TextField();
        groupSizeField.setPromptText("Enter number of students per group");
        
        // Buttons
        Button groupButton = new Button("Create Groups");
        Button cancelButton = new Button("Cancel");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(cancelButton, groupButton);
        
        // Error label
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        
        // Results area
        TextArea resultsArea = new TextArea();
        resultsArea.setEditable(false);
        resultsArea.setPrefHeight(200);
        resultsArea.setVisible(false);
        
        // Add components to dialog
        dialogVbox.getChildren().addAll(groupSizeLabel, groupSizeField, errorLabel, resultsArea, buttonBox);
        
        // Create the scene
        Scene dialogScene = new Scene(dialogVbox, 400, 350);
        dialogStage.setScene(dialogScene);
        
        // Set button actions
        cancelButton.setOnAction(e -> dialogStage.close());
        
        groupButton.setOnAction(e -> {
            String groupSizeText = groupSizeField.getText().trim();
            
            // Validate input
            if (groupSizeText.isEmpty()) {
                errorLabel.setText("Group size cannot be empty");
                errorLabel.setVisible(true);
                return;
            }
            
            int groupSize;
            try {
                groupSize = Integer.parseInt(groupSizeText);
                if (groupSize <= 0) {
                    errorLabel.setText("Group size must be greater than 0");
                    errorLabel.setVisible(true);
                    return;
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Group size must be a valid number");
                errorLabel.setVisible(true);
                return;
            }
            
            // Create groups
            List<List<Student>> groups = controller.groupStudents(selectedCourse, groupSize);
            
            if (groups != null && !groups.isEmpty()) {
                // Display groups in the results area
                StringBuilder sb = new StringBuilder();
                sb.append("Student Groups:\n\n");
                
                for (int i = 0; i < groups.size(); i++) {
                    sb.append("Group ").append(i + 1).append(":\n");
                    List<Student> group = groups.get(i);
                    
                    for (Student student : group) {
                        sb.append("  - ").append(student.getFullName()).append(" (").append(student.getUsername()).append(")\n");
                    }
                    
                    sb.append("\n");
                }
                
                resultsArea.setText(sb.toString());
                resultsArea.setVisible(true);
                
                // Hide error label if visible
                errorLabel.setVisible(false);
                
                // Resize dialog to show results
                dialogStage.setHeight(400);
            } else {
                errorLabel.setText("Failed to create groups");
                errorLabel.setVisible(true);
            }
        });
        
        // Show the dialog
        dialogStage.showAndWait();
    }
    
    /**
     * Shows a dialog for assigning a final grade to a student.
     */
    private void showAssignFinalGradeDialog() {
        Course selectedCourse = courseSelector.getValue();
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        
        if (selectedCourse == null || selectedStudent == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Course or Student Selected");
            alert.setContentText("Please select a course and a student first.");
            alert.showAndWait();
            return;
        }
        
        // Create a dialog window
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.setTitle("Assign Final Grade for " + selectedStudent.getFullName());
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        
        // Create the dialog content
        VBox dialogVbox = new VBox(10);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        
        // Course and student info
        Label infoLabel = new Label("Course: " + selectedCourse.getCourseName() + 
                " | Student: " + selectedStudent.getFullName());
        infoLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        
        // Current average
        double average = selectedCourse.calculateStudentAverage(selectedStudent);
        Label averageLabel = new Label("Current Average: " + 
                (average >= 0 ? String.format("%.2f%%", average) : "N/A"));
        
        // Final grade selection
        Label gradeLabel = new Label("Select Final Grade:");
        
        ComboBox<FinalGrade> gradeComboBox = new ComboBox<>();
        gradeComboBox.getItems().addAll(
                FinalGrade.A, FinalGrade.B, FinalGrade.C, 
                FinalGrade.D, FinalGrade.E
        );
        
        // If the student already has a final grade, select it
        FinalGrade currentGrade = selectedCourse.getFinalGrade(selectedStudent);
        if (currentGrade != null) {
            gradeComboBox.setValue(currentGrade);
        } else {
            // Suggest a grade based on the average
            if (average >= 90) {
                gradeComboBox.setValue(FinalGrade.A);
            } else if (average >= 80) {
                gradeComboBox.setValue(FinalGrade.B);
            } else if (average >= 70) {
                gradeComboBox.setValue(FinalGrade.C);
            } else if (average >= 60) {
                gradeComboBox.setValue(FinalGrade.D);
            } else {
                gradeComboBox.setValue(FinalGrade.E);
            }
        }
        
        // Buttons
        Button saveButton = new Button("Save Grade");
        Button cancelButton = new Button("Cancel");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(cancelButton, saveButton);
        
        // Add components to dialog
        dialogVbox.getChildren().addAll(infoLabel, averageLabel, gradeLabel, gradeComboBox, buttonBox);
        
        // Create the scene
        Scene dialogScene = new Scene(dialogVbox, 400, 250);
        dialogStage.setScene(dialogScene);
        
        // Set button actions
        cancelButton.setOnAction(e -> dialogStage.close());
        
        saveButton.setOnAction(e -> {
            FinalGrade selectedGrade = gradeComboBox.getValue();
            
            if (selectedGrade != null) {
                // Assign the final grade
                controller.assignFinalGrade(selectedCourse, selectedStudent, selectedGrade);
                
                // Close the dialog
                dialogStage.close();
                
                // Update the student table to reflect the new grade
                updateStudentTable(selectedCourse);
                
                // Force the table to refresh
                studentTable.refresh();
                
                // Show success message
                javafx.scene.control.Alert successAlert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Final Grade Assigned");
                successAlert.setContentText("Final grade has been successfully assigned to " + 
                        selectedStudent.getFullName() + ".");
                successAlert.showAndWait();
            }
        });
        
        // Show the dialog
        dialogStage.showAndWait();
    }
    
    /**
     * Shows a dialog for assigning grades to students for a selected assignment.
     */
    private void showAssignGradeDialog() {
        Course selectedCourse = courseSelector.getValue();
        Assignment selectedAssignment = assignmentTable.getSelectionModel().getSelectedItem();
        
        if (selectedCourse == null || selectedAssignment == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Course or Assignment Selected");
            alert.setContentText("Please select a course and an assignment first.");
            alert.showAndWait();
            return;
        }
        
        // Get students from course
        List<Student> students = controller.getStudentsForCourse(selectedCourse);
        if (students.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Students in Course");
            alert.setContentText("This course has no students to grade.");
            alert.showAndWait();
            return;
        }
        
        // Create a dialog window
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.setTitle("Assign Grades for " + selectedAssignment.getTitle());
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        
        // Create the dialog content
        VBox dialogVbox = new VBox(10);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        
        // Assignment info
        Label assignmentInfoLabel = new Label("Assignment: " + selectedAssignment.getTitle() + 
                " (Max Points: " + selectedAssignment.getMaxPoints() + ")");
        assignmentInfoLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        
        // Student grades grid
        GridPane gradeGrid = new GridPane();
        gradeGrid.setHgap(15);
        gradeGrid.setVgap(10);
        gradeGrid.setPadding(new Insets(10, 0, 10, 0));
        
        // Column headers
        Label nameHeader = new Label("Student Name");
        nameHeader.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gradeGrid.add(nameHeader, 0, 0);
        
        Label pointsHeader = new Label("Points");
        pointsHeader.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gradeGrid.add(pointsHeader, 1, 0);
        
        Label statusHeader = new Label("Status");
        statusHeader.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        gradeGrid.add(statusHeader, 2, 0);
        
        // Create a map to store text fields for each student
        java.util.Map<Student, TextField> gradeFields = new java.util.HashMap<>();
        java.util.Map<Student, Label> statusLabels = new java.util.HashMap<>();
        
        // Add students and grade fields
        int row = 1;
        for (Student student : students) {
            // Student name
            Label nameLabel = new Label(student.getFullName());
            gradeGrid.add(nameLabel, 0, row);
            
            // Grade field
            TextField gradeField = new TextField();
            gradeField.setPrefWidth(80);
            
            // If student already has a grade, show it
            Grade existingGrade = student.getGrade(selectedAssignment);
            if (existingGrade != null) {
                gradeField.setText(String.valueOf(existingGrade.getPointsReceived()));
            }
            
            gradeGrid.add(gradeField, 1, row);
            gradeFields.put(student, gradeField);
            
            // Status label
            Label statusLabel = new Label(existingGrade != null ? "Graded" : "Not Graded");
            statusLabel.setTextFill(existingGrade != null ? Color.GREEN : Color.RED);
            gradeGrid.add(statusLabel, 2, row);
            statusLabels.put(student, statusLabel);
            
            row++;
        }
        
        // Add scrolling for many students
        javafx.scene.control.ScrollPane scrollPane = new javafx.scene.control.ScrollPane(gradeGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(300);
        
        // Error label
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        errorLabel.setVisible(false);
        
        // Buttons
        Button saveButton = new Button("Save Grades");
        Button cancelButton = new Button("Cancel");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(cancelButton, saveButton);
        
        // Add components to dialog
        dialogVbox.getChildren().addAll(assignmentInfoLabel, scrollPane, errorLabel, buttonBox);
        
        // Create the scene
        Scene dialogScene = new Scene(dialogVbox, 500, 400);
        dialogStage.setScene(dialogScene);
        
        // Set button actions
        cancelButton.setOnAction(e -> dialogStage.close());
        
        saveButton.setOnAction(e -> {
            boolean hasError = false;
            
            // Process each student's grade
            for (Student student : students) {
                TextField field = gradeFields.get(student);
                String gradeText = field.getText().trim();
                
                if (gradeText.isEmpty()) {
                    // Skip empty grades
                    continue;
                }
                
                try {
                    double score = Double.parseDouble(gradeText);
                    
                    // Validate score
                    if (score < 0) {
                        field.setStyle("-fx-border-color: red;");
                        errorLabel.setText("Points cannot be negative");
                        errorLabel.setVisible(true);
                        hasError = true;
                        continue;
                    }
                    
                    if (score > selectedAssignment.getMaxPoints()) {
                        field.setStyle("-fx-border-color: red;");
                        errorLabel.setText("Points cannot exceed max points (" + 
                                selectedAssignment.getMaxPoints() + ")");
                        errorLabel.setVisible(true);
                        hasError = true;
                        continue;
                    }
                    
                    // Reset error styling
                    field.setStyle("");
                    
                    // Assign the grade
                    controller.assignGrade(selectedCourse, student, selectedAssignment, score);
                    
                    // Update status
                    Label statusLabel = statusLabels.get(student);
                    statusLabel.setText("Graded");
                    statusLabel.setTextFill(Color.GREEN);
                    
                } catch (NumberFormatException ex) {
                    // Invalid number
                    field.setStyle("-fx-border-color: red;");
                    errorLabel.setText("Points must be valid numbers");
                    errorLabel.setVisible(true);
                    hasError = true;
                }
            }
            
            if (!hasError) {
                // Close the dialog if there are no errors
                dialogStage.close();
                
                // Update the assignment table
                updateAssignmentTable(selectedCourse);
                
                // Update student table to reflect new averages
                updateStudentTable(selectedCourse);
                
                // Force refresh tables to ensure UI updates
                studentTable.refresh();
                assignmentTable.refresh();
                
                // Update class average
                updateClassAverage(selectedCourse);
            }
        });
        
        // Show the dialog
        dialogStage.showAndWait();
    }
    
    /**
     * Shows a dialog for removing a student from a course.
     */
    private void showRemoveStudentDialog() {
        Course selectedCourse = courseSelector.getValue();
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        
        if (selectedCourse == null || selectedStudent == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Course or Student Selected");
            alert.setContentText("Please select a course and a student first.");
            alert.showAndWait();
            return;
        }
        
        // Confirmation dialog
        javafx.scene.control.Alert confirmDialog = new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirm Removal");
        confirmDialog.setHeaderText("Remove Student from Course");
        confirmDialog.setContentText("Are you sure you want to remove " + 
                selectedStudent.getFullName() + " from " + selectedCourse.getCourseName() + "?");
        
        java.util.Optional<javafx.scene.control.ButtonType> result = confirmDialog.showAndWait();
        
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            // Remove the student from the course
            boolean success = controller.removeStudentFromCourse(selectedStudent, selectedCourse);
            
            if (success) {
                // Update student table
                updateStudentTable(selectedCourse);
                
                // Update class average
                updateClassAverage(selectedCourse);
                
                // Show success message
                javafx.scene.control.Alert successAlert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText("Student Removed");
                successAlert.setContentText("Student has been removed from the course.");
                successAlert.showAndWait();
            } else {
                // Show error message
                javafx.scene.control.Alert errorAlert = new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Removal Failed");
                errorAlert.setContentText("Failed to remove student from course.");
                errorAlert.showAndWait();
            }
        }
    }
    
    /**
     * Shows a dialog for editing an assignment in a course.
     */
    private void showEditAssignmentDialog() {
        Course selectedCourse = courseSelector.getValue();
        Assignment selectedAssignment = assignmentTable.getSelectionModel().getSelectedItem();
        
        if (selectedCourse == null || selectedAssignment == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Course or Assignment Selected");
            alert.setContentText("Please select a course and an assignment first.");
            alert.showAndWait();
            return;
        }
        
        // Create a dialog window
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.setTitle("Edit Assignment");
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        
        // Create the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));
        
        // Assignment title
        Label titleLabel = new Label("Title:");
        grid.add(titleLabel, 0, 0);
        
        TextField titleField = new TextField(selectedAssignment.getTitle());
        grid.add(titleField, 1, 0);
        
        // Max points
        Label pointsLabel = new Label("Max Points:");
        grid.add(pointsLabel, 0, 1);
        
        TextField pointsField = new TextField(String.valueOf(selectedAssignment.getMaxPoints()));
        grid.add(pointsField, 1, 1);
        
        // Category (optional)
        Label categoryLabel = new Label("Category (optional):");
        grid.add(categoryLabel, 0, 2);
        
        TextField categoryField = new TextField(selectedAssignment.getCategory());
        grid.add(categoryField, 1, 2);
        
        // Buttons
        Button saveButton = new Button("Save Changes");
        Button cancelButton = new Button("Cancel");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(cancelButton, saveButton);
        grid.add(buttonBox, 0, 4, 2, 1);
        
        // Error label
        Label errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        grid.add(errorLabel, 0, 3, 2, 1);
        errorLabel.setVisible(false);
        
        // Create the scene
        Scene dialogScene = new Scene(grid, 400, 250);
        dialogStage.setScene(dialogScene);
        
        // Set button actions
        cancelButton.setOnAction(e -> dialogStage.close());
        
        saveButton.setOnAction(e -> {
            String title = titleField.getText().trim();
            String pointsText = pointsField.getText().trim();
            String category = categoryField.getText().trim();
            
            // Validate input
            if (title.isEmpty()) {
                errorLabel.setText("Title cannot be empty");
                errorLabel.setVisible(true);
                return;
            }
            
            double maxPoints;
            try {
                maxPoints = Double.parseDouble(pointsText);
                if (maxPoints <= 0) {
                    errorLabel.setText("Max points must be greater than 0");
                    errorLabel.setVisible(true);
                    return;
                }
            } catch (NumberFormatException ex) {
                errorLabel.setText("Max points must be a valid number");
                errorLabel.setVisible(true);
                return;
            }
            
            // Check if any actual changes were made
            boolean titleChanged = !title.equals(selectedAssignment.getTitle());
            boolean pointsChanged = maxPoints != selectedAssignment.getMaxPoints();
            boolean categoryChanged = !category.equals(selectedAssignment.getCategory() != null ? 
                    selectedAssignment.getCategory() : "");
            
            if (!titleChanged && !pointsChanged && !categoryChanged) {
                // No changes made, just close the dialog
                dialogStage.close();
                return;
            }
            
            // Use null if category is not provided
            if (category.isEmpty()) {
                category = null;
            }
            
            // Update the assignment
            boolean success = controller.updateAssignment(selectedCourse, selectedAssignment, 
                    title, maxPoints, category);
            
            if (success) {
                // Update assignment table
                updateAssignmentTable(selectedCourse);
                
                // Close the dialog
                dialogStage.close();
                
                // Update student table to reflect new averages (in case max points changed)
                updateStudentTable(selectedCourse);
                
                // Update class average
                updateClassAverage(selectedCourse);
            } else {
                errorLabel.setText("Failed to update assignment");
                errorLabel.setVisible(true);
            }
        });
        
        // Show the dialog
        dialogStage.showAndWait();
    }
    
    /**
     * Shows a dialog for sorting students by grade on a specific assignment.
     */
    private void showSortByGradeDialog() {
        Course selectedCourse = courseSelector.getValue();
        
        if (selectedCourse == null) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Course Selected");
            alert.setContentText("Please select a course first.");
            alert.showAndWait();
            return;
        }
        
        List<Assignment> assignments = controller.getAssignmentsForCourse(selectedCourse);
        if (assignments.isEmpty()) {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(
                    javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("No Assignments in Course");
            alert.setContentText("This course has no assignments to sort by.");
            alert.showAndWait();
            return;
        }
        
        // Create a dialog window
        javafx.stage.Stage dialogStage = new javafx.stage.Stage();
        dialogStage.setTitle("Sort Students by Grade");
        dialogStage.initModality(javafx.stage.Modality.WINDOW_MODAL);
        
        // Create the dialog content
        VBox dialogVbox = new VBox(10);
        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        
        // Assignment selection
        Label selectionLabel = new Label("Select Assignment:");
        selectionLabel.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        
        ComboBox<Assignment> assignmentComboBox = new ComboBox<>();
        assignmentComboBox.setItems(FXCollections.observableArrayList(assignments));
        assignmentComboBox.setPromptText("Select an assignment");
        
        // Custom cell factory to display assignment titles
        assignmentComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<Assignment>() {
            @Override
            protected void updateItem(Assignment item, boolean empty) {
                super.updateItem(item, empty);
                
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getTitle());
                }
            }
        });
        
        // Custom string converter for the selected value
        assignmentComboBox.setConverter(new javafx.util.StringConverter<Assignment>() {
            @Override
            public String toString(Assignment assignment) {
                if (assignment == null) {
                    return "";
                }
                return assignment.getTitle();
            }
            
            @Override
            public Assignment fromString(String string) {
                return null; // Not needed for ComboBox
            }
        });
        
        // Buttons
        Button sortButton = new Button("Sort");
        Button cancelButton = new Button("Cancel");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(cancelButton, sortButton);
        
        // Add components to dialog
        dialogVbox.getChildren().addAll(selectionLabel, assignmentComboBox, buttonBox);
        
        // Create the scene
        Scene dialogScene = new Scene(dialogVbox, 400, 200);
        dialogStage.setScene(dialogScene);
        
        // Set button actions
        cancelButton.setOnAction(e -> dialogStage.close());
        
        sortButton.setOnAction(e -> {
            Assignment selectedAssignment = assignmentComboBox.getValue();
            
            if (selectedAssignment != null) {
                // Sort students by grade on the selected assignment
                List<Student> sortedStudents = controller.sortStudentsByAssignmentGrade(
                        selectedCourse, selectedAssignment);
                
                // Update student table with the sorted list
                updateStudentTable(sortedStudents);
                
                // Close the dialog
                dialogStage.close();
            }
        });
        
        // Show the dialog
        dialogStage.showAndWait();
    }
    
    /**
     * Updates the student table with students from a course.
     *
     * @param course the course to display students from
     */
    private void updateStudentTable(Course course) {
        studentTable.setItems(FXCollections.observableArrayList(controller.getStudentsForCourse(course)));
    }
    
    /**
     * Updates the student table with a specific list of students.
     *
     * @param students the students to display
     */
    private void updateStudentTable(List<Student> students) {
        studentTable.setItems(FXCollections.observableArrayList(students));
    }
    
    /**
     * Updates the assignment table with assignments from a course.
     *
     * @param course the course to display assignments from
     */
    private void updateAssignmentTable(Course course) {
        assignmentTable.setItems(FXCollections.observableArrayList(controller.getAssignmentsForCourse(course)));
    }
    
    /**
     * Updates the class average label.
     *
     * @param course the course to calculate average for
     */
    private void updateClassAverage(Course course) {
        double average = controller.calculateClassAverage(course);
        classAverageLabel.setText("Class Average: " + String.format("%.2f", average));
    }
    
    /**
     * Gets the scene for this view.
     *
     * @return the dashboard scene
     */
    public Scene getScene() {
        return scene;
    }
} 