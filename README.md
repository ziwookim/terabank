# terabank
---
#### 토큰 생성 방법

```
-   JWT(JSON Web Token 토큰 기반인증)을 이용한 토큰 생성
-   application.yml 파일에 'config.token' 이라는 랜덤 문자열 150자 설정.
-   회원 가입시, 생성한 토큰을 Account 테이블에 저장한다.
```

#### 입금 / 출금 / 송금 응답 코드 목록 (공통 응답 코드)

```
public enum ResultCode {
    /**
     * 송금, 입금, 출금 시도 결과 코드 열거형
     */

    /**
     * 성공
     */
    SUCCESS(100), //  성공

      /**
     * 잔액 부족
     */
    LACKOFMONEY(400), //  송금/출금 시 잔액 부족

    /**
     * 금액 오류
     */
    INVALIDAMOUNT(7777), //  송금/입금/출금 api 호출 시 요청 amount 값이 0원 이하이거나, 숫자로 변환 불가능할 경우

    /**
     * 출금자 오류
     */
    SENDERERROR(888), //  송금/출금 호출 시 요청 (sender)privateKey 값으로 account 조회가 불가능한 경우

    /**
     * 입금자 오류
     */
    RECEIVERERROR(999), //  송금/입금 호출 시 요청 (receiver)publicKey 값으로 account 조회가 불가능한 경우

    /**
     * 기타 오류
     */
    OTHERPROBLEMS(6666); //  추가로 원인 분석이 필요한 오류
```

#### 1\. 계좌 신청 API (POST)

