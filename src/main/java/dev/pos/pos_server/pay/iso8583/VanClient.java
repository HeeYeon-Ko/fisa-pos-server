package dev.pos.pos_server.pay.iso8583;

import dev.pos.pos_server.pay.dto.VanRequest;
import dev.pos.pos_server.pay.dto.VanResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Base64;

// VAN 서버와의 통신 담당
@Component
public class VanClient {

    private final RestTemplate restTemplate;

    public VanClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ISO8583 메시지를 VAN 서버로 보내는 메서드
    public VanResponse sendPaymentRequest(byte[] iso8583Message) {
        // VAN 서버의 엔드포인트
        String url = "http://192.168.0.11:8083/van"; // 실제 VAN 서버 URL로 변경 필요

        // ISO8583 메시지를 Base64로 인코딩
        String encodedMessage = Base64.getEncoder().encodeToString(iso8583Message);

        // VanRequest 객체 생성 (Base64로 인코딩된 메시지)
        VanRequest request = VanRequest.builder()
                .rawIsoMessage(encodedMessage) // Base64로 인코딩된 메시지
                .build();

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        // HTTP 요청 바디에 VanRequest 객체 담기 (JSON 형식)
        HttpEntity<VanRequest> entity = new HttpEntity<>(request, headers);

        // POST 요청을 보내고 응답 받기
        ResponseEntity<VanResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, VanResponse.class);

        // 응답 메시지 반환
        return response.getBody();
    }
}
