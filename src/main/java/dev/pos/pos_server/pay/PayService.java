package dev.pos.pos_server.pay;

import dev.pos.pos_server.pay.dto.PayRequest;
import dev.pos.pos_server.pay.dto.PayResponse;
import org.springframework.stereotype.Service;

import java.util.Random;

import static dev.pos.pos_server.pay.iso8583.Iso8583MessageBuilder.buildIso8583Message;

@Service

public class PayService {

    /**
     * ISO 8583 변환 후 VAN 서버에 전송
     * @param request
     * @return
     */
    public PayResponse authorize(PayRequest request) {

        // 1. transactionId, terminalId, merchantId 생성
        String transactionId = String.format("%06d", new Random().nextInt(1000000)); // 랜덤 6자리
        String terminalId = "TERM0001";
        String merchantId = "MERCHANT000001";

        // 2. 요청 ISO 메시지 생성
        byte[] message = buildIso8583Message(transactionId, request.getCardNumber(), request.getAmount(), terminalId, merchantId);

        // 2. VAN 서버로 전송


        // 임시로 응답 만들기 (응답 내용은 나중에 보고 수정)
        return PayResponse.from(request, transactionId, merchantId, terminalId);


    }

}