\- URL: /api/account ([http://localhost:8080/api/account](http://localhost:8080/api/account))

\- request parameter: userId(사용자 ID)

\- response feld: userId(사용자 ID), publicKey(사용자 공개키), privateKey(사용자 비밀키)

\- 테스트 케이스1 (예상 결과: 정상 처리)

-   requestBody

```
{
    "userId" : "test"
}
```

-   responseBody

```
{
    "userId": "test",
    "publicKey": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjYzNjczMjQ2LCJleHAiOjE2NjM2NzMyNDZ9.4G2PHT3doIEQxpCBe-rocOnBtHmC5tMfRXITWcjFuW8",
    "privateKey": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjYzNjczMjQ2LCJleHAiOjE2NjM2NzMyNDZ9.jWC__B-lDFbpdFM-62rPf3MFJAfLC-P12BjP2_l9hGI"
}
```

\- 테스트 케이스2 (예상 결과: 중복 ID 오류 발생)

-   requestBody

```
{
    "userId" : "test"
}
```

-   responseBody

```
{
    "timestamp": "2022-09-20T11:34:00.278+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/account"
}
```

![image](https://user-images.githubusercontent.com/50875502/192010668-016f0141-5b6a-4f37-8c50-c21074168368.png)

\- 테스트 케이스3 (예상 결과: ID 길이 유효성 관련 오류 발생)

-   requestBody

```
{
    "userId" : "1ab"
}
```

-   responseBody

```
{
    "timestamp": "2022-09-20T11:36:01.507+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/account"
}
```

[##_Image|kage@s9qt2/btrMDQiXxFY/05KK2T8tQKu4IbKc9LVAsk/img.png|CDM|1.3|{"originWidth":2054,"originHeight":216,"style":"alignCenter","width":1070,"height":113,"caption":"IllegalStateException"}_##]

#### 2\. 입금 처리 API (POST)

\- URL: /api/deposit ([http://localhost:8080/api/deposit](http://localhost:8080/api/account))

\- request parameter: receiverPublicKey(입금 받을 사용자 공개 키), amount(입금 금액)

\- response field: resultCode(응답 코드), receiverUserId(입금 받은 사용자 ID)

\- 테스트 케이스1 (예상 resultCode: SUCCESS)

-   requestBody

```
{
    "receiverPublicKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.Ea9QUVeBNEG2eoxCzH7hnxjYWJWDsGoohJt5G09EMz4",
    "amount" : "1000000"
}
```

-   responseBody

```
{
    "resultCode": "SUCCESS",
    "receiverUserId": "sender"
}
```

\- 테스트 케이스2 (예상 resultCode: RECEIVERERROR)

-   requestBody

```
{
    "receiverPublicKey" : "",
    "amount" : "1000000"
}
```

-   responseBody

```
{
    "resultCode": "RECEIVERERROR",
    "receiverUserId": ""
}
```

\- 테스트 케이스3 (예상 resultCode: RECEIVERERROR)

-   requestBody

```
{
    "receiverPublicKey" : "12345",
    "amount" : "100000"
}
```

-   responseBody

```
{
    "resultCode": "RECEIVERERROR",
    "receiverUserId": ""
}
```

\- 테스트 케이스4 (예상 resultCode: INVALIDAMOUNT)

-   requestBody

```
{
    "receiverPublicKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.Ea9QUVeBNEG2eoxCzH7hnxjYWJWDsGoohJt5G09EMz4",
    "amount" : "0"
}
```

-   responseBody

```
{
    "resultCode": "INVALIDAMOUNT",
    "receiverUserId": ""
}
```

\- 테스트 케이스5 (예상 resultCode: INVALIDAMOUNT)

-   requestBody

```
{
    "receiverPublicKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.Ea9QUVeBNEG2eoxCzH7hnxjYWJWDsGoohJt5G09EMz4",
    "amount" : "-30000"
}
```

-   responseBody

```
{
    "resultCode": "INVALIDAMOUNT",
    "receiverId": ""
}
```

#### 3\. 출금 처리 API (POST)

\- URL: /api/withdraw ([http://localhost:8080/api/withdraw](http://localhost:8080/api/account))

\- request parameter: senderPrivateKey(출금될 사용자 비밀키), amount(입금 금액)

\- response field: resultCode(응답 코드), senderUserId(출금된 사용자 user ID), balance(출금 후 잔액)

\- 테스트 케이스1 (예상 resultCode: SUCCESS)

-   requestBody

```
{
    "senderPrivateKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.FVQmVG9OP7Xf7-n9MNA2_n8598BdHgCFvm0CXguiyuc",
    "amount" : "1000000"
}
```

-   responseBody

```
{
    "resultCode": "SUCCESS",
    "senderUserId": "sender",
    "balance": 100000
}
```

\- 테스트 케이스2 (예상 resultCode: SENDERERROR)

-   requestBody

```
{
    "senderPrivateKey" : "abcde",
    "amount" : "1000000"
}
```

-   responseBody

```
{
    "resultCode": "SENDERERROR",
    "senderUserId": "",
    "balance": 0
}
```

\- 테스트 케이스3 (예상 resultCode: SENDERERROR)

-   requestBody

```
{
    "senderPrivateKey" : "",
    "amount" : "1000000"
}
```

-   responseBody

```
{
    "resultCode": "SENDERERROR",
    "senderUserId": "",
    "balance": 0
}
```

\- 테스트 케이스4 (예상 resultCode: INVALIDAMOUNT)

-   requestBody

```
{
    "senderPrivateKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.FVQmVG9OP7Xf7-n9MNA2_n8598BdHgCFvm0CXguiyuc",
    "amount" : "0"
}
```

-   responseBody

```
{
    "resultCode": "INVALIDAMOUNT",
    "senderUserId": "",
    "balance": 0
}
```

\- 테스트 케이스5 (예상 resultCode: INVALIDAMOUNT)

-   requestBody

```
{
    "senderPrivateKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.FVQmVG9OP7Xf7-n9MNA2_n8598BdHgCFvm0CXguiyuc",
    "amount" : "amount"
}
```

-   responseBody

```
{
    "resultCode": "INVALIDAMOUNT",
    "senderUserId": "",
    "balance": 0
}
```

\- 테스트 케이스6 (예상 resultCode: LACKOFMONEY)

-   requestBody

```
{
    "senderPrivateKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.FVQmVG9OP7Xf7-n9MNA2_n8598BdHgCFvm0CXguiyuc",
    "amount" : "7000000"
}
```

-   responseBody

```
{
    "resultCode": "LACKOFMONEY",
    "senderUserId": "sender",
    "balance": 100000
}
```

#### 4\. 송금 처리 API (POST)

\- URL: /api/remit ([http://localhost:8080/api/remit](http://localhost:8080/api/account))

\- request parameter: receiverPublicKey(입금 받을 사용자 공개 키), senderPrivateKey(출금될 사용자 비밀키), amount(입금 금액)

\- response field: resultCode(응답 코드), remitHistoryId(거래 식별키)

\- 테스트 케이스1 (예상 resultCode: 성공)

-   requestBody

```
{
    "receiverPublicKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWNlaXZlciIsImlhdCI6MTY2Mzg1MjE0NH0.LuwsgcMNB4w0ApquZ2i6-8PLiqP8X18Br6esywFLjmY",
    "senderPrivateKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.FVQmVG9OP7Xf7-n9MNA2_n8598BdHgCFvm0CXguiyuc",
    "amount" : "10000"
}
```

-   responseBody

```
{
    "returnCode": "SUCCESS",
    "remitHistoryId": "8"
}
```

\- 테스트 케이스2 (예상 resultCode: RECEIVERERROR)

-   requestBody

```
{
    "receiverPublicKey" : "",
    "senderPrivateKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.FVQmVG9OP7Xf7-n9MNA2_n8598BdHgCFvm0CXguiyuc",
    "amount" : "10000"
}
```

-   responseBody

```
{
    "returnCode": "RECEIVERERROR",
    "remitHistoryId": "11"
}
```

\- 테스트 케이스3 (예상 resultCode: SENDERERROR)

-   requestBody

```
{
    "receiverPublicKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWNlaXZlciIsImlhdCI6MTY2Mzg1MjE0NH0.LuwsgcMNB4w0ApquZ2i6-8PLiqP8X18Br6esywFLjmY",
    "senderPrivateKey" : "123456abcde",
    "amount" : "10000"
}
```

-   responseBody

```
{
    "returnCode": "SENDERERROR",
    "remitHistoryId": "14"
}
```

\- 테스트 케이스4 (예상 resultCode: LACKOFMONEY)

-   requestBody

```
{
    "receiverPublicKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyZWNlaXZlciIsImlhdCI6MTY2Mzg1MjE0NH0.LuwsgcMNB4w0ApquZ2i6-8PLiqP8X18Br6esywFLjmY",
    "senderPrivateKey" : "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZW5kZXIiLCJpYXQiOjE2NjM4NTIxMjN9.FVQmVG9OP7Xf7-n9MNA2_n8598BdHgCFvm0CXguiyuc",
    "amount" : "1000000"
}
```

-   responseBody

```
{
    "returnCode": "LACKOFMONEY",
    "remitHistoryId": "17"
}
```

#### 5\. 송금 처리 결과 조회 API (GET)

\- URL: /api/remit/info ([http://localhost:8080/api/remit/info](http://localhost:8080/api/account))

\- request parameter: id(거래 식별키)

\- response field: resultCode(거래 결과 코드)

\- 테스트 케이스1 (예상 결과: 정상 조회)

-   requestBody

```
{
    "id": "17"
}
```

-   responseBody

```
{
    "returnCode": "LACKOFMONEY"
}
```

\- 테스트 케이스2 (예상 결과: 조회 실패)

-   requestBody

```
{
    "id": "99"
}
```

-   responseBody

```
{
    "timestamp": "2022-09-22T16:01:13.584+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/remit/info"
}
```

[##_Image|kage@cGQEiL/btrMPfQggdM/HZItg8l0wPhQDS6ToHEFt0/img.png|CDM|1.3|{"originWidth":1730,"originHeight":132,"style":"alignCenter","caption":"NotExistRemitException"}_##]

\- 테스트 케이스3 (예상 결과: 조회 실패)

-   requestBody

```
{
    "id": "ff"
}
```

-   responseBody

```
{
    "timestamp": "2022-09-22T16:04:05.637+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/remit/info"
}
```

[##_Image|kage@IYikO/btrMNhhJW4k/44cz8zFv4Qy2lFjo2PO1u0/img.png|CDM|1.3|{"originWidth":1734,"originHeight":142,"style":"alignCenter","caption":"InvalidRemitIdException"}_##]

\- 테스트 케이스4 (예상 결과: 조회 실패)

-   requestBody

```
{
    "id": ""
}
```

-   responseBody

```
{
    "timestamp": "2022-09-22T16:05:22.305+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "path": "/api/remit/info"
}
```

[##_Image|kage@BV66T/btrMOWpNnQ7/KrAOR3w30HFKOKXE0LrOv1/img.png|CDM|1.3|{"originWidth":1730,"originHeight":138,"style":"alignCenter","caption":"InvalidRemitIdException"}_##]

#### DockerFile

```
FROM amazoncorretto:11-alpine3.13
ARG JAR_FILE=build/libs/*.jar
VOLUME /tmp
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```
