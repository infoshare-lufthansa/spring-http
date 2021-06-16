package pl.infoshare.http.guitars.client;

public class Cart {

    private final String id;
    private final String owner;

    public Cart(String id, String owner) {
        this.id = id;
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }
}
