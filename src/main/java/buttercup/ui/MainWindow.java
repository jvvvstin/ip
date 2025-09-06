package buttercup.ui;

import buttercup.Buttercup;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Buttercup buttercup;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/professor.jpg"));
    private Image buttercupImage = new Image(this.getClass().getResourceAsStream("/images/buttercup.jpg"));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Buttercup instance */
    public void setButtercup(Buttercup buttercup) {
        this.buttercup = buttercup;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Buttercup's reply and then appends them
     * to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() throws InterruptedException {
        String input = userInput.getText();
        String response = buttercup.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getButtercupDialog(response, buttercupImage)
        );
        userInput.clear();
        if (input.trim().equals("bye")) {
            Platform.exit();
        }
    }
}
