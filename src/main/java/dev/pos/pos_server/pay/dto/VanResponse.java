package dev.pos.pos_server.pay.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VanResponse {
    private String transactionId;
    private String approvalNumber;
    private String responseCode;
    private String message;
    private BigDecimal amount;
    private LocalDateTime authorizationDate;
    private boolean approved;

    @Override
    public String toString() {
        return "VanResponse{" +
                "transactionId='" + transactionId + '\'' +
                ", approvalNumber='" + approvalNumber + '\'' +
                ", responseCode='" + responseCode + '\'' +
                ", message='" + message + '\'' +
                ", amount=" + amount +
                ", authorizationDate='" + authorizationDate + '\'' +
                ", approved=" + approved +
                '}';
    }
}
