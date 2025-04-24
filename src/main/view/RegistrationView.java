package view;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import model.*;
import util.FileUtil;
import util.SecurityUtil;

public class RegistrationView {

    private Scene scene;

    public RegistrationView(GradebookModel model, MainView mainView) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Label passwordHint = new Label("Password must be 8+ characters with uppercase, lowercase, digit, and special character.");
        passwordHint.setStyle("-fx-font-size: 11px; -fx-text-fill: gray;");

        ToggleGroup roleGroup = new ToggleGroup();
        RadioButton studentBtn = new RadioButton("Student");
        RadioButton teacherBtn = new RadioButton("Teacher");
        studentBtn.setToggleGroup(roleGroup);
        teacherBtn.setToggleGroup(roleGroup);
        studentBtn.setSelected(true);

        Button registerButton = new Button("Register");
        Button goToLoginBtn = new Button("Go to Login");
        goToLoginBtn.setVisible(false);

        Label status = new Label();

        registerButton.setOnAction(e -> {
            String first = firstNameField.getText().trim();
            String last = lastNameField.getText().trim();
            String user = usernameField.getText().trim();
            String pass = passwordField.getText().trim();

            if (first.isEmpty() || last.isEmpty() || user.isEmpty() || pass.isEmpty()) {
                status.setText("All fields are required.");
                return;
            }

            if (!SecurityUtil.isValidPassword(pass)) {
                status.setText("Password does not meet complexity requirements.");
                return;
            }

            if (model.studentExists(user) || model.teacherExists(user)) {
                status.setText("Username already exists.");
                return;
            }

            if (studentBtn.isSelected()) {
                Student s = new Student(first, last, user, pass);
                model.addStudent(s);
                FileUtil.saveUsers(model.getAllUsers(), "users.txt");
                status.setText("Student registered.");
            } else if (teacherBtn.isSelected()) {
                Teacher t = new Teacher(user, first, last, pass);
                model.addTeacher(t);
                FileUtil.saveUsers(model.getAllUsers(), "users.txt");
                status.setText("Teacher registered.");
            } else {
                status.setText("Please select a role.");
                return;
            }

            goToLoginBtn.setVisible(true);
        });

        goToLoginBtn.setOnAction(e -> mainView.showLoginScreen());

        root.getChildren().addAll(
            new Label("Register"),
            firstNameField, lastNameField,
            usernameField, passwordField, passwordHint,
            new Label("Role:"), studentBtn, teacherBtn,
            registerButton, goToLoginBtn, status
        );

        this.scene = new Scene(root, 420, 470);
    }

    public Scene getScene() {
        return scene;
    }
}
