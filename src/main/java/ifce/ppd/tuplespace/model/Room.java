package ifce.ppd.tuplespace.model;

import net.jini.core.entry.Entry;
import java.util.UUID;

public class Room implements Entry {
    public UUID id;
    public User[] users;

    public Room(UUID id) {
        this.id = id;
    }

    public Room() {
    }
}
