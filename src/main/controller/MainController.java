package controller;

import model.*;
import view.MainView;

public class MainController {

    private final GradebookModel model;
    private final MainView mainView;
    private User currentUser;

    /**
     * Constructor for MainController.
     * @param model the shared data model
     * @param mainView the main view for switching scenes
     */
    public MainController(GradebookModel model, MainView mainView) {
        this.model = model;
        this.mainView = mainView;
    }

    
}
