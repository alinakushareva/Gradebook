package org.gradebook.view;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * The main view manager for the application.
 * Controls which scene is currently displayed and manages scene transitions.
 */
public class MainView {
    private Stage primaryStage;
    private Scene currentScene;
    private Map<String, Scene> sceneMap;
    
    /**
     * Constructor for MainView class.
     *
     * @param stage the primary stage of the application
     */
    public MainView(Stage stage) {
        this.primaryStage = stage;
        this.sceneMap = new HashMap<>();
        
        // Set default window size
        primaryStage.setWidth(800);
        primaryStage.setHeight(600);
        primaryStage.setTitle("Gradebook Management System");
    }
    
    /**
     * Sets the current scene to display.
     *
     * @param sceneName the name of the scene to display
     */
    public void setScene(String sceneName) {
        Scene scene = sceneMap.get(sceneName);
        
        if (scene != null) {
            currentScene = scene;
            primaryStage.setScene(scene);
        } else {
            System.err.println("Scene not found: " + sceneName);
        }
    }
    
    /**
     * Registers a scene with a name for later use.
     *
     * @param name  the name to associate with the scene
     * @param scene the scene to register
     */
    public void registerScene(String name, Scene scene) {
        sceneMap.put(name, scene);
    }
    
    /**
     * Gets the currently displayed scene.
     *
     * @return the current scene
     */
    public Scene getCurrentScene() {
        return currentScene;
    }
    
    /**
     * Gets the primary stage.
     *
     * @return the primary stage
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}