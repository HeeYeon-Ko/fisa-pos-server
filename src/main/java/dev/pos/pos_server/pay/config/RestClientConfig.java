package dev.pos.pos_server.pay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class RestClientConfig {

    // RestTemplate을 빈으로 등록
    @Bean
    public RestClient restClient() {

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        // VAN 서버가 꺼져있거나 방화벽에 막혀있을 때 5초 후 예외 발생
        factory.setConnectTimeout(Duration.ofSeconds(5));
        // 연결은 성공했지만 VAN 서버에서 15초 동안 응답이 없을 때 예외 발생
        factory.setReadTimeout(Duration.ofSeconds(15));

        return RestClient.builder()
                .requestFactory(factory)
                .build();
    }
}
