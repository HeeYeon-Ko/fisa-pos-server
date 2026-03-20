package dev.pos.pos_server.pay.entity;


import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 기본 키

    private String transactionId;  // 트랜잭션 ID
    private String cardNumber;  // 카드 번호
    private BigDecimal amount;  // 결제 금액
    private String merchantId;  // 가맹점 ID
    private String terminalId;  // 단말기 ID

    //private String approvalNumber;  // 승인 번호
    //private String responseCode;  // 응답 코드
    //private String message;  // 응답 메시지
    //private LocalDateTime authorizationDate;  // 승인 날짜
    //private boolean approved;  // 승인 여부

    @Enumerated(EnumType.STRING)
    private PaymentRequestStatus status;  // 요청 상태 (PENDING, SUCCESS, FAILED)

    // 상태 업데이트
    public void updateStatus(PaymentRequestStatus status) {
        this.status = status;
    }
}
