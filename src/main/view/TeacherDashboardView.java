// TeacherDashboardView.java (updated with "Manage Assignments" section)
package view;

import controller.MainController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import model.*;

public class TeacherDashboardView {

    private final Scene scene;

    public TeacherDashboardView(Teacher teacher, GradebookModel model, MainController controller, MainView mainView) {
        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30));

        Label welcome = new Label("Welcome, " + teacher.getFirstName() + " " + teacher.getLastName());
        welcome.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label instruction = new Label("Choose a section to manage your gradebook:");
        instruction.setStyle("-fx-font-size: 14px;");

        // Buttons for navigation
        Button coursesButton = new Button(" Manage Courses");
        coursesButton.setPrefWidth(200);
        coursesButton.setOnAction(e -> {
            CourseDetailView view = new CourseDetailView(teacher, model, controller, mainView);
            mainView.setScene(view.getScene());
        });

        Button studentsButton = new Button(" Manage Students");
        studentsButton.setPrefWidth(200);
        studentsButton.setOnAction(e -> {
            StudentManagementView view = new StudentManagementView(teacher, model, controller, mainView);
            mainView.setScene(view.getScene());
        });

        Button assignmentsButton = new Button(" Manage Assignments");
        assignmentsButton.setPrefWidth(200);
        assignmentsButton.setOnAction(e -> {
            AssignmentManagementView view = new AssignmentManagementView(teacher, model, controller, mainView);
            mainView.setScene(view.getScene());
        });

        Button gradesButton = new Button(" Manage Grades");
        gradesButton.setPrefWidth(200);
        gradesButton.setOnAction(e -> {
            GradeManagementView view = new GradeManagementView(teacher, model, controller, mainView);
            mainView.setScene(view.getScene());
        });

        VBox navButtons = new VBox(15, coursesButton, studentsButton, assignmentsButton, gradesButton);
        navButtons.setAlignment(Pos.CENTER);

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> controller.logout());
        HBox footer = new HBox(logoutBtn);
        footer.setAlignment(Pos.BOTTOM_RIGHT);

        mainContent.getChildren().addAll(welcome, instruction, navButtons);

        BorderPane layout = new BorderPane();
        layout.setCenter(mainContent);
        layout.setBottom(footer);
        layout.setPadding(new Insets(20));

        this.scene = new Scene(layout, 600, 500);
    }

    public Scene getScene() {
        return scene;
    }
}
