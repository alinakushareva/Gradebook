/**
 * Project Name: Gradebook
 * File Name: GradePopupView.java
 * Course: CSC 335 Spring 2025
 *
* Purpose:
 * This class provides a popup window for teachers to view and edit assignment grades for students.
 * It allows entering or updating grades, sorting students by score, and submitting all pending grades.
 * Grades are stored temporarily until submitted and updates trigger observer notifications.
 * AI generated class!
 */
package view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.*;

import java.util.*;

public class GradePopupView {

    private static final Map<Student, Double> tempGrades = new HashMap<>();

    /**
     * Displays the grade popup for the selected assignment and course.
     * Allows grade editing, sorting, and submission.
     *
     * @param course The course containing the assignment
     * @param assignment The assignment being graded
     */
    public static void showPopup(Course course, Assignment assignment) {
        Stage popup = new Stage(); // New window for the popup
        popup.setTitle("Grade Assignment: " + assignment.getTitle());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        // List of students and their grades
        ListView<String> studentGradeList = new ListView<>();
        refreshList(course, assignment, studentGradeList);

        // Button to edit selected student's grade
        Button editGradeBtn = new Button("Edit Grade");
        editGradeBtn.setOnAction(e -> {
            String selected = studentGradeList.getSelectionModel().getSelectedItem();
            if (selected == null || !selected.contains("(")) return;

            // Extract student username from selected line
            String username = selected.substring(selected.indexOf("(") + 1, selected.indexOf(")"));
            Student student = course.getStudents().stream()
                    .filter(s -> s.getUsername().equals(username))
                    .findFirst().orElse(null);
            if (student == null) return;

            // Dialog to input new grade
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit Grade");
            dialog.setHeaderText("Set new grade for " + student.getFirstName());
            dialog.setContentText("Points (max: " + assignment.getMaxPoints() + "):");

            dialog.showAndWait().ifPresent(input -> {
                try {
                    double score = Double.parseDouble(input);
                    // Validate score range
                    if (score >= 0 && score <= assignment.getMaxPoints()) {
                        tempGrades.put(student, score); // Temporarily store grade
                        refreshList(course, assignment, studentGradeList); // Update UI
                    }
                } catch (NumberFormatException ignored) {}
                // Silently ignore invalid inputs
            });
        });

        // Button to sort the list of students by their current grade
        Button sortBtn = new Button("Sort by Grade");
        sortBtn.setOnAction(e -> {
            // Sort students in descending order of their grade for the selected assignment
            List<Student> sorted = course.getStudents().stream()
                .sorted(Comparator.comparingDouble((Student s) -> {
                    Grade g = assignment.getGrade(s);
                    return g != null ? g.getPointsReceived() : 0.0;
                }).reversed()) // Sort descending
                .toList();

            // Format the sorted list for display in the ListView
            studentGradeList.getItems().setAll(
                sorted.stream()
                    .map(s -> {
                        Grade g = assignment.getGrade(s);
                        String entry = s.getFirstName() + " " + s.getLastName() + " (" + s.getUsername() + ") - ";
                        // Show either the grade or mark as Ungraded
                        return entry + (g != null ? g.getPointsReceived() + "/" + assignment.getMaxPoints() : "Ungraded");
                    })
                    .toList()
            );
        });
        
        // Submit button applies all pending grade changes
        Button submitBtn = new Button("✅ Submit Grades");
        submitBtn.setOnAction(e -> {
            // Loop through each pending grade and apply it to the assignment
            for (Map.Entry<Student, Double> entry : tempGrades.entrySet()) {
                assignment.assignGrade(entry.getKey(), entry.getValue());
            }
            tempGrades.clear(); // Clear temporary storage after commit
            course.notifyObservers(); // Trigger update in AssignmentManagementView
            popup.close();
        });

        // Add components to layout
        layout.getChildren().addAll(
            new Label("Students & Grades:"),
            studentGradeList,
            new HBox(10, editGradeBtn, sortBtn, submitBtn)
        );
        // Display the popup window with specified dimensions
        popup.setScene(new Scene(layout, 420, 420));
        popup.show();
    }

    /**
     * Refreshes the student grade list display in the popup.
     *
     * @param course The course whose students are shown
     * @param assignment The assignment being viewed/graded
     * @param listView The ListView component to update
     */
    private static void refreshList(Course course, Assignment assignment, ListView<String> listView) {
        listView.getItems().clear();
        // Iterate through students and show either saved or pending grade
        for (Student s : course.getStudents()) {
            String entry = s.getFirstName() + " " + s.getLastName() + " (" + s.getUsername() + ") - ";
            // If a grade is temporarily stored (not submitted yet), show it as (Pending)
            if (tempGrades.containsKey(s)) {
                entry += tempGrades.get(s) + "/" + assignment.getMaxPoints() + " (Pending)";
            } else {
                // Otherwise, show the actual submitted grade or mark as Ungraded
                Grade g = assignment.getGrade(s);
                entry += g != null ? g.getPointsReceived() + "/" + assignment.getMaxPoints() : "Ungraded";
            }
            // Add the constructed string to the ListView for display
            listView.getItems().add(entry);
        }
    }
}
