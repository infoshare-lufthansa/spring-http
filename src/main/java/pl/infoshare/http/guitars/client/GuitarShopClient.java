package pl.infoshare.http.guitars.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@CacheConfig(cacheNames = "guitars")
public class GuitarShopClient {

    private final RestTemplate restTemplate;

    public GuitarShopClient(@Value("${guitar.shop.uri}") String guitarShopUri,
                            @Value("${guitar.shop.username}") String guitarShopUsername,
                            @Value("${guitar.shop.password}") String guitarShopPassword,
                            RestTemplateBuilder builder) {
        this.restTemplate = builder
                .rootUri(guitarShopUri)
                .basicAuthentication(guitarShopUsername, guitarShopPassword)
                .build();
    }

//    @Cacheable
    @CircuitBreaker(name = "guitarsBreaker", fallbackMethod = "fallbackToEmptyGuitars")
    public List<Guitar> getGuitars() {
        System.out.println("getGuitars");
        return Arrays.asList(restTemplate.getForObject("/guitars", Guitar[].class));
    }

    public Cart createCart() {
        return restTemplate.postForObject("/carts", null, Cart.class);
    }

    public void addGuitarToCart(String cartId, AddGuitarRequest addGuitarRequest) {
        var httpEntity = new HttpEntity<>(addGuitarRequest, prepareHeaders(cartId));
        restTemplate.exchange("/carts/guitars", HttpMethod.PUT, httpEntity, Void.class);
    }

    @CacheEvict(allEntries = true)
    public void purchaseCart(String cartId) {
        var httpEntity = new HttpEntity<>(prepareHeaders(cartId));
        restTemplate.exchange("/carts/checkout", HttpMethod.POST, httpEntity, Void.class);
    }

    private HttpHeaders prepareHeaders(String cartId) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.add("x-cart-id", cartId);

        return httpHeaders;
    }

    private List<Guitar> fallbackToEmptyGuitars(Exception exception) {
        return Collections.emptyList();
    }
}
