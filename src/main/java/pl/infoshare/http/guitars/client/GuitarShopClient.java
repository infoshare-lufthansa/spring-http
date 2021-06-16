package pl.infoshare.http.guitars.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class GuitarShopClient {

    private final RestTemplate restTemplate;

    public GuitarShopClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Guitar> getGuitars() {
        return Arrays.asList(restTemplate.getForObject("/guitars", Guitar[].class));
    }

    public Cart createCart() {
        return restTemplate.postForObject("/carts", null, Cart.class);
    }

    public void addGuitarToCart(String cartId, AddGuitarRequest addGuitarRequest) {
        var httpEntity = new HttpEntity<>(addGuitarRequest, prepareHeaders(cartId));
        restTemplate.exchange("/carts/guitars", HttpMethod.PUT, httpEntity, Void.class);
    }

    public void purchaseCart(String cartId) {
        var httpEntity = new HttpEntity<>(prepareHeaders(cartId));
        restTemplate.exchange("/carts/checkout", HttpMethod.POST, httpEntity, Void.class);
    }

    private HttpHeaders prepareHeaders(String cartId) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.add("x-cart-id", cartId);

        return httpHeaders;
    }

}
