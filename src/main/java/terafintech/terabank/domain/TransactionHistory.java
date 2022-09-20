package terafintech.terabank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class TransactionHistory {
    /**
     *  송금 내역 데이터
     */

    @Id
    @GeneratedValue
    @Column(name = "transaction_history_id")
    private Long id;

    private String receiverPublicKey;

    private String senderPrivateKey;

    /**
     * 입금 내역 데이터
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "deposit_history_id")
    private DepositHistory depositHistory;

    /**
     * 출금 내역 데이터
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "withdraw_history_id")
    private WithdrawHistory withdrawHistory;

    /**
     * 송금(시도)시 입력된 송금액 드
     */
    private String amount;

    /**
     * 송금 시간
     */
    private LocalDateTime transactionDate;

    /**
     * 송금 처리 결과 코드
     */
    private TransactionResult resultCode;

    /**
     * 송금 데이터 생성
     */
    public static TransactionHistory createTransaction(DepositHistory depositHistory, WithdrawHistory withdrawHistory, String receiverPublicKey, String senderPrivateKey, String amount) {
        TransactionHistory transactionHistory = new TransactionHistory();

        if(depositHistory.getResultCode().equals(TransactionResult.SUCCESS) &&
                withdrawHistory.getResultCode().equals(TransactionResult.SUCCESS)) {
            transactionHistory.setResultCode(TransactionResult.SUCCESS);
        } else if(!depositHistory.getResultCode().equals(TransactionResult.SUCCESS)) {
            withdrawHistory.setResultCode(depositHistory.getResultCode());
            transactionHistory.setResultCode(depositHistory.getResultCode());
        } else {
            depositHistory.setResultCode(withdrawHistory.getResultCode());
            transactionHistory.setResultCode(withdrawHistory.getResultCode());
        }

        transactionHistory.setDepositHistory(depositHistory);
        transactionHistory.setWithdrawHistory(withdrawHistory);
        transactionHistory.setReceiverPublicKey(receiverPublicKey);
        transactionHistory.setSenderPrivateKey(senderPrivateKey);
        transactionHistory.setAmount(amount);
        transactionHistory.setTransactionDate(LocalDateTime.now());

        return transactionHistory;
    }

}
