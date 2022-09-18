package terafintech.terabank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class DepositHistory {

    @Id
    @GeneratedValue
    @Column(name = "deposit_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account receiver;

    @OneToOne(mappedBy = "depositHistory", fetch = FetchType.LAZY)
    private TransactionHistory transactionHistory;

    private int amount;

    private LocalDateTime depositDate;

    private TransactionResult resultCode;


}
