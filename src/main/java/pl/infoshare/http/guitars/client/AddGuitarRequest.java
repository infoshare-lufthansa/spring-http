package pl.infoshare.http.guitars.client;

public class AddGuitarRequest {

    private final String guitarId;
    private final Integer amount;

    public AddGuitarRequest(String guitarId, Integer amount) {
        this.guitarId = guitarId;
        this.amount = amount;
    }

    public String getGuitarId() {
        return guitarId;
    }

    public Integer getAmount() {
        return amount;
    }
}
