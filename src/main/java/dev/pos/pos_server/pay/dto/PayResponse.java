package dev.pos.pos_server.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(name = "결제 응답", description = "결제 응답 DTO")
public class PayResponse {

    @Schema(description = "거래 id", example = "000001")
    private String transactionId;

    @Schema(description = "카드번호", example = "4111111111111111")
    private String cardNumber;

    @Schema(description = "금액", example = "10000")
    private BigDecimal amount;

    @Schema(description = "가맹점 id", example = "MERCHANT000001")
    private String merchantId;

    @Schema(description = "단말기 id", example = "TERM0001")
    private String terminalId;
}