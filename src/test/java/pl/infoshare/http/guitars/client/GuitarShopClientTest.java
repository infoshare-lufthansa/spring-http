package pl.infoshare.http.guitars.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheType;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@RestClientTest(components = GuitarShopClient.class, properties = "guitar.shop.uri=http://localhost:8081")
@AutoConfigureCache(cacheProvider = CacheType.SIMPLE)
class GuitarShopClientTest {

    @Autowired
    private GuitarShopClient testObj;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private MockRestServiceServer helperProjectServer;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_cache_guitars() throws JsonProcessingException {
        // given
        var givenGuitar = new Guitar("1", "Fender", 900, 1);
        helperProjectServer.expect(once(), requestTo("/guitars"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withStatus(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(objectMapper.writeValueAsBytes(List.of(givenGuitar)))
                );

        // when
        var firstResult = testObj.getGuitars();
        var secondResult = testObj.getGuitars();

        // then
        assertThat(firstResult).isSameAs(secondResult);
        assertThat(cacheManager.getCache("guitars").get(SimpleKey.EMPTY)).isNotNull();
    }

    @Test
    void should_evict_cache_after_buying_guitar() {
        // given
        var givenGuitar = new Guitar("1", "Fender", 900, 1);
        cacheManager.getCache("guitars").put(SimpleKey.EMPTY, List.of(givenGuitar));

        helperProjectServer.expect(once(), requestTo("/carts/checkout"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(header("x-cart-id", "2"))
                .andRespond(withStatus(HttpStatus.OK));

        // when
        testObj.purchaseCart("2");

        // then
        assertThat(cacheManager.getCache("guitars").get(SimpleKey.EMPTY)).isNull();
    }
}