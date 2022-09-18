package terafintech.terabank.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class TransactionHistory {

    @Id
    @GeneratedValue
    @Column(name = "transaction_history_id")
    private Long id;

    private String receiverPublicKey;

    private String senderPrivateKey;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deposit_history_id")
    private DepositHistory depositHistory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "withdraw_history_id")
    private WithdrawHistory withdrawHistory;

    /**
     * 송금액
     */
    private int amount;

    /**
     * 송금 시간
     */
    private LocalDateTime transactionDate;

    /**
     * 응답 코드
     */
    private TransactionResult resultCode;

//    @Transactional
//    public TransactionHistory tryCreateRemit(String receiverPublicKey, Account receiver, String senderPrivateKey, Account sender, int amount) {
//
//        TransactionHistory transactionHistory = new TransactionHistory();
//        DepositHistory depositHistory  = new DepositHistory();
//        WithdrawHistory withdrawHistory = new WithdrawHistory();
//
//        transactionHistory.setReceiverPublicKey(receiverPublicKey);
//        transactionHistory.setSenderPrivateKey(senderPrivateKey);
//
//        depositHistory.setReceiver(receiver);
//        withdrawHistory.setSender(sender);
//
//        transactionHistory.setAmount(amount);
//        depositHistory.setAmount(amount);
//        withdrawHistory.setAmount(amount);
//
//        /**
//         * Call Deposit API
//         */
//
//        /**
//         * Call Withdraw API
//         */
//
//        transactionHistory.setDepositHistory(depositHistory);
//        transactionHistory.setWithdrawHistory(withdrawHistory);
//
//
//        transactionHistory.setResultCode(TransactionResult.SUCCESS);
//
//        transactionHistory.setTransactionDate(LocalDateTime.now());
//
//        return transactionHistory;
//    }

//    @Transactional
//    public TransactionHistory recordTransactionHistory(String receiverPublicKey, String senderPrivateKey, String amount, TransactionResult resultCode) {
//        TransactionHistory transactionHistory = new TransactionHistory();
//        transactionHistory.setReceiverPublicKey(receiverPublicKey);
//        transactionHistory.setSenderPrivateKey(senderPrivateKey);
//        if(!resultCode.equals(TransactionResult.INVALIDAMOUNT)) {
//            transactionHistory.setAmount(Integer.parseInt(amount));
//        } else {
//            transactionHistory.setAmount(0);
//        }
//        transactionHistory.setResultCode(TransactionResult.SUCCESS);
//        transactionHistory.setTransactionDate(LocalDateTime.now());
//
//        return transactionHistory;
//    }

}
