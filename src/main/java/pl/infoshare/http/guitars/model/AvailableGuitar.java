package pl.infoshare.http.guitars.model;

import lombok.Value;
import pl.infoshare.http.guitars.client.Guitar;

@Value
public class AvailableGuitar {
    String id;
    String name;
    Integer price;
    Integer stock;

}
