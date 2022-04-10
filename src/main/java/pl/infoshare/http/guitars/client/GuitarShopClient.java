package pl.infoshare.http.guitars.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
public class GuitarShopClient {

    private final WebClient webClient;

    public GuitarShopClient(@Value("${guitar.shop.uri}") String guitarShopUri,
                            @Value("${guitar.shop.username}") String guitarShopUsername,
                            @Value("${guitar.shop.password}") String guitarShopPassword,
                            WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(guitarShopUri)
                .defaultHeaders(headers -> headers.setBasicAuth(guitarShopUsername, guitarShopPassword))
                .build();
    }

    public List<Guitar> getGuitars() {
        return webClient.get()
                .uri("/guitars")
                .retrieve()
                .toEntityList(Guitar.class)
                .block()
                .getBody();
    }

    public Cart createCart() {
        return webClient.post()
                .uri("/carts")
                .retrieve()
                .toEntity(Cart.class)
                .block()
                .getBody();
    }

    public void addGuitarToCart(String cartId, AddGuitarRequest addGuitarRequest) {
        webClient.put()
                .uri("/carts/guitars")
                .header("x-cart-id", cartId)
                .body(BodyInserters.fromValue(addGuitarRequest))
                .retrieve()
                .toEntity(Void.class)
                .block();
    }

    public void purchaseCart(String cartId) {
        webClient.post()
                .uri("/carts/checkout")
                .header("x-cart-id", cartId)
                .retrieve()
                .toEntity(Void.class)
                .block();
    }
}
