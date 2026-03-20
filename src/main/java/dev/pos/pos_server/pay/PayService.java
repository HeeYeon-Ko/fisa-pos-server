package dev.pos.pos_server.pay;

import dev.pos.pos_server.pay.dto.PayRequest;
import dev.pos.pos_server.pay.dto.PayResponse;
import org.springframework.stereotype.Service;

@Service

public class PayService {

    /**
     * ISO 8583 변환
     * @param request
     * @return
     */
    public PayResponse authorize(PayRequest request) {

        // 1. 요청 ISO 메시지 생성

        // 2. VAN 서버로 전송


    }

}
