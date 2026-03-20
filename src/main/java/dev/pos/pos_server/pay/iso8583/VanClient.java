package dev.pos.pos_server.pay.iso8583;

import dev.pos.pos_server.pay.dto.VanRequest;
import dev.pos.pos_server.pay.dto.VanResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

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

        HttpEntity<VanRequest> entity = new HttpEntity<>(request, headers);

        try {
            // VAN 서버로 요청 전송
            ResponseEntity<VanResponse> response = restTemplate.exchange(url, HttpMethod.POST, entity, VanResponse.class);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            // 서버 응답은 있으나 오류 발생 시 처리 (예: 500 에러)
            return VanResponse.builder()
                    .transactionId("unknown")
                    .approvalNumber("")
                    .responseCode("500")
                    .message("Internal Server Error")
                    .amount(BigDecimal.ZERO)
                    .authorizationDate(null)
                    .approved(false)
                    .build();
        } catch (ResourceAccessException e) {
            // 연결이 안되거나 타임아웃이 발생했을 때 처리
            // 15초 대기 후 FAILED 응답 반환
            try {
                // 15초 대기 후 응답 처리
                TimeUnit.SECONDS.sleep(15);
            } catch (InterruptedException interruptedException) {
                // 대기 중 인터럽트가 발생할 경우에도 처리
                Thread.currentThread().interrupt();
            }

            // VAN 서버와의 연결 실패 시 'FAILED'로 처리
            return VanResponse.builder()
                    .transactionId("unknown")
                    .approvalNumber("")
                    .responseCode("504")  // Gateway Timeout
                    .message("VAN 서버 연결 실패 또는 타임아웃")
                    .amount(BigDecimal.ZERO)
                    .authorizationDate(null)
                    .approved(false)
                    .build();
        } catch (Exception e) {
            // 그 외의 예외 처리
            return VanResponse.builder()
                    .transactionId("unknown")
                    .approvalNumber("")
                    .responseCode("500")
                    .message("예상치 못한 오류: " + e.getMessage())
                    .amount(BigDecimal.ZERO)
                    .authorizationDate(null)
                    .approved(false)
                    .build();
        }
    }

}
