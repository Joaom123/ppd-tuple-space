package ifce.ppd.tuplespace.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class InitController {
    @FXML
    private TextField usernameInput;

    @FXML
    private Text usernameFieldErrorMessage;

    @FXML
    protected void onHelloButtonClick() {
        if(usernameInput.getText() == null || usernameInput.getText().trim().isEmpty()) {
            usernameFieldErrorMessage.setText("O nome de usuário é obrigatório");
            return;
        }

        usernameFieldErrorMessage.setText("");
        // Go to choose room view
    }
}