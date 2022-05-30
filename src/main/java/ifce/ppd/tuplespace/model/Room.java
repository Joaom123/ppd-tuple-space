package ifce.ppd.tuplespace.model;

import net.jini.core.entry.Entry;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Room implements Entry {
    private static final long serialVersionUID = -4165842418415657629L;

    public UUID id;
    public String name;
    public Set<User> users;

    public Room(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public Room() {
    }

    @Override
    public String toString() {
        return "Id: " + id.toString().substring(0, 5) + " - " + "Nome: " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Room)) return false;
        Room room = (Room) o;
        return id.equals(room.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
