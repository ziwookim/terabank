package terafintech.terabank.domain;

public enum TransactionResult {
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
    INVALIDAMOUNT(666),

    /**
     * 출금자 오류
     */
    SENDERERROR(888),

    /**
     * 입금자 오류
     */
    RECEIVERERROR(999);

    private int resultCode;

    TransactionResult(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }
}
