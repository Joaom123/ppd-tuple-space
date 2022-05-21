package ifce.ppd.tuplespace.controller;

import ifce.ppd.tuplespace.model.User;
import ifce.ppd.tuplespace.server.Lookup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.UUID;

public class ChooseRoomController implements Initializable {
    @FXML
    private Text username;

    @FXML
    private Text id;

    private String usernameInput;

    public ChooseRoomController(String usernameInput) {
        this.usernameInput = usernameInput;
    }

    @FXML
    protected void onCreateRoom() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Procurando serviço JavaSpace...");
        Lookup finder = new Lookup(JavaSpace.class);
        JavaSpace space = (JavaSpace) finder.getService();
        if (space == null) {
            System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
            System.exit(-1);
        }
        System.out.println("O servico JavaSpace foi encontrado.");
        System.out.println(space);

        User user = new User();
        user.id = UUID.randomUUID();
        user.name = usernameInput;
        username.setText(user.name);
        id.setText(user.id.toString());

        try {
            space.write(user, null, 60 * 1000);
        } catch (TransactionException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
