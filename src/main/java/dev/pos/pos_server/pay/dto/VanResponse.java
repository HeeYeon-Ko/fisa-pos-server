package dev.pos.pos_server.pay.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class VanResponse {
    private String transactionId;
    private String approvalNumber;
    private String responseCode;
    private String message;
    private BigDecimal amount;
    private LocalDateTime authorizationDate;
    private boolean approved;
}
