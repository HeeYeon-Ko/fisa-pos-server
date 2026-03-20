package dev.pos.pos_server;

import dev.pos.pos_server.pay.iso8583.Iso8583MessageBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PosServerApplicationTests {

	@Test
	void buildIso8583Message_ShouldCreateMessage() {
		// given
		String transactionId = "TXN123456";
		String cardNumber = "1234567812345678";
		BigDecimal amount = new BigDecimal("1000.50");
		String terminalId = "T001";
		String merchantId = "M001";

		// when
		byte[] iso8583Message = Iso8583MessageBuilder.buildIso8583Message(transactionId, cardNumber, amount, terminalId, merchantId);
		System.out.println(Arrays.toString(iso8583Message));

		// then
		assertNotNull(iso8583Message);  // 메시지가 생성되었는지 확인
		assertTrue(iso8583Message.length > 0);  // 바이트 배열의 길이가 0보다 커야 함
		// 추가적인 검사: 특정 필드가 잘 설정되었는지 (예: DE2, DE4 등) 확인할 수 있음
		// 이 부분은 바이트 배열을 읽어서 특정 필드를 검증하는 방법을 추가할 수 있음
	}

}
