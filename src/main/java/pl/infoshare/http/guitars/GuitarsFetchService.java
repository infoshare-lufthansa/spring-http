package pl.infoshare.http.guitars;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.infoshare.http.guitars.client.Guitar;
import pl.infoshare.http.guitars.client.GuitarShopClient;
import pl.infoshare.http.guitars.model.AvailableGuitar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GuitarsFetchService {


    public List<AvailableGuitar> fetchAllGuitars() {
        return Collections.emptyList();
    }
}
