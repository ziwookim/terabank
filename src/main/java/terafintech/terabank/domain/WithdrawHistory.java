package terafintech.terabank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class WithdrawHistory {
    /**
     * 출금 내역 데이터
     */

    @Id @GeneratedValue
    private Long id;

    /**
     * 출금 대상 계좌 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account sender;

    /**
     * 연관 송금 내역 데이터
     */
    @OneToOne(mappedBy = "withdrawHistory", fetch = FetchType.LAZY)
    private TransactionHistory transactionHistory;

    /**
     * 실제 출금액
     */
    private int amount;

    /**
     * 출금 시간
     */
    private LocalDateTime withdrawDate;

    /**
     * 출금 처리 결과 코드
     */
    private TransactionResult resultCode;

    public static WithdrawHistory createWithdraw(Account sender, int amount) {

        TransactionResult resultCode = TransactionResult.SUCCESS;

        WithdrawHistory withdrawHistory = new WithdrawHistory();

        /**
         * valid senderPrivateKey value
         */
        if(sender.equals(null)) {
            withdrawHistory.setResultCode(TransactionResult.SENDERERROR);
        } else {
            withdrawHistory.setSender(sender);
        }

        /**
         * valid sender's Money value
         */

        if(amount == -999) {
            withdrawHistory.setResultCode(TransactionResult.INVALIDAMOUNT);
        } else if(amount == -1) {
            withdrawHistory.setResultCode(TransactionResult.LACKOFMONEY);
        } else {
            withdrawHistory.setAmount(amount);
        }

        if(withdrawHistory.getResultCode().equals(TransactionResult.SUCCESS)) {
            withdrawHistory.setSender(sender);
            /**
             * 출금 처리
             */
//            withdrawHistory = withdraw(withdrawHistory);
            try{

                /**
                 * minus Money
                 */
                sender.minusMoney(withdrawHistory.getAmount());
                withdrawHistory.setResultCode(TransactionResult.SUCCESS);
            } catch(Exception e) {
                withdrawHistory.setResultCode(TransactionResult.OTHERPROBLEMS);
            }

        }

        withdrawHistory.setWithdrawDate(LocalDateTime.now());

        return withdrawHistory;
    }
}
