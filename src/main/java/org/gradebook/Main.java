package org.gradebook;

import javafx.application.Application;
import javafx.stage.Stage;
import org.gradebook.model.GradebookModel;
import org.gradebook.view.MainView;
import org.gradebook.controller.MainController;

import java.util.Locale;

/**
 * Main application class for the Gradebook system.
 * This class serves as the entry point for the application.
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Locale.setDefault(Locale.ENGLISH);
        // Initialize the model
        GradebookModel model = new GradebookModel();
        
        // Initialize the view
        MainView view = new MainView(primaryStage);
        
        // Initialize the controller with model and view
        MainController controller = new MainController(model, view);
        
        // Display the login screen
        controller.showLoginScreen();
        
        // Set window title and show the primary stage
        primaryStage.setTitle("Gradebook Management System");
        primaryStage.show();
        
        // Add shutdown hook to save data when application closes
        primaryStage.setOnCloseRequest(event -> {
            // Save all data
            model.saveAllData();
        });
    }
    
    /**
     * Main method that launches the JavaFX application.
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
} 