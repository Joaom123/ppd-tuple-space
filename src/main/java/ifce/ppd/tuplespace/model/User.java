package ifce.ppd.tuplespace.model;

public class User {
    private final String id;
    private final String name;
    private Room room;

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
