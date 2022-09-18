package terafintech.terabank.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class WithdrawHistory {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account sender;

    @OneToOne(mappedBy = "withdrawHistory", fetch = FetchType.LAZY)
    private TransactionHistory transactionHistory;

    private int amount;

    private LocalDateTime withdrawDate;

    private TransactionResult resultCode;
}
