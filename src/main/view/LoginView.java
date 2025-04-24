package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import model.*;

public class LoginView {
    private Scene scene;

    public LoginView(GradebookModel model, MainView mainView) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Label status = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (model.studentExists(username)) {
                Student student = model.getStudentByUsername(username);
                if (student.getPasswordHash().equals(password)) {
                    mainView.showStudentDashboard(student);
                    return;
                }
            }
            if (model.teacherExists(username)) {
                Teacher teacher = model.getTeacherByUsername(username);
                if (teacher.getPasswordHash().equals(password)) {
                    mainView.showTeacherDashboard(teacher);
                    return;
                }
            }
            status.setText("Invalid login.");
        });

        root.getChildren().addAll(new Label("Login"), usernameField, passwordField, loginButton, status);
        scene = new Scene(root, 400, 300);
    }

    public Scene getScene() {
        return scene;
    }
}