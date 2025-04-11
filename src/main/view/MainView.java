/**
 * Project Name: Gradebook
 * File Name: MainView.java
 * Course: CSC 335 Spring 2025
 * Purpose: Root view container that manages all application scenes and handles 
 *          transitions between different views. Maintains a registry of scenes
 *          and coordinates with MainController for navigation.
 */
package view;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;

public class MainView {
    private final Stage primaryStage;
    private final Map<String, Scene> sceneMap;
    private Scene currentScene;

    /**
     * Initializes the MainView with the primary JavaFX stage
     * @param primaryStage The main application window stage
     */
    public MainView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.sceneMap = new HashMap<>();
        this.currentScene = null;
        configureStage();
    }

    /**
     * Configures basic stage properties
     */
    private void configureStage() {
        primaryStage.setTitle("Gradebook System");
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
    }

    /**
     * Registers a scene with a unique identifier
     * @param sceneName Key to identify the scene
     * @param scene The Scene object to register
     */
    public void registerScene(String sceneName, Scene scene) {
        sceneMap.put(sceneName, scene);
    }

    /**
     * Switches the current view to a registered scene
     * @param sceneName The key of the scene to display
     */
    public void setScene(String sceneName) {
        Scene scene = sceneMap.get(sceneName);
        if (scene != null) {
            currentScene = scene;
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }

    /**
     * Overloaded version that registers and switches to a scene in one operation
     * @param sceneName Key to identify the scene
     * @param scene The Scene object to display
     */
    public void setScene(String sceneName, Scene scene) {
        registerScene(sceneName, scene);
        setScene(sceneName);
    }

    /**
     * Displays an error message to the user
     * @param message The error message to display
     */
    public void showError(String message) {
        ErrorPopupView errorPopup = new ErrorPopupView(message);
        errorPopup.show();
    }

    /**
     * Gets the currently displayed scene
     * @return The current Scene object
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Starts the application by showing the initial scene
     * @param initialSceneName The name of the first scene to display
     */
    public void start(String initialSceneName) {
        setScene(initialSceneName);
        primaryStage.show();
    }
}