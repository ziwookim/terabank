package terafintech.terabank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class DepositHistory {
    /**
     * 입금 내역 데이터
     */

    @Id @GeneratedValue
    @Column(name = "deposit_history_id")
    private Long id;

    /**
     * 입금 대상 계좌 정보
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account receiver;

    /**
     * 연관 송금 내역 데이드
     */
//    @OneToOne(mappedBy = "depositHistory", fetch = FetchType.LAZY)
//    private RemitHistory remitHistory;

    /**
     * 실제 입금액
     */
    private int amount;

    /**
     * 입금 시간
     */
    private LocalDateTime depositDate;

    /**
     *  입금 처리 결과 코드
     */
    private ResultCode resultCode;

    /**
     * 입금 데이터 생성
     */
    public static DepositHistory createDeposit(Account receiver, int amount) {

        DepositHistory depositHistory = new DepositHistory();
        depositHistory.setResultCode(ResultCode.SUCCESS);
        depositHistory.setAmount(0);
        /**
         * valid receiverPublicKey value
         */
        if(receiver == null) {
            depositHistory.setResultCode(ResultCode.RECEIVERERROR);
        } else {
            depositHistory.setReceiver(receiver);
        }

        if(amount < 0) {
            depositHistory.setAmount(0);
            depositHistory.setResultCode(ResultCode.INVALIDAMOUNT);
        }
        depositHistory.setAmount(amount);

        if(depositHistory.getResultCode().equals(ResultCode.SUCCESS)) {
            /**
             * 입금 처리
             */
//            depositHistory = deposit(depositHistory);
            try{
                /**
                 * add Money
                 */
                receiver.addBalance(depositHistory.getAmount());
                depositHistory.setResultCode(ResultCode.SUCCESS);
            } catch(Exception e) {
                depositHistory.setResultCode(ResultCode.OTHERPROBLEMS);
            }
        }

        depositHistory.setDepositDate(LocalDateTime.now());

        return depositHistory;
    }
}
