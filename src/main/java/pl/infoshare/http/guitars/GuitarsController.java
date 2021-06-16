package pl.infoshare.http.guitars;

import org.springframework.web.bind.annotation.*;
import pl.infoshare.http.guitars.model.AvailableGuitar;

import java.util.List;

@RestController
public class GuitarsController {

    private final GuitarsFetchService guitarsFetchService;
    private final GuitarPurchaseService guitarPurchaseService;

    public GuitarsController(GuitarsFetchService guitarsFetchService, GuitarPurchaseService guitarPurchaseService) {
        this.guitarsFetchService = guitarsFetchService;
        this.guitarPurchaseService = guitarPurchaseService;
    }

    @GetMapping("/guitars")
    public List<AvailableGuitar> getGuitars() {
        return guitarsFetchService.fetchAllGuitars();
    }

    @PutMapping("/guitars/{id}/purchase")
    public void purchaseGuitar(@PathVariable String id) {
        guitarPurchaseService.purchase(id);
    }
}
