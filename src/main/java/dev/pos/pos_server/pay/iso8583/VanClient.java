package dev.pos.pos_server.pay.iso8583;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

// VAN 서버와의 통신 담당
@Component
public class VanClient {

    private final RestTemplate restTemplate;

    public VanClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // ISO8583 메시지를 VAN 서버로 보내는 메서드
    public String sendPaymentRequest(byte[] iso8583Message) {
        // VAN 서버의 엔드포인트
        String url = "http://van-server.com/process"; // 실제 VAN 서버 URL로 변경 필요

        // HTTP 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/octet-stream");

        // HTTP 요청 바디에 ISO8583 메시지 바이트 배열 담기
        HttpEntity<byte[]> entity = new HttpEntity<>(iso8583Message, headers);

        // POST 요청을 보내고 응답 받기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // 응답 메시지 반환
        return response.getBody();
    }
}
