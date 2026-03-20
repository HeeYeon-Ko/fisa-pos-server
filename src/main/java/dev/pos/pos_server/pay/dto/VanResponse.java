package dev.pos.pos_server.pay.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record VanResponse(
        String transactionId,
        String approvalNumber,
        String responseCode,
        String message,
        BigDecimal amount,
        LocalDateTime authorizationDate,
        boolean approved
) {}
