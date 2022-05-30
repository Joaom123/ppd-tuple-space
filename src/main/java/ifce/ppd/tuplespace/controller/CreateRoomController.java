package ifce.ppd.tuplespace.controller;

import ifce.ppd.tuplespace.model.ListRoom;
import ifce.ppd.tuplespace.model.Room;
import ifce.ppd.tuplespace.server.Lookup;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.lease.Lease;
import net.jini.core.transaction.TransactionException;
import net.jini.space.JavaSpace;
import java.rmi.RemoteException;
import java.util.UUID;

public class CreateRoomController {
    @FXML
    public TextField roomName;

    @FXML
    public void onCreateRoom(ActionEvent actionEvent) {
        System.out.println("Procurando serviço JavaSpace...");
        Lookup finder = new Lookup(JavaSpace.class);
        JavaSpace javaSpaces = (JavaSpace) finder.getService();

        if (javaSpaces == null) {
            System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
            System.exit(-1);
        }

        System.out.println("O servico JavaSpace foi encontrado.");
        System.out.println(javaSpaces);

        try {
            Room room = new Room(UUID.randomUUID(), roomName.getText());
            ListRoom roomList = (ListRoom) javaSpaces.take(new ListRoom(), null, 5);

            if (roomList == null) {
                roomList = new ListRoom();
                roomList.init();
            }

            roomList.rooms.add(room);

            javaSpaces.write(roomList, null, Lease.FOREVER);
            System.out.println("Sala adicionada!");

            Node source = (Node)  actionEvent.getSource();
            Stage stage  = (Stage) source.getScene().getWindow();
            stage.close();
        } catch (TransactionException | RemoteException | UnusableEntryException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
