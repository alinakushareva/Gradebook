// CourseDetailView.java (Enhanced to prompt category setup when creating a course)
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

    public CourseDetailView(Teacher teacher, GradebookModel model, MainController controller, MainView mainView) {
        this.model = model;
        this.teacher = teacher;
        this.model.addObserver(this);

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));

        Label header = new Label("📚 Manage Your Courses");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TextField courseNameField = new TextField();
        courseNameField.setPromptText("New Course Name");
        Button addCourseBtn = new Button("Add Course");

        HBox createBox = new HBox(10, courseNameField, addCourseBtn);
        createBox.setAlignment(Pos.CENTER_LEFT);

        Label currentLabel = new Label("Current Courses:");
        currentList = new ListView<>();

        Label completedLabel = new Label("Completed Courses:");
        completedList = new ListView<>();

        refreshCourseLists();

        addCourseBtn.setOnAction(e -> {
            String name = courseNameField.getText().trim();
            if (!name.isEmpty() && model.getCourseByName(name) == null) {
                Course course = new Course(name);

                // Prompt category setup
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Set Categories for " + name);
                dialog.setHeaderText("Define assignment categories and weights (must total 100%)");

                VBox categoryBox = new VBox(10);
                ScrollPane scrollPane = new ScrollPane(categoryBox);
                scrollPane.setFitToWidth(true);

                Button addCategoryBtn = new Button("+ Add Category");
                categoryBox.getChildren().add(addCategoryBtn);

                List<HBox> categoryRows = new ArrayList<>();

                addCategoryBtn.setOnAction(event -> {
                    TextField catName = new TextField();
                    catName.setPromptText("Category Name");
                    Spinner<Double> weight = new Spinner<>(0.0, 100.0, 10.0, 1.0);
                    weight.setEditable(true);
                    HBox row = new HBox(10, new Label("Name:"), catName, new Label("Weight:"), weight);
                    categoryRows.add(row);
                    categoryBox.getChildren().add(row);
                });

                dialog.getDialogPane().setContent(scrollPane);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                dialog.setResultConverter(btn -> {
                    if (btn == ButtonType.OK) {
                        double total = 0;
                        for (HBox row : categoryRows) {
                            TextField nameField = (TextField) row.getChildren().get(1);
                            Spinner<?> weightSpinner = (Spinner<?>) row.getChildren().get(3);
                            if (!nameField.getText().trim().isEmpty()) {
                                String catName = nameField.getText().trim();
                                double catWeight = (Double) weightSpinner.getValue();
                                total += catWeight;
                                course.addCategory(new Category(catName, catWeight / 100.0));
                            }
                        }
                        if (Math.abs(total - 100.0) > 0.01) {
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Category weights must sum to 100%", ButtonType.OK);
                            alert.showAndWait();
                            return null;
                        }
                        teacher.addCourse(course);
                        model.addCourse(course);
                        courseNameField.clear();
                    }
                    return null;
                });

                dialog.showAndWait();
            }
        });

        Button backBtn = new Button("← Back");
        backBtn.setOnAction(e -> mainView.setScene(new TeacherDashboardView(teacher, model, controller, mainView).getScene()));

        content.getChildren().addAll(header, createBox, currentLabel, currentList, completedLabel, completedList, backBtn);
        content.setAlignment(Pos.TOP_LEFT);

        this.scene = new Scene(content, 650, 600);
    }

    @Override
    public void update() {
        refreshCourseLists();
    }

    private void refreshCourseLists() {
        currentList.getItems().clear();
        completedList.getItems().clear();

        for (Course c : teacher.getCourses()) {
            boolean allGraded = !c.getStudents().isEmpty();
            for (Student s : c.getStudents()) {
                if (s.getFinalGrade(c) == null) {
                    allGraded = false;
                    break;
                }
            }
            if (allGraded && !c.getStudents().isEmpty()) {
                completedList.getItems().add(c.getCourseName());
            } else {
                currentList.getItems().add(c.getCourseName());
            }
        }

        if (currentList.getItems().isEmpty()) currentList.getItems().add("No current courses yet");
        if (completedList.getItems().isEmpty()) completedList.getItems().add("No completed courses yet");
    }

    public Scene getScene() {
        return scene;
    }
}
