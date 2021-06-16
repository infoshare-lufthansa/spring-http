package pl.infoshare.http.guitars.model;

import lombok.Value;
import pl.infoshare.http.guitars.client.Guitar;

@Value
public class AvailableGuitar {
    String id;
    String name;
    Integer price;
    Integer stock;

    public static AvailableGuitar fromGuitar(Guitar guitar) {
        return new AvailableGuitar(guitar.getId(), guitar.getName(), guitar.getPrice(), guitar.getStock());
    }
}
