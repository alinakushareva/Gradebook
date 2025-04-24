/**
 * Project Name: Gradebook
 * File Name: CourseDetailView.java
 * Course: CSC 335 Spring 2025
 * 
 * Purpose:
 * Displays and manages courses for a teacher. Allows creating courses, setting up 
 * assignment categories with weights, and viewing categorized lists of current vs. 
 * completed courses. Reacts to updates via the Observer interface.
 * AI generated class!
 */
package view;

import controller.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailView implements Observer {
    private final Scene scene;
    private final ListView<String> currentList;
    private final ListView<String> completedList;
    private final GradebookModel model;
    private final Teacher teacher;

    /**
     * Constructs the course management view for the teacher.
     *
     * @param teacher the current teacher
     * @param model the data model
     * @param controller controller (used for back navigation)
     * @param mainView main view object for scene switching
     */
    public CourseDetailView(Teacher teacher, GradebookModel model, MainController controller, MainView mainView) {
        this.model = model;
        this.teacher = teacher;
        this.model.addObserver(this);

        // Main vertical layout container
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        // Title header for the view
        Label header = new Label("📚 Manage Your Courses");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Input field for entering a new course name
        TextField courseNameField = new TextField();
        courseNameField.setPromptText("New Course Name");
        Button addCourseBtn = new Button("Add Course");

        // Layout for input and button in a horizontal row
        HBox createBox = new HBox(10, courseNameField, addCourseBtn);
        createBox.setAlignment(Pos.CENTER_LEFT);

        // Label and list for currently active courses
        Label currentLabel = new Label("Current Courses:");
        currentList = new ListView<>();

        // Label and list for completed courses
        Label completedLabel = new Label("Completed Courses:");
        completedList = new ListView<>();

        refreshCourseLists(); // Populate both course lists initially

        // Button action: Add course with category setup
        addCourseBtn.setOnAction(e -> {
            String name = courseNameField.getText().trim();
            if (!name.isEmpty() && model.getCourseByName(name) == null) {
                Course course = new Course(name); // Create new course object

                // Open dialog to define assignment categories for the course
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Set Categories for " + name);
                dialog.setHeaderText("Define assignment categories and weights (must total 100%)");

                // Container for category rows
                VBox categoryBox = new VBox(10);
                // Scrollable view in case many categories are added
                ScrollPane scrollPane = new ScrollPane(categoryBox);
                scrollPane.setFitToWidth(true);

                // Button to allow user to add a category input row
                Button addCategoryBtn = new Button("+ Add Category");
                categoryBox.getChildren().add(addCategoryBtn);

                List<HBox> categoryRows = new ArrayList<>(); // Store category rows for later access

                // When + Add Category is clicked
                addCategoryBtn.setOnAction(event -> {
                    TextField catName = new TextField();
                    catName.setPromptText("Category Name");
                    
                    // Spinner input for weight percentage (0 to 100%)
                    Spinner<Double> weight = new Spinner<>(0.0, 100.0, 10.0, 1.0);
                    weight.setEditable(true);
                    
                    // Create a horizontal row for this category input
                    HBox row = new HBox(10, new Label("Name:"), catName, new Label("Weight:"), weight);
                    categoryRows.add(row);
                    categoryBox.getChildren().add(row);
                });

                // Set the dialog's scrollable content
                dialog.getDialogPane().setContent(scrollPane);
                // Add OK and Cancel buttons to dialog
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                // Validate and save course on confirmation
                dialog.setResultConverter(btn -> {
                    if (btn == ButtonType.OK) {
                        double total = 0; // To track the sum of all category weights
                        // Iterate over each category input row the user added
                        for (HBox row : categoryRows) {
                            TextField nameField = (TextField) row.getChildren().get(1);
                            Spinner<?> weightSpinner = (Spinner<?>) row.getChildren().get(3);
                            // Only process non-empty category names
                            if (!nameField.getText().trim().isEmpty()) {
                                String catName = nameField.getText().trim();
                                double catWeight = (Double) weightSpinner.getValue();
                                
                                total += catWeight; // Add to running total of weights
                                
                                // Add this category to the course, converting weight from percent to decimal (e.g. 20% → 0.20)
                                course.addCategory(new Category(catName, catWeight / 100.0));
                            }
                        }
                        if (Math.abs(total - 100.0) > 0.01) {
                            // Ensure category weights sum to 100%
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Category weights must sum to 100%", ButtonType.OK);
                            alert.showAndWait();
                            return null;
                        }
                        teacher.addCourse(course); // Add to teacher
                        model.addCourse(course);  // Add to model
                        courseNameField.clear(); // Reset input
                    }
                    return null;
                });

                dialog.showAndWait(); // Launch the dialog
            }
        });
        
        // Return to dashboard
        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> mainView.setScene(new TeacherDashboardView(teacher, model, controller, mainView).getScene()));

        content.getChildren().addAll(header, createBox, currentLabel, currentList, completedLabel, completedList, backBtn);
        content.setAlignment(Pos.TOP_LEFT);

        this.scene = new Scene(content, 650, 600);
    }

    /**
     * Called when model data is updated.
     */
    @Override
    public void update() {
        refreshCourseLists();
    }

    /**
     * Refreshes the lists of current and completed courses based on student grading.
     */
    private void refreshCourseLists() {
        currentList.getItems().clear();
        completedList.getItems().clear();

        // Loop through teacher's courses
        for (Course c : teacher.getCourses()) {
            boolean allGraded = !c.getStudents().isEmpty(); // Ensure course has students
            // Check if all students in course have final grades
            for (Student s : c.getStudents()) {
                if (s.getFinalGrade(c) == null) {
                    allGraded = false;
                    break;
                }
            }
            // Add to appropriate list based on grade completion
            if (allGraded && !c.getStudents().isEmpty()) {
                completedList.getItems().add(c.getCourseName());
            } else {
                currentList.getItems().add(c.getCourseName());
            }
        }
        // Fallback if no courses
        if (currentList.getItems().isEmpty()) currentList.getItems().add("No current courses yet");
        if (completedList.getItems().isEmpty()) completedList.getItems().add("No completed courses yet");
    }

    /**
     * Returns the UI scene associated with this view.
     *
     * @return the JavaFX scene
     */
    public Scene getScene() {
        return scene;
    }
}
