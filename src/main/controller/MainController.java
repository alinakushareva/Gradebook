package controller;

import model.*;
import util.SecurityUtil;
import view.MainView;

public class MainController {

    private final GradebookModel model;
    private final MainView mainView;
    private User currentUser;

    /**
     * Constructs the main controller.
     * @param model the gradebook data model
     * @param mainView the main view for switching scenes
     */
    public MainController(GradebookModel model, MainView mainView) {
        this.model = model;
        this.mainView = mainView;
    }

    /**
     * Attempts to authenticate a user based on username and password.
     * @param username the username entered
     * @param password the password entered
     * @return true if authentication is successful, false otherwise
     */
    public boolean authenticate(String username, String password) {
        if (model.studentExists(username)) {
            Student s = model.getStudentByUsername(username);
            if (SecurityUtil.checkPassword(password, s.getPasswordHash())) {
                currentUser = s;
                return true;
            }
        }
        if (model.teacherExists(username)) {
            Teacher t = model.getTeacherByUsername(username);
            if (SecurityUtil.checkPassword(password, t.getPasswordHash())) {
                currentUser = t;
                return true;
            }
        }
        return false;
    }



    /**
     * Navigates to the appropriate dashboard depending on user role.
     */
    public void switchToDashboard() {
        if (currentUser instanceof Student) {
            mainView.setScene("studentDashboard");
        } else if (currentUser instanceof Teacher) {
            mainView.setScene("teacherDashboard");
        }
    }

    /**
     * Logs out the current user and returns to the login screen.
     */
    public void logout() {
        currentUser = null;
        mainView.setScene("login");
    }

    /**
     * Gets the currently logged-in user.
     * @return the current user or null
     */
    public User getCurrentUser() {
        return currentUser;
    }
}
