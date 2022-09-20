package terafintech.terabank.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import terafintech.terabank.domain.DepositHistory;
import terafintech.terabank.domain.WithdrawHistory;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class WithdrawHistoryRepository {

    private EntityManager em;

    public void save(WithdrawHistory withdrawHistory) {
        em.persist(withdrawHistory);
    }

    public WithdrawHistory findOne(Long id) {
        return em.find(WithdrawHistory.class, id);
    }
}
