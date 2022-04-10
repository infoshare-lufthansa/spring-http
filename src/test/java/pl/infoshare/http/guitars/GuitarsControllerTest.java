package pl.infoshare.http.guitars;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.infoshare.http.guitars.client.Cart;
import pl.infoshare.http.guitars.client.Guitar;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest("guitar.shop.uri=http://localhost:8081")
@AutoConfigureMockMvc
class GuitarsControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CacheManager cacheManager;

    @Test
    void should_buy_guitar() throws Exception {
        // given
        WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().port(8081));
        wireMockServer.start();

        var guitarId = "2";
        var givenGuitar = new Guitar(guitarId, "Ibanez", 10, 90);
        var allGuitars = List.of(givenGuitar);

        var cartId = "15";
        var createdCart = new Cart(cartId, "Maciek");

        wireMockServer.stubFor(
                get("/guitars")
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(objectMapper.writeValueAsBytes(allGuitars))
                        )
        );
        wireMockServer.stubFor(
                post("/carts")
                        .willReturn(aResponse()
                                .withStatus(201)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(objectMapper.writeValueAsBytes(createdCart))
                        )
        );
        wireMockServer.stubFor(
                put("/carts/guitars")
                        .withHeader("x-cart-id", equalTo(cartId))
                        .withRequestBody(matchingJsonPath("guitarId", equalTo(guitarId)))
                        .withRequestBody(matchingJsonPath("amount", equalTo("1")))
                        .willReturn(aResponse().withStatus(200))
        );
        wireMockServer.stubFor(
                post("/carts/checkout")
                        .withHeader("x-cart-id", equalTo(cartId))
                        .willReturn(aResponse().withStatus(200))
        );

        // when
        mockMvc.perform(MockMvcRequestBuilders.put("/guitars/{id}/purchase", guitarId))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // then
        assertThat(cacheManager.getCache("guitars").get(SimpleKey.EMPTY)).isNotNull();
    }
}