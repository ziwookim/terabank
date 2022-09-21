package terafintech.terabank.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import terafintech.terabank.domain.DepositHistory;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class DepositHistoryRepository {

    private final EntityManager em;

    public void save(DepositHistory depositHistory) {
        em.persist(depositHistory);
    }

    public DepositHistory findOne(Long id) {
        return em.find(DepositHistory.class, id);
    }
}
