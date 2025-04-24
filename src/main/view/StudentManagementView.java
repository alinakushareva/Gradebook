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

    public StudentManagementView(Teacher teacher, GradebookModel model, MainController controller, MainView mainView) {
        this.model = model;
        this.teacher = teacher;
        this.controller = controller;
        this.mainView = mainView;
        this.model.addObserver(this);

        VBox root = new VBox(15);
        root.setPadding(new Insets(30));

        Label title = new Label("👥 Manage Students");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        courseCombo = new ComboBox<>();
        for (Course c : teacher.getCourses()) {
            courseCombo.getItems().add(c.getCourseName());
        }

        courseCombo.setOnAction(e -> {
            String selected = courseCombo.getValue();
            selectedCourse = model.getCourseByName(selected);
            refreshStudentList();
        });

        studentListView = new ListView<>();

        Button importBtn = new Button("Import Students");
        Button addStudentBtn = new Button("Add Student");
        Button removeBtn = new Button("Remove Selected Student");
        Button groupBtn = new Button("Group Students");

        ComboBox<String> sortCombo = new ComboBox<>(FXCollections.observableArrayList("First Name", "Last Name", "Username"));
        sortCombo.setPromptText("Sort By");

        sortCombo.setOnAction(e -> {
            if (selectedCourse == null) return;
            Comparator<Student> comparator = switch (sortCombo.getValue()) {
                case "First Name" -> Comparator.comparing(Student::getFirstName);
                case "Last Name" -> Comparator.comparing(Student::getLastName);
                case "Username" -> Comparator.comparing(Student::getUsername);
                default -> null;
            };
            if (comparator != null) {
                List<Student> sorted = selectedCourse.getStudents().stream().sorted(comparator).toList();
                studentListView.getItems().setAll(
                        sorted.stream()
                                .map(s -> s.getFirstName() + " " + s.getLastName() + " (" + s.getUsername() + ")")
                                .toList());
            }
        });

        importBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            try {
                Set<String> existingUsernames = selectedCourse.getStudents().stream()
                        .map(Student::getUsername)
                        .collect(Collectors.toSet());

                List<Student> students = FileUtil.loadUsers("users.txt").stream()
                        .filter(u -> u instanceof Student)
                        .map(u -> (Student) u)
                        .filter(s -> !existingUsernames.contains(s.getUsername()))
                        .toList();

                for (Student s : students) {
                    selectedCourse.addStudent(s);
                }
                refreshStudentList();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        addStudentBtn.setOnAction(e -> {
            if (selectedCourse == null) return;

            List<Student> allStudents;
            try {
                allStudents = FileUtil.loadUsers("users.txt").stream()
                        .filter(u -> u instanceof Student)
                        .map(u -> (Student) u)
                        .filter(s -> !selectedCourse.getStudents().contains(s))
                        .toList();
            } catch (IOException ex) {
                ex.printStackTrace();
                return;
            }

            if (allStudents.isEmpty()) {
                new Alert(Alert.AlertType.INFORMATION, "No more registered students to add.").showAndWait();
                return;
            }

            Dialog<Student> dialog = new Dialog<>();
            dialog.setTitle("Add Registered Student");
            dialog.setHeaderText("Select a registered student:");

            ComboBox<Student> studentBox = new ComboBox<>();
            studentBox.getItems().addAll(allStudents);
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

            VBox dialogBox = new VBox(10, new Label("Student:"), studentBox);
            dialogBox.setPadding(new Insets(10));
            dialog.getDialogPane().setContent(dialogBox);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(btn -> btn == ButtonType.OK ? studentBox.getValue() : null);

            dialog.showAndWait().ifPresent(s -> {
                selectedCourse.addStudent(s);
                refreshStudentList();
            });
        });

        removeBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            String selected = studentListView.getSelectionModel().getSelectedItem();
            if (selected == null) return;
            String username = selected.substring(selected.indexOf("(") + 1, selected.indexOf(")"));
            Student s = model.getStudentByUsername(username);
            if (s != null) {
                selectedCourse.removeStudent(s);
                refreshStudentList();
            }
        });

        groupBtn.setOnAction(e -> {
            if (selectedCourse == null) return;
            int groupSize = 2;
            List<Student> all = selectedCourse.getStudents();
            StringBuilder groups = new StringBuilder();
            for (int i = 0; i < all.size(); i += groupSize) {
                int end = Math.min(i + groupSize, all.size());
                List<Student> group = all.subList(i, end);
                groups.append("Group ").append(i / groupSize + 1).append(": ");
                for (Student s : group) {
                    groups.append(s.getFirstName()).append(" ").append(s.getLastName()).append(", ");
                }
                groups.setLength(groups.length() - 2);
                groups.append("\n");
            }
            new Alert(Alert.AlertType.INFORMATION, groups.toString()).show();
        });

        Button back = new Button("← Back");
        back.setOnAction(e -> mainView.setScene(new TeacherDashboardView(teacher, model, controller, mainView).getScene()));

        root.getChildren().addAll(
                title, courseCombo, studentListView, sortCombo,
                importBtn, addStudentBtn, removeBtn, groupBtn, back);
        root.setAlignment(Pos.TOP_LEFT);

        this.scene = new Scene(root, 650, 600);
    }

    private void refreshStudentList() {
        if (selectedCourse == null) return;
        List<Student> students = selectedCourse.getStudents();
        ObservableList<String> items = FXCollections.observableArrayList(
                students.stream()
                        .map(s -> s.getFirstName() + " " + s.getLastName() + " (" + s.getUsername() + ")")
                        .collect(Collectors.toList()));
        studentListView.setItems(items);
        if (items.isEmpty()) {
            studentListView.getItems().add("No students enrolled");
        }
    }

    @Override
    public void update() {
        refreshStudentList();
    }

    public Scene getScene() {
        return scene;
    }
}
