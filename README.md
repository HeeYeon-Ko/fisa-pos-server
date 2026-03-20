# fisa-pos-server
# POS Server (가맹점 결제 단말기)

카드 결제 요청을 ISO 8583 전문으로 변환하여 VAN 서버로 전송하는 POS 서버입니다.

## 프로젝트 구조

```
src/main/java/dev/pos/pos_server/
├── PosServerApplication.java
├── pay/
│   ├── PayController.java          # 결제 API 엔드포인트
│   ├── PayService.java             # 결제 비즈니스 로직
│   ├── PaymentRepository.java      # 결제 내역 저장소
│   ├── config/
│   │   └── RestClientConfig.java   # RestClient 빈 설정
│   ├── dto/
│   │   ├── PayRequest.java         # 결제 요청 DTO (카드번호, 금액)
│   │   ├── PayResponse.java        # 결제 응답 DTO
│   │   ├── VanRequest.java         # VAN 요청 DTO (Base64 인코딩된 ISO 메시지)
│   │   └── VanResponse.java        # VAN 응답 DTO (승인번호, 응답코드 등)
│   ├── entity/
│   │   ├── Payment.java            # 결제 엔티티
│   │   └── PaymentRequestStatus.java  # 결제 상태 (PENDING, SUCCESS, FAILED)
│   └── iso8583/
│       ├── Iso8583MessageBuilder.java  # ISO 8583 전문 생성
│       └── VanClient.java             # VAN 서버 통신 클라이언트
```

## 결제 흐름

```
1. 사용자가 카드번호, 금액을 입력하여 결제 요청
2. POS 서버가 결제 정보를 DB에 PENDING 상태로 저장
3. ISO 8583 전문(0200 승인 요청)으로 변환
4. Base64 인코딩 후 VAN 서버(:8083)로 REST 전송
5. VAN 응답의 approved 값에 따라 SUCCESS / FAILED로 상태 업데이트
6. 결제 결과를 사용자에게 반환
```

## API

### POST /api/pos

결제 승인을 요청합니다.

**Request**
```json
{
  "cardNumber": "4111111111111111",
  "amount": 10000
}
```

| 필드 | 타입 | 설명 | 검증 |
|------|------|------|------|
| cardNumber | String | 카드번호 | 15~16자리 숫자, 필수 |
| amount | BigDecimal | 결제 금액 | 1원 이상, 필수 |

**Response (201 Created)**
```json
{
  "transactionId": "048231",
  "cardNumber": "4111111111111111",
  "amount": 10000,
  "merchantId": "MERCHANT000001",
  "terminalId": "TERM0001",
  "status": "SUCCESS"
}
```

## ISO 8583 전문 구성

MTI `0200` (Financial Transaction Request)으로 다음 필드를 세팅합니다.

| DE | 필드명 | 설명 | 예시 |
|----|--------|------|------|
| 2 | PAN | 카드번호 (LLVAR) | 4111111111111111 |
| 4 | Amount | 결제 금액 | 000000010000 |
| 37 | Retrieval Reference | 거래 ID (ALPHA 12) | 048231 |
| 41 | Terminal ID | 단말기 ID (ALPHA 8) | TERM0001 |
| 42 | Merchant ID | 가맹점 ID (ALPHA 15) | MERCHANT000001 |

## VAN 서버 통신

- VAN 서버 주소: `http://{VAN_IP}:8083/van`
- ISO 8583 바이트 배열을 Base64로 인코딩하여 JSON으로 전송
- 통신 실패 시 fallback 응답 반환 (approved: false)
- HTTP 오류, 타임아웃, 연결 실패 각각 예외 처리

## 실행 방법

### 1. MySQL 데이터베이스 생성

```sql
CREATE DATABASE pos;
```

### 2. application.yaml 설정

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/pos?serverTimezone=UTC&characterEncoding=UTF-8
    username: root
    password: 1234
  jpa:
    hibernate:
      ddl-auto: update
```

### 3. 실행

```bash
./gradlew bootRun
```

### 4. Swagger UI 접속

```
http://localhost:8080/swagger-ui.html
```
