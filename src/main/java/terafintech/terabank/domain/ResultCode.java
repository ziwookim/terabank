package terafintech.terabank.domain;

public enum ResultCode {
    /**
     * 송금, 입금, 출금 시도 결과 코드 열거형 리스트
     */


    /**
     * 성공
     */
    SUCCESS(100),

    /**
     * 잔액부족
     */
    LACKOFMONEY(400),

    /**
     * 입금액 입력 오류
     */
    INVALIDAMOUNT(7777),

    /**
     * 출금자 오류
     */
    SENDERERROR(888),

    /**
     * 입금자 오류
     */
    RECEIVERERROR(999),

    /**
     * 기타 오류
     */
    OTHERPROBLEMS(6666);

    private int resultCode;

    ResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }
}
