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
    private RemitHistory remitHistory;

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
    private ResultCode resultCode;

    public static WithdrawHistory createWithdraw(Account sender, int amount) {

        WithdrawHistory withdrawHistory = new WithdrawHistory();
        withdrawHistory.setResultCode(ResultCode.SUCCESS);

        /**
         * valid senderPrivateKey value
         */
        if(sender == null) {
            withdrawHistory.setResultCode(ResultCode.SENDERERROR);
        } else {
            withdrawHistory.setSender(sender);
        }

        /**
         * valid sender's Money value
         */

        if(amount == -999) {
            withdrawHistory.setResultCode(ResultCode.INVALIDAMOUNT);
        } else if(amount == -1) {
            withdrawHistory.setResultCode(ResultCode.LACKOFMONEY);
        } else {
            withdrawHistory.setAmount(amount);
        }

        if(withdrawHistory.getResultCode().equals(ResultCode.SUCCESS)) {
            /**
             * 출금 처리
             */
//            withdrawHistory = withdraw(withdrawHistory);
            try{

                /**
                 * minus Money
                 */
                sender.minusBalance(withdrawHistory.getAmount());
                withdrawHistory.setResultCode(ResultCode.SUCCESS);
            } catch(Exception e) {
                withdrawHistory.setResultCode(ResultCode.OTHERPROBLEMS);
            }

        }

        withdrawHistory.setWithdrawDate(LocalDateTime.now());

        return withdrawHistory;
    }
}
