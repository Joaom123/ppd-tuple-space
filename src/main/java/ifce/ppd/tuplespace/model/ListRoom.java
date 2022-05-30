package ifce.ppd.tuplespace.model;

import net.jini.core.entry.Entry;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ListRoom implements Entry {
    private static final long serialVersionUID = -1L;

    public Set<Room> rooms;

    public ListRoom() {}

    public void init() {
        this.rooms = new HashSet<>();
    }

    public Room getById(UUID uuid) {
        for (Room room : rooms)
            if (room.id.equals(uuid))
                return room;

        return null;
    }

    public void view() {
        for (Room room : rooms) {
            System.out.println(room.name);
            System.out.println(room.id);
        }
    }
}

