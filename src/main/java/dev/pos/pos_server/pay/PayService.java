package dev.pos.pos_server.pay;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.pos.pos_server.pay.dto.PayRequest;
import dev.pos.pos_server.pay.dto.PayResponse;
import dev.pos.pos_server.pay.dto.VanResponse;
import dev.pos.pos_server.pay.entity.Payment;
import dev.pos.pos_server.pay.entity.PaymentRequestStatus;
import dev.pos.pos_server.pay.iso8583.VanClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Random;

import static dev.pos.pos_server.pay.iso8583.Iso8583MessageBuilder.buildIso8583Message;

@Service
@RequiredArgsConstructor
public class PayService {

    private final VanClient vanClient;
    private final PaymentRepository paymentRepository;

    /**
     * ISO 8583 변환 후 VAN 서버에 전송
     * @param request
     * @return
     */
    public PayResponse authorize(PayRequest request) {

        // transactionId, terminalId, merchantId 생성
        String transactionId = String.format("%06d", new Random().nextInt(1000000)); // 랜덤 6자리
        String terminalId = "TERM0001";
        String merchantId = "MERCHANT000001";

        Payment payment = Payment.builder()
                .transactionId(transactionId)
                .cardNumber(request.getCardNumber())
                .amount(request.getAmount())
                .merchantId(merchantId)
                .terminalId(terminalId)
                .status(PaymentRequestStatus.PENDING)  // 요청 현황: PENDING으로 저장
                .build();
        paymentRepository.save(payment);


        // 요청 ISO 메시지 생성
        byte[] message = buildIso8583Message(transactionId, request.getCardNumber(), request.getAmount(), terminalId, merchantId);
        System.out.println(Arrays.toString(message));

        // VAN 서버로 전송
        VanResponse vanResponse = vanClient.sendPaymentRequest(message);
        System.out.println("VAN 서버 응답: " + vanResponse);

        // 결제 상태 결정
        PaymentRequestStatus status = vanResponse.isApproved() ? PaymentRequestStatus.SUCCESS : PaymentRequestStatus.FAILED;

        // VAN 서버 응답에 따라 상태 업데이트
        updatePaymentStatus(payment.getId(), status);

        return PayResponse.from(request, transactionId, merchantId, terminalId, status);

    }

    // VAN 서버에서 응답을 받으면 상태를 성공/실패로 업데이트
    public void updatePaymentStatus(Long paymentId, PaymentRequestStatus status) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.updateStatus(status);
        paymentRepository.save(payment);
    }

}
