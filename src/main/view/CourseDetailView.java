package view;

import controller.TeacherController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import model.Assignment;
import model.Course;
import model.FinalGrade;
import model.Student;
import util.GradeCalculator;

import java.util.List;

/**
 * CourseDetailView displays detailed information about a selected course.
 * It shows a list of assignments and students, and provides controls for
 * sorting, grading, and assigning final grades.
 */
public class CourseDetailView {

    private final Label courseNameLabel = new Label();
    private final TableView<Assignment> assignmentTable = new TableView<>();
    private final TableView<Student> studentTable = new TableView<>();
    private final Button sortByNameButton = new Button("Sort by Name");
    private final Button sortByGradeButton = new Button("Sort by Grade");
    private final Button assignFinalGradesButton = new Button("Assign Final Grades");
    private final Scene scene;

    /**
     * Constructs the course detail view and initializes layout and button logic.
     * 
     * @param course The course being viewed
     * @param controller The controller handling business logic
     */
    public CourseDetailView(Course course, TeacherController controller) {
        courseNameLabel.setText("Course: " + course.getCourseName());
        courseNameLabel.setFont(new Font(18));

        // Sort students alphabetically by name
        sortByNameButton.setOnAction(e -> {
            List<Student> sorted = controller.sortStudentsByName(course);
            updateStudents(sorted);
        });

        // Sort students by selected assignment grade
        sortByGradeButton.setOnAction(e -> {
            Assignment selected = assignmentTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                List<Student> sorted = controller.sortStudentsByAssignmentGrade(course, selected);
                updateStudents(sorted);
            }
        });

        // Automatically assign letter grades to students based on course average
        assignFinalGradesButton.setOnAction(e -> {
            for (Student s : course.getStudents()) {
                double average = course.calculateStudentAverage(s);
                FinalGrade fg = GradeCalculator.getLetterGrade(average);
                controller.assignFinalGrade(course, s, fg);
            }
            updateStudents(course.getStudents()); // Refresh list after grading
        });

        HBox buttonBar = new HBox(10, sortByNameButton, sortByGradeButton, assignFinalGradesButton);
        VBox layout = new VBox(10, courseNameLabel, assignmentTable, studentTable, buttonBar);
        layout.setPadding(new Insets(15));
        scene = new Scene(layout, 900, 600);
    }

    /**
     * Returns the JavaFX scene for rendering this view.
     * @return The Scene object
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Updates the assignment table with new data.
     * @param assignments List of assignments to show
     */
    public void updateAssignments(List<Assignment> assignments) {
        assignmentTable.getItems().setAll(assignments);
    }

    /**
     * Updates the student table with a new list of students.
     * @param students List of students to display
     */
    public void updateStudents(List<Student> students) {
        studentTable.getItems().setAll(students);
    }

    /**
     * Loads course-specific data into the view.
     * @param course The course object to display
     */
    public void displayCourseInfo(Course course) {
        courseNameLabel.setText("Course: " + course.getCourseName());
        updateAssignments(course.getAssignments());
        updateStudents(course.getStudents());
    }
}
