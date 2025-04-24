// GradePopupView.java
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

    public static void showPopup(Course course, Assignment assignment) {
        Stage popup = new Stage();
        popup.setTitle("Grade Assignment: " + assignment.getTitle());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));

        ListView<String> studentGradeList = new ListView<>();
        refreshList(course, assignment, studentGradeList);

        Button editGradeBtn = new Button("Edit Grade");
        editGradeBtn.setOnAction(e -> {
            String selected = studentGradeList.getSelectionModel().getSelectedItem();
            if (selected == null || !selected.contains("(")) return;

            String username = selected.substring(selected.indexOf("(") + 1, selected.indexOf(")"));
            Student student = course.getStudents().stream()
                    .filter(s -> s.getUsername().equals(username))
                    .findFirst().orElse(null);
            if (student == null) return;

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit Grade");
            dialog.setHeaderText("Set new grade for " + student.getFirstName());
            dialog.setContentText("Points (max: " + assignment.getMaxPoints() + "):");

            dialog.showAndWait().ifPresent(input -> {
                try {
                    double score = Double.parseDouble(input);
                    if (score >= 0 && score <= assignment.getMaxPoints()) {
                        tempGrades.put(student, score);
                        refreshList(course, assignment, studentGradeList);
                    }
                } catch (NumberFormatException ignored) {}
            });
        });

        Button sortBtn = new Button("Sort by Grade");
        sortBtn.setOnAction(e -> {
            List<Student> sorted = course.getStudents().stream()
                .sorted(Comparator.comparingDouble((Student s) -> {
                    Grade g = assignment.getGrade(s);
                    return g != null ? g.getPointsReceived() : 0.0;
                }).reversed())
                .toList();

            studentGradeList.getItems().setAll(
                sorted.stream()
                    .map(s -> {
                        Grade g = assignment.getGrade(s);
                        String entry = s.getFirstName() + " " + s.getLastName() + " (" + s.getUsername() + ") - ";
                        return entry + (g != null ? g.getPointsReceived() + "/" + assignment.getMaxPoints() : "Ungraded");
                    })
                    .toList()
            );
        });


        Button submitBtn = new Button("✅ Submit Grades");
        submitBtn.setOnAction(e -> {
            for (Map.Entry<Student, Double> entry : tempGrades.entrySet()) {
                assignment.assignGrade(entry.getKey(), entry.getValue());
            }
            tempGrades.clear();
            course.notifyObservers(); // Trigger update in AssignmentManagementView
            popup.close();
        });

        layout.getChildren().addAll(
            new Label("Students & Grades:"),
            studentGradeList,
            new HBox(10, editGradeBtn, sortBtn, submitBtn)
        );

        popup.setScene(new Scene(layout, 420, 420));
        popup.show();
    }

    private static void refreshList(Course course, Assignment assignment, ListView<String> listView) {
        listView.getItems().clear();
        for (Student s : course.getStudents()) {
            String entry = s.getFirstName() + " " + s.getLastName() + " (" + s.getUsername() + ") - ";
            if (tempGrades.containsKey(s)) {
                entry += tempGrades.get(s) + "/" + assignment.getMaxPoints() + " (Pending)";
            } else {
                Grade g = assignment.getGrade(s);
                entry += g != null ? g.getPointsReceived() + "/" + assignment.getMaxPoints() : "Ungraded";
            }
            listView.getItems().add(entry);
        }
    }
}
