package pl.infoshare.http.guitars.model;

import lombok.Value;

@Value
public class AvailableGuitar {
    String id;
    String name;
    Integer price;
    Integer stock;

}
