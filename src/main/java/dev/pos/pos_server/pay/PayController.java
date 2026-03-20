package dev.pos.pos_server.pay;

import dev.pos.pos_server.pay.dto.PayRequest;
import dev.pos.pos_server.pay.dto.PayResponse;
import dev.pos.pos_server.pay.entity.PaymentRequestStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pos")
@Tag(name = "결제")
public class PayController {

    private final PayService payService;

    @Operation(summary = "결제 요청", description = "")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "결제 요청 성공"),
            @ApiResponse(responseCode = "400", description = "결제 승인 실패"),

    })
    @PostMapping()
    public ResponseEntity<PayResponse> requestPayment(@Valid @RequestBody PayRequest request){

        PayResponse response = payService.authorize(request);

        if (response.getStatus() == PaymentRequestStatus.SUCCESS) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);      // 201
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 400
        }
    }


}
