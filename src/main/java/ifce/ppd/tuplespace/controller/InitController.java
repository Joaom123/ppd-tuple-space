package ifce.ppd.tuplespace.controller;

import ifce.ppd.tuplespace.InitApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class InitController {
    @FXML
    private TextField usernameInput;

    @FXML
    private Text usernameFieldErrorMessage;

    @FXML
    protected void onHelloButtonClick(ActionEvent actionEvent) throws IOException {
        if(usernameInput.getText() == null || usernameInput.getText().trim().isEmpty()) {
            usernameFieldErrorMessage.setText("O nome de usuário é obrigatório");
            return;
        }

        usernameFieldErrorMessage.setText("");

        // Go to choose room view
        FXMLLoader fxmlLoader = new FXMLLoader(InitApplication.class.getResource("choose-room-view.fxml"));
        fxmlLoader.setController(new ChooseRoomController(usernameInput.getText()));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        actualStage.setScene(scene);
        actualStage.show();
    }
}