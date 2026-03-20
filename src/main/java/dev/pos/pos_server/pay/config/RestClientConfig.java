package dev.pos.pos_server.pay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // RestTemplate을 빈으로 등록
    @Bean
    public RestClient restClient() {
        return RestClient.builder().build();
    }
}
