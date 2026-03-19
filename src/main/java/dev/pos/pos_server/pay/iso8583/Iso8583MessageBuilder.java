package dev.pos.pos_server.pay.iso8583;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.MessageFactory;

import java.math.BigDecimal;

// ISO8583 메시지 생성
public class Iso8583MessageBuilder {
    public static byte[] buildIso8583Message(String transactionId, String cardNumber, BigDecimal amount, String terminalId, String merchantId) {

        // MessageFactory를 이용해 ISO8583 메시지 생성
        MessageFactory<IsoMessage> factory = new MessageFactory<>();
        factory.setUseBinaryMessages(false);  // ASCII 메시지 사용

        // 승인 요청 메시지(0200)
        IsoMessage isoMessage = factory.newMessage(0x0200);

        // 필드 값 세팅
        isoMessage.setValue(2, cardNumber, IsoType.LLVAR, 0);  // DE2: 카드번호
        isoMessage.setValue(4, amount, IsoType.AMOUNT, 0);     // DE4: 금액
        isoMessage.setValue(37, transactionId, IsoType.ALPHA, 12);  // DE37: 트랜잭션 ID
        isoMessage.setValue(41, terminalId, IsoType.ALPHA, 8);   // DE41: 단말기 ID
        isoMessage.setValue(42, merchantId, IsoType.ALPHA, 15);  // DE42: 가맹점 ID

        // ISO8583 전문을 바이트 배열로 변환
        return isoMessage.writeData();
    }
}
