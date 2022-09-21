package terafintech.terabank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.transform.Result;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class RemitHistory {
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
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "deposit_history_id")
//    private DepositHistory depositHistory;

    /**
     * 출금 내역 데이터
     */
//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "withdraw_history_id")
//    private WithdrawHistory withdrawHistory;

    /**
     * 송금(시도)시 입력된 송금액 금액
     */
    private String amount;

    /**
     * 송금 시간
     */
    private LocalDateTime transactionDate;

    /**
     * 송금 처리 결과 코드
     */
    private ResultCode resultCode;

    /**
     * 송금 데이터 생성
     */
    public static RemitHistory createTransaction(ResultCode depositResult, ResultCode withdrawResult, String receiverPublicKey, String senderPrivateKey, String amount) {
        RemitHistory remitHistory = new RemitHistory();

        if (depositResult == null || withdrawResult == null) {
            remitHistory.setResultCode(ResultCode.OTHERPROBLEMS);
        } else if(depositResult.equals(ResultCode.SUCCESS) &&
                withdrawResult.equals(ResultCode.SUCCESS)) {
            remitHistory.setResultCode(ResultCode.SUCCESS);
        } else if(!depositResult.equals(ResultCode.SUCCESS)) {
            remitHistory.setResultCode(depositResult);
        } else {
            remitHistory.setResultCode(withdrawResult);
        }

        remitHistory.setReceiverPublicKey(receiverPublicKey);
        remitHistory.setSenderPrivateKey(senderPrivateKey);
        remitHistory.setAmount(amount);
        remitHistory.setTransactionDate(LocalDateTime.now());

        return remitHistory;
    }

}
