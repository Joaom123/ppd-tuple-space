package ifce.ppd.tuplespace.model;

import net.jini.core.entry.Entry;

import java.util.Objects;
import java.util.UUID;

public class Message implements Entry {
    private static final long serialVersionUID = -1L;

    public UUID id;
    public UUID roomId;
    public User author;
    public String content;
    public User receiver;

    public Message() {}

    public Message(UUID id, UUID roomId, User author, String content) {
        this.id = id;
        this.roomId = roomId;
        this.author = author;
        this.content = content;
        this.receiver = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;
        Message message = (Message) o;
        return id.equals(message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
