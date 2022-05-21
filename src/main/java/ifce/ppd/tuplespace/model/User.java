package ifce.ppd.tuplespace.model;

import net.jini.core.entry.Entry;
import java.util.UUID;

public class User implements Entry {
    public UUID id;
    public String name;
    public Room room;

    public User(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public User() {
    }
}
