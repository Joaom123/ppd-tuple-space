package ifce.ppd.tuplespace.controller;

import ifce.ppd.tuplespace.InitApplication;
import ifce.ppd.tuplespace.model.ListRoom;
import ifce.ppd.tuplespace.model.Message;
import ifce.ppd.tuplespace.model.Room;
import ifce.ppd.tuplespace.model.User;
import ifce.ppd.tuplespace.server.Lookup;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class RoomController implements Initializable {
    @FXML public TextArea messageArea;
    @FXML public TextField messageInput;
    @FXML public ListView usersListView;

    private JavaSpace javaSpaces;
    private User user;
    private User selectedUser;
    private Timer timer;
    private String selectedUserText;

    public RoomController(User user) {
        this.user = user;

        System.out.println("Procurando serviço JavaSpace...");
        Lookup finder = new Lookup(JavaSpace.class);
        javaSpaces = (JavaSpace) finder.getService();

        if (javaSpaces == null) {
            System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
            System.exit(-1);
        }
    }

    @FXML
    protected void onExitRoom(ActionEvent actionEvent) throws IOException, UnusableEntryException, TransactionException, InterruptedException {
        // Add user to room
        ListRoom listRoom = (ListRoom) javaSpaces.take(new ListRoom(UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c")), null, 5);

        Room nRoom = listRoom.getById(user.room.id);

        nRoom.users.remove(user);
        user.room = null;

        javaSpaces.write(listRoom, null, Lease.FOREVER);
        timer.cancel();
        // Go to next
        FXMLLoader fxmlLoader = new FXMLLoader(InitApplication.class.getResource("choose-room-view.fxml"));
        fxmlLoader.setController(new ChooseRoomController(user));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        actualStage.setScene(scene);
        actualStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timer = new Timer();
        timer.schedule( new TimerTask()
        {
            public void run() {
                Platform.runLater(() -> {
                    ListRoom listRoom = null;
                    try {
                        listRoom = (ListRoom) javaSpaces.read(new ListRoom(UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c")), null, 30);
                        if (listRoom != null) {
                            Room room = listRoom.getById(user.room.id);
                            usersListView.getItems().clear();
                            for (User u : room.users)
                                usersListView.getItems().add(u);
                        }

                        Message message = (Message) javaSpaces.read(new Message(), null, 30);

                        System.out.println(message);
                        if (message == null) return;
                        System.out.println(message.content);
                        System.out.println(message.author.name);

                        if (!message.author.equals(user) && message.roomId.equals(user.room.id) && message.receiver == null)
                            addMessageToChat(message.author.name, message.content);
                        if (!message.author.equals(user) && message.roomId.equals(user.room.id) && message.receiver != null && message.receiver.id.equals(user.id))
                            addMessageToChat(message.author.name, message.content);
                    } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 2000);
    }

    private void addMessageToChat(String author, String message) {
        messageArea.appendText(author + ": " + message + "\n");
    }

    @SuppressWarnings("unused")
    public void handleChatInput(ActionEvent actionEvent) throws TransactionException, RemoteException {
        String inputText = messageInput.getText();

        // If user typed nothing, exit function
        if (inputText.equals("")) return;

        messageInput.setText("");

        addMessageToChat(user.name, inputText);

        // Send inputText to javaSpaces
        Message message = new Message(UUID.randomUUID(), user.room.id, user, inputText);
        if(selectedUser != null && messageInput.getText().contains(selectedUserText))
            message.receiver = selectedUser;
        javaSpaces.write(message, null, 1000);
    }

    @FXML public void handleMouseClick(MouseEvent arg0) {
        selectedUser = (User) usersListView.getSelectionModel().getSelectedItem();
        if(selectedUser.equals(user))
            selectedUser = null;
        if (selectedUser == null)
            return;
        String actualText = messageInput.getText();
        selectedUserText = "@" + selectedUser.name;
        messageInput.setText(selectedUserText + " " + actualText);
    }
}
