/**
 * Project Name: Gradebook
 * File Name: StudentManagementView.java
 * Course: CSC 335 Spring 2025
 *
 * Purpose:
 * Provides a user interface for teachers to manage student enrollment in courses.
 * Includes functionality to import, add, remove, sort, and group students,
 * all while observing changes in the gradebook model.
 * AI generated class!
 */
package view;

import controller.MainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;
import model.Observer;
import util.FileUtil;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class StudentManagementView implements Observer {
    private final Scene scene;
    private final GradebookModel model;
    private final Teacher teacher;
    private final MainView mainView;
    private final MainController controller;
    private final ListView<String> studentListView;
    private final ComboBox<String> courseCombo;
    private Course selectedCourse;

    /**
     * Constructs the StudentManagementView.
     *
     * @param teacher the logged-in teacher
     * @param model the gradebook model
     * @param controller the main controller
     * @param mainView the view navigation handler
     */
    public StudentManagementView(Teacher teacher, GradebookModel model, MainController controller, MainView mainView) {
        this.model = model;
        this.teacher = teacher;
        this.controller = controller;
        this.mainView = mainView;
        this.model.addObserver(this);

        // Main vertical layout container
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));

        // Title label for the page
        Label title = new Label("👥 Manage Students");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Course selector dropdown (shows all courses taught by this teacher)
        courseCombo = new ComboBox<>();
        for (Course c : teacher.getCourses()) {
            courseCombo.getItems().add(c.getCourseName()); // Add each course name to dropdown
        }

        // When a course is selected, update the selectedCourse and student list
        courseCombo.setOnAction(e -> {
            String selected = courseCombo.getValue();
            selectedCourse = model.getCourseByName(selected);
            refreshStudentList(); // Update list for selected course
        });

        // List of students for the selected course
        studentListView = new ListView<>();
        
        // Buttons for importing, adding, removing, and grouping students
        Button importBtn = new Button("Import Students");
        Button addStudentBtn = new Button("Add Student");
        Button removeBtn = new Button("Remove Selected Student");
        Button groupBtn = new Button("Group Students");

        // ComboBox to select sorting method for student list
        ComboBox<String> sortCombo = new ComboBox<>(FXCollections.observableArrayList("First Name", "Last Name", "Username"));
        sortCombo.setPromptText("Sort By");

        // Sorting logic for student list based on selected attribute
        sortCombo.setOnAction(e -> {
            if (selectedCourse == null) return;
            // Choose comparator based on sort option selected
            Comparator<Student> comparator = switch (sortCombo.getValue()) {
                case "First Name" -> Comparator.comparing(Student::getFirstName);
                case "Last Name" -> Comparator.comparing(Student::getLastName);
                case "Username" -> Comparator.comparing(Student::getUsername);
                default -> null;
            };
            // If a valid sort was selected, apply it and update the list view
            if (comparator != null) {
                List<Student> sorted = selectedCourse.getStudents().stream().sorted(comparator).toList();
                studentListView.getItems().setAll(
                        sorted.stream()
                                .map(s -> s.getFirstName() + " " + s.getLastName() + " (" + s.getUsername() + ")")
                                .toList());
            }
        });

        // Logic for importing students from file who are not already in the selected course
        importBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            try {
                // Get usernames of currently enrolled students
                Set<String> existingUsernames = selectedCourse.getStudents().stream()
                        .map(Student::getUsername)
                        .collect(Collectors.toSet());
                // Load all users, filter to new students not yet enrolled
                List<Student> students = FileUtil.loadUsers("users.txt").stream()
                        .filter(u -> u instanceof Student)
                        .map(u -> (Student) u)
                        .filter(s -> !existingUsernames.contains(s.getUsername()))
                        .toList();

                // Enroll each new student into the course
                for (Student s : students) {
                    selectedCourse.addStudent(s);
                }
                // Refresh the student list view
                refreshStudentList();
            } catch (IOException ex) {
                ex.printStackTrace(); // Handle file reading errors
            }
        });

        // Add registered student logic
        addStudentBtn.setOnAction(e -> {
            if (selectedCourse == null) return;

            List<Student> allStudents;
            try {
                // Load all registered users, filter to only students
                allStudents = FileUtil.loadUsers("users.txt").stream()
                        .filter(u -> u instanceof Student)
                        .map(u -> (Student) u)
                        .filter(s -> !selectedCourse.getStudents().contains(s)) // Only those not yet enrolled
                        .toList();
            } catch (IOException ex) {
                ex.printStackTrace(); // Handle error if file read fails
                return;
            }

            // If there are no additional students to add, show alert
            if (allStudents.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No more registered students to add.").showAndWait();
                return;
            }

            // Create dialog to allow the teacher to pick a student from list
            Dialog<Student> dialog = new Dialog<>();
            dialog.setTitle("Add Registered Student");
            dialog.setHeaderText("Select a registered student:");

            // Dropdown containing all available students to add
            ComboBox<Student> studentBox = new ComboBox<>();
            studentBox.getItems().addAll(allStudents);
            
            // Custom text shown in ComboBox
            studentBox.setConverter(new javafx.util.StringConverter<>() {
                @Override
                public String toString(Student s) {
                    return s == null ? "" : s.getFirstName() + " " + s.getLastName() + " (" + s.getUsername() + ")";
                }

                @Override
                public Student fromString(String string) {
                    return null;
                }
            });

            // Layout and appearance of the dialog
            VBox dialogBox = new VBox(10, new Label("Student:"), studentBox);
            dialogBox.setPadding(new Insets(10));
            dialog.getDialogPane().setContent(dialogBox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Define what to do when OK is clicked — return the selected student
            dialog.setResultConverter(btn -> btn == ButtonType.OK ? studentBox.getValue() : null);

            // If a student is selected and OK was clicked, add them to the course
            dialog.showAndWait().ifPresent(s -> {
                selectedCourse.addStudent(s);
                refreshStudentList(); // Refresh UI with new student
            });
        });

        // Remove student button
        removeBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            
            // Get selected string from the list view
            String selected = studentListView.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            // Extract username from string format "Name (username)"
            String username = selected.substring(selected.indexOf("(") + 1, selected.indexOf(")"));
            // Look up student by username and remove them
            Student s = model.getStudentByUsername(username);
            if (s != null) {
                selectedCourse.removeStudent(s); // Remove from course
                refreshStudentList();
            }
        });

        // Group students logic
        groupBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            int groupSize = 2; // Group size is hardcoded
            List<Student> all = selectedCourse.getStudents();
            StringBuilder groups = new StringBuilder(); // Holds formatted output of groups
            for (int i = 0; i < all.size(); i += groupSize) {
                int end = Math.min(i + groupSize, all.size()); // Handle last group with < groupSize students
                List<Student> group = all.subList(i, end); // Create sublist for the group
                groups.append("Group ").append(i / groupSize + 1).append(": "); // Group header
                for (Student s : group) {
                    // Append full name of each student in group
                    groups.append(s.getFirstName()).append(" ").append(s.getLastName()).append(", ");
                }
                groups.setLength(groups.length() - 2); // Remove trailing comma
                groups.append("\n");
            }
            // Show group info in an alert dialog
            new Alert(Alert.AlertType.INFORMATION, groups.toString()).show();
        });

        // Back button logic
        Button back = new Button("← Back");
        // Navigate back to the teacher dashboard
        back.setOnAction(e -> mainView.setScene(new TeacherDashboardView(teacher, model, controller, mainView).getScene()));

        // Add all controls to layout
        root.getChildren().addAll(
                title, courseCombo, studentListView, sortCombo,
                importBtn, addStudentBtn, removeBtn, groupBtn, back);
        root.setAlignment(Pos.TOP_LEFT); // Align all controls to top left

        this.scene = new Scene(root, 650, 600); // Set scene with complete layout
    }

    /**
     * Updates the list view with students from the currently selected course.
     */
    private void refreshStudentList() {
        if (selectedCourse == null) return;
        List<Student> students = selectedCourse.getStudents();
        // Format: "First Last (username)" for display
        ObservableList<String> items = FXCollections.observableArrayList(
                students.stream()
                        .map(s -> s.getFirstName() + " " + s.getLastName() + " (" + s.getUsername() + ")")
                        .collect(Collectors.toList()));
        studentListView.setItems(items); // Update the UI list
        if (items.isEmpty()) {
            // Display message when no students are enrolled
            studentListView.getItems().add("No students enrolled");
        }
    }

    /**
     * Responds to observable model changes by updating the student list.
     */
    @Override
    public void update() {
        refreshStudentList();
    }

    /**
     * Returns the current scene for student management.
     *
     * @return the JavaFX scene
     */
    public Scene getScene() {
        return scene;
    }
}