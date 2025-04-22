package view;

import controller.TeacherController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import model.*;
import java.util.List;
import javafx.scene.layout.VBox;


/**
 * TeacherDashboardView provides a list of courses the teacher is managing
 * and allows navigation to course details or creating new content.
 */
public class TeacherDashboardView {
    private final ListView<Course> courseListView = new ListView<>();
    private final Button viewCourseButton = new Button("View Selected Course");
    private final Label welcomeLabel = new Label();
    private final Scene scene;

    public TeacherDashboardView(Teacher teacher, TeacherController controller, MainView mainView) {
        welcomeLabel.setText("Welcome, " + teacher.getFullName());
        welcomeLabel.setFont(new Font(20));

        List<Course> courses = controller.getTeachingCourses();
        courseListView.getItems().setAll(courses);

        // When "View Course" is clicked, load the selected course in a detail view
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
        layout.setPadding(new javafx.geometry.Insets(15));
        scene = new Scene(layout, 800, 600);
    }

    public Scene getScene() {
        return scene;
    }
}

