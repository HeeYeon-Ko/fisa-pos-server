package dev.pos.pos_server.pay.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Schema(name = "결제 요청", description = "결제 요청 DTO")
public class PayRequest {

    @NotNull(message = "카드 번호는 필수값입니다.")
    @Pattern(regexp = "\\d{15,16}", message = "카드번호는 15~16자리 숫자여야 합니다.")
    @Schema(description = "카드번호", example = "4111111111111111")
    private String cardNumber;

    @NotNull(message = "금액은 필수값입니다.")
    @DecimalMin(value = "1", message = "금액은 1원 이상이어야 합니다.")
    @Schema(description = "금액", example = "10000")
    private BigDecimal amount;
}