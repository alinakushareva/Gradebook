/**
 * Project Name: Gradebook
 * File Name: MainApp.java
 * Course: CSC 335 Spring 2025
 *
 * Purpose:
 * The entry point for the Gradebook application. Initializes the GradebookModel and launches
 * the JavaFX application by creating the main view.
 */
package view;

import javafx.application.Application;
import javafx.stage.Stage;
import model.GradebookModel;

public class MainApp extends Application {

	/**
     * The main entry point for all JavaFX applications.
     * Initializes the model and launches the main GUI window.
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        GradebookModel model = new GradebookModel();
        new MainView(primaryStage, model);
    }

    /**
     * Launches the application from the command line.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
