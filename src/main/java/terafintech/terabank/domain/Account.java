package terafintech.terabank.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.With;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Account {

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
     * 거래 내역
     */
//    @OneToMany(mappedBy = "transactionHistory")
//    private List<TransactionHistory> transactionHistoryList = new ArrayList<>();

    @OneToMany(mappedBy = "receiver") // 읽기 전용
    private List<DepositHistory> depositHistories = new ArrayList<>();

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
}
