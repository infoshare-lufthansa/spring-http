package pl.infoshare.http.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class GuitarClientConfiguration {

    @Bean
    public RestTemplate guitarClient(@Value("${guitar.shop.uri}") String guitarShopUri,
                                     @Value("${guitar.shop.username}") String guitarShopUsername,
                                     @Value("${guitar.shop.password}") String guitarShopPassword,
                                     RestTemplateBuilder builder) {
        return builder
                .rootUri(guitarShopUri)
                .basicAuthentication(guitarShopUsername, guitarShopPassword)
                .build();
    }
}
