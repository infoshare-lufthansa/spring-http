package pl.infoshare.http;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class SpringHttpApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringHttpApplication.class, args);
    }

}
