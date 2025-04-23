package view;

import controller.TeacherController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import model.Course;
import model.Teacher;

import java.util.List;

/**
 * TeacherDashboardView provides a visual overview for teachers,
 * allowing them to view their courses and access course details.
 */
public class TeacherDashboardView {

    private final ListView<Course> courseListView = new ListView<>();
    private final Button viewCourseButton = new Button("View Selected Course");
    private final Label welcomeLabel = new Label();
    private final Scene scene;

    /**
     * Constructs the teacher dashboard view.
     *
     * @param teacher The currently logged-in teacher
     * @param controller The controller for teacher operations
     * @param mainView The main view used for switching scenes
     */
    public TeacherDashboardView(Teacher teacher, TeacherController controller, MainView mainView) {
        welcomeLabel.setText("Welcome, " + teacher.getFullName());
        welcomeLabel.setFont(new Font(20));

        // Populate the course list from controller
        List<Course> courses = controller.getTeachingCourses();
        courseListView.getItems().setAll(courses);

        // Set up button logic to navigate to CourseDetailView
        viewCourseButton.setOnAction(e -> {
            Course selectedCourse = courseListView.getSelectionModel().getSelectedItem();
            if (selectedCourse != null) {
                CourseDetailView detailView = new CourseDetailView(selectedCourse, controller);
                mainView.setScene("courseDetail", detailView.getScene());
            } else {
                new ErrorPopupView("Please select a course.").show();
            }
        });

        VBox layout = new VBox(10, welcomeLabel, courseListView, viewCourseButton);
        layout.setPadding(new Insets(15));
        scene = new Scene(layout, 800, 600);
    }

    /**
     * Returns the scene object associated with this view.
     * @return Scene for the teacher dashboard
     */
    public Scene getScene() {
        return scene;
    }
}
