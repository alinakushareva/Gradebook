/**
 * Project Name: Gradebook
 * File Name: ErrorPopupView.java
 * Course: CSC 335 Spring 2025
 * Purpose: Provides a reusable modal error dialog for displaying messages to users.
 *          Follows JavaFX best practices for alert dialogs with consistent styling.
 */
package view;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

public class ErrorPopupView {
	    private final Alert alert;
	
	/**
	 * Creates a new error popup with the specified message
	 * @param message The error message to display (required)
	 */
    public ErrorPopupView(String message) {
        alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.initModality(Modality.APPLICATION_MODAL);
    }

    /**
     * Displays the error popup and waits for user acknowledgment
     * The dialog will block interaction with other application windows until closed
     */
    public void show() {
        alert.showAndWait();
    }
}