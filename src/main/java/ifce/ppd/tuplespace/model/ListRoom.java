package ifce.ppd.tuplespace.model;

import net.jini.core.entry.Entry;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class ListRoom implements Entry {
    private static final long serialVersionUID = -4165842418415657629L;

    public UUID id;
    public Set<Room> rooms;

    public ListRoom() {}

    public ListRoom(UUID id) {
        this.id = id;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ListRoom)) return false;
        ListRoom listRoom = (ListRoom) o;
        return id.equals(listRoom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

