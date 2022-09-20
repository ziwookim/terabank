package terafintech.terabank.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.With;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Account {
    /**
     * 계좌 데이터
     */

    @Id @GeneratedValue
    @Column(name = "account_id")
    private Long id;

    /**
     *  userId: 사용자 ID
     *  4자리 이상
     */
    private String userId;

    /**
     * 잔액
     */
    private int money;

    /**
     * 입금 내역
     */
    @OneToMany(mappedBy = "receiver") // 읽기 전용
    private List<DepositHistory> depositHistories = new ArrayList<>();

    /**
     * 출금 내역
     */
    @OneToMany(mappedBy = "sender") // 읽기 전용
    private List<WithdrawHistory> withdrawHistories = new ArrayList<>();


    /**
     * 사용자 공개키 (public token)
     */
    private String publicKey;

    /**
     * 사용자 비밀키 (private token)
     */
    private String privateKey;

    /**
     * 입금 처리
     */
    @Transactional(rollbackFor = {Exception.class})
    public void addMoney(int amount) {
        this.setMoney(this.getMoney() + amount);
    }

    /**
     * 출금 처리
     */
    @Transactional(rollbackFor = {Exception.class})
    public void minusMoney(int amount) {
        this.setMoney(this.getMoney() - amount);
    }

}
