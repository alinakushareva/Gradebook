package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Insets;

import model.*;
import controller.*;

public class MainView {

    private Stage stage;
    private GradebookModel model;
    private MainController mainController;

    public MainView(Stage stage, GradebookModel model) {
        this.stage = stage;
        this.model = model;
        this.mainController = new MainController(model, this);
        showHomeScreen();
    }

    public void showHomeScreen() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-alignment: center");

        Text title = new Text("Gradebook System");
        title.setFont(Font.font("Tahoma", FontWeight.BOLD, 26));

        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(150);
        loginBtn.setOnAction(e -> showLoginScreen());

        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(150);
        registerBtn.setOnAction(e -> showRegistrationScreen());

        root.getChildren().addAll(title, loginBtn, registerBtn);

        stage.setTitle("Gradebook - Home");
        stage.setScene(new Scene(root, 400, 300));
        stage.show();
    }

    public void showLoginScreen() {
        LoginView loginView = new LoginView(model, this);
        stage.setScene(loginView.getScene());
    }

    public void showRegistrationScreen() {
        RegistrationView registrationView = new RegistrationView(model, this);
        stage.setScene(registrationView.getScene());
    }

    public void showStudentDashboard(Student student) {
        StudentDashboardView dashboardView = new StudentDashboardView(student, model, mainController, this);
        stage.setScene(dashboardView.getScene());
    }

    public void showTeacherDashboard(Teacher teacher) {
        TeacherDashboardView dashboardView = new TeacherDashboardView(teacher, model, mainController, this);
        stage.setScene(dashboardView.getScene());
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
    }

    public Stage getStage() {
        return stage;
    }
}
