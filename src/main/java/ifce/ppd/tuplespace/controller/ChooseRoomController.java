package ifce.ppd.tuplespace.controller;

import ifce.ppd.tuplespace.InitApplication;
import ifce.ppd.tuplespace.model.ListRoom;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.*;

public class ChooseRoomController implements Initializable {
    @FXML private Text username;
    @FXML private Text id;
    @FXML private ListView<Room> roomListView = new ListView<>();
    @FXML private ListView<User> userListView = new ListView<>();

    private String usernameInput;
    private User user;

    private JavaSpace javaSpaces = null;
    private Timer timer;

    public ChooseRoomController(String usernameInput) {
        this.usernameInput = usernameInput;
    }

    public ChooseRoomController(User user) {
        this.usernameInput = user.name;
        this.user = user;
        username.setText(user.name);
        id.setText(user.id.toString());
    }

    @FXML
    protected void onCreateRoom(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(InitApplication.class.getResource("create-room-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 300, 200);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }

    @FXML public void handleMouseClick(MouseEvent arg0) {
        Room clickedRoom = roomListView.getSelectionModel().getSelectedItem();
        userListView.getItems().clear();

        if (clickedRoom.users == null)
            return;

        Platform.runLater(() -> {
            for (User u : clickedRoom.users)
                userListView.getItems().add(u);
        });
    }

    @FXML
    protected void onEnterRoom(ActionEvent actionEvent) throws IOException, UnusableEntryException, TransactionException, InterruptedException {
        // Get room
        Room room = roomListView.getSelectionModel().getSelectedItem();

        // Add user to room
        ListRoom listRoom = (ListRoom) javaSpaces.take(new ListRoom(UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c")), null, 5);

        Room nRoom = listRoom.getById(room.id);
        if(nRoom.users == null) nRoom.users = new HashSet<>();

        nRoom.users.add(user);
        user.room = nRoom;

        javaSpaces.write(listRoom, null, Lease.FOREVER);
        timer.cancel();

        // Go to next
        FXMLLoader fxmlLoader = new FXMLLoader(InitApplication.class.getResource("room-view.fxml"));
        fxmlLoader.setController(new RoomController(user));
        Scene scene = new Scene(fxmlLoader.load(), 1024, 768);
        Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        actualStage.setScene(scene);
        actualStage.show();
    }

    private void updateRoomList(ListRoom listRoom) {
        for(Room room : listRoom.rooms)
            roomListView.getItems().add(room);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Procurando serviço JavaSpace...");
        Lookup finder = new Lookup(JavaSpace.class);
        javaSpaces = (JavaSpace) finder.getService();

        if (javaSpaces == null) {
            System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
            System.exit(-1);
        }

        System.out.println("O servico JavaSpace foi encontrado.");
        System.out.println(javaSpaces);

        // Create user
        if (user == null) {
            user = new User(UUID.randomUUID(), usernameInput);

            username.setText(user.name);
            id.setText(user.id.toString());

            try {
                javaSpaces.write(user, null, Lease.FOREVER);
            } catch (TransactionException | RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        timer = new Timer();
        timer.schedule( new TimerTask()
        {
            public void run() {
                Platform.runLater(() -> {
                    cleanRoomList();
                    ListRoom listRoom = null;
                    try {
                        listRoom = (ListRoom) javaSpaces.readIfExists(new ListRoom(UUID.fromString("5fc03087-d265-11e7-b8c6-83e29cd24f4c")), null, 5);
                        System.out.println("ListRoom: " + listRoom);
                        if (listRoom != null) {
                            updateRoomList(listRoom);
                        }
                    } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 4000);
    }

    private void cleanRoomList() {
        roomListView.getItems().clear();
    }
}
